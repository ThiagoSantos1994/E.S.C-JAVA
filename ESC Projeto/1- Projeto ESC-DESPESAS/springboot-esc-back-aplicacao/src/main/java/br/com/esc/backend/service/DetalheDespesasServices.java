package br.com.esc.backend.service;

import br.com.esc.backend.domain.*;
import br.com.esc.backend.repository.AplicacaoRepository;
import br.com.esc.backend.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.esc.backend.utils.GlobalUtils.getAnoAtual;
import static br.com.esc.backend.utils.GlobalUtils.getMesAtual;
import static br.com.esc.backend.utils.MotorCalculoUtils.*;
import static br.com.esc.backend.utils.ObjectUtils.*;
import static br.com.esc.backend.utils.VariaveisGlobais.*;
import static java.util.Arrays.asList;

@Service
@RequiredArgsConstructor
@Slf4j
public class DetalheDespesasServices {

    private final AplicacaoRepository repository;
    private final DespesasParceladasServices despesasParceladasServices;
    private final ConsolidacaoService consolidacaoService;

    public DetalheDespesasMensaisDTO obterDetalheDespesaMensal(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String ordem) {
        var despesaMensal = repository.getDespesasMensais(idDespesa, idFuncionario, idDetalheDespesa);

        if (despesaMensal.size() <= 0) {
            return new DetalheDespesasMensaisDTO();
        } else {
            var tpRelatorio = despesaMensal.get(0).getTpRelatorio();

            despesaMensal.get(0).setVlTotalDespesa((tpRelatorio.equalsIgnoreCase("S")) ?
                    convertToMoedaBR(repository.getCalculoDespesaTipoRelatorio(idDespesa, idDetalheDespesa, idFuncionario)) :
                    convertToMoedaBR(convertStringToDecimal(repository.getValorTotalDespesa(idDespesa, idDetalheDespesa, idFuncionario))));

            despesaMensal.get(0).setVlLimiteExibicao((despesaMensal.get(0).getTpReferenciaSaldoMesAnterior().equalsIgnoreCase("S")) ?
                    this.obterSubTotalDespesa((idDespesa - 1), idDetalheDespesa, idFuncionario, (tpRelatorio.equals("S") ? "relatorio" : "default")).getVlSubTotalDespesa() :
                    despesaMensal.get(0).getVlLimite());

            despesaMensal.get(0).setDsExtratoDespesa(this.obterExtratoDespesasMes(idDespesa, idDetalheDespesa, idFuncionario, "detalheDespesas").getMensagem());
        }

        var detalheDespesasMensaisList = repository.getDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario, parserOrdem(ordem)).stream()
                .filter(d -> isNull(d.getIdDespesaConsolidacao()) || d.getIdDespesaConsolidacao() == 0)
                .collect(Collectors.toList());

        var isContemDespesasConsolidadas = detalheDespesasMensaisList.stream()
                .filter(d -> isNotNull(d.getIdConsolidacao()) && d.getIdConsolidacao() > 0)
                .filter(d -> d.getTpLinhaSeparacao().equals("N"))
                .collect(Collectors.toList())
                .size();

        if (isContemDespesasConsolidadas > 0) {
            List<DetalheDespesasMensaisDAO> newDetalheDespesasMensaisList = new ArrayList<>();
            for (DetalheDespesasMensaisDAO detalheDespesas : detalheDespesasMensaisList) {
                if ((isNotNull(detalheDespesas.getIdConsolidacao()) && detalheDespesas.getIdConsolidacao() > 0) && detalheDespesas.getTpLinhaSeparacao().equals("N")) {
                    var valorTotalDespesa = repository.getCalculoTotalDespesaConsolidada(detalheDespesas.getIdDespesa(), detalheDespesas.getIdDetalheDespesa(), detalheDespesas.getIdConsolidacao(), detalheDespesas.getIdFuncionario());
                    var valorTotalDespesaPaga = repository.getCalculoTotalDespesaConsolidadaPaga(detalheDespesas.getIdDespesa(), detalheDespesas.getIdDetalheDespesa(), detalheDespesas.getIdConsolidacao(), detalheDespesas.getIdFuncionario());

                    detalheDespesas.setVlTotal(convertDecimalToString(valorTotalDespesa));
                    detalheDespesas.setVlTotalPago(convertDecimalToString(valorTotalDespesaPaga));
                }

                newDetalheDespesasMensaisList.add(detalheDespesas);
            }
            detalheDespesasMensaisList.clear();
            detalheDespesasMensaisList.addAll(newDetalheDespesasMensaisList);
        }


        return DetalheDespesasMensaisDTO.builder()
                .sizeDetalheDespesaMensalVB(detalheDespesasMensaisList.size())
                .despesaMensal(despesaMensal.get(0))
                .detalheDespesaMensal(detalheDespesasMensaisList)
                .build();
    }

    public void gravarDetalheDespesasMensais(DetalheDespesasMensaisDAO detalheDAO, boolean isParcelaAmortizacao) throws Exception {
        var bIsParcelaAdiada = false;
        var despesa = repository.getDespesaMensalPorFiltro(detalheDAO.getIdDespesa(), detalheDAO.getIdDetalheReferencia(), detalheDAO.getIdFuncionario());
        if (null != despesa && despesa.getTpRelatorio().equalsIgnoreCase("S") && detalheDAO.getTpRelatorio().equalsIgnoreCase("S")) {
            //Se a despesa referencia for do tipo relatorio e o detalhe tambem, não permite a gravação dos dados.
            return;
        }

        /*Valida se a despesa é do tipo parcelada, se sim, busca o valor total a pagar da tabela de parcelas*/
        if (detalheDAO.getIdDespesaParcelada() > 0) {
            var referencia = this.obterReferenciaMesProcessamento(detalheDAO.getIdDespesa(), detalheDAO.getIdFuncionario());
            var dataVencimento = (referencia.getDsMes() + "/" + referencia.getDsAno());

            /*Fluxo especifico para gravacao de parcelas amortizadas*/
            if (isParcelaAmortizacao) {
                var referenciaParcela = repository.getParcelasPorFiltro(detalheDAO.getIdDespesaParcelada(), detalheDAO.getIdParcela(), "N", detalheDAO.getIdFuncionario());
                dataVencimento = referenciaParcela.get(0).getDsDataVencimento();
            }

            ParcelasDAO parcela = repository.getParcelaPorDataVencimento(detalheDAO.getIdDespesaParcelada(), dataVencimento, detalheDAO.getIdFuncionario());
            detalheDAO.setVlTotal(parcela.getVlParcela());
            detalheDAO.setDsDescricao(DESCRICAO_DESPESA_PARCELADA);
            detalheDAO.setIdParcela(parcela.getIdParcela());
            bIsParcelaAdiada = parcela.getTpParcelaAdiada().equalsIgnoreCase("S");
            detalheDAO.setTpParcelaAdiada(bIsParcelaAdiada ? "S" : detalheDAO.getTpParcelaAdiada());
        }

        if (detalheDAO.getTpStatus().equalsIgnoreCase(PENDENTE)) {
            detalheDAO.setVlTotalPago(VALOR_ZERO);
            detalheDAO.setTpMeta(isEmpty(detalheDAO.getTpMeta()) ? "N" : detalheDAO.getTpMeta());
            detalheDAO.setTpParcelaAmortizada(isEmpty(detalheDAO.getTpParcelaAmortizada()) ? "N" : detalheDAO.getTpParcelaAmortizada());
            detalheDAO.setTpParcelaAdiada(isEmpty(detalheDAO.getTpParcelaAdiada()) ? "N" : detalheDAO.getTpParcelaAdiada());
        }

        //Retira os espaços em branco recebido na request pelo frontend
        detalheDAO.setVlTotal(this.removeNBSP_html(detalheDAO.getVlTotal()));
        detalheDAO.setVlTotalPago(this.removeNBSP_html(detalheDAO.getVlTotalPago()));

        if (isNotNull(detalheDAO.getIdOrdem()) && this.isDetalheDespesaExistente(detalheDAO)) {
            log.info("Atualizando DetalheDespesaMensal: request = {}", detalheDAO);
            repository.updateDetalheDespesasMensais(detalheDAO);
            despesasParceladasServices.validaStatusDespesaParcelada(detalheDAO.getIdDespesa(), detalheDAO.getIdDetalheDespesa(), detalheDAO.getIdDespesaParcelada(), detalheDAO.getIdParcela(), detalheDAO.getIdFuncionario(), detalheDAO.getTpStatus(), false);
        } else {
            var idOrdemInsert = detalheDAO.getTpRelatorio().equalsIgnoreCase("N") ?
                    repository.getMaxOrdemDetalheDespesasMensais(detalheDAO.getIdDespesa(), detalheDAO.getIdDetalheDespesa(), detalheDAO.getIdFuncionario()) :
                    repository.getMaxOrdemDetalheDespesasTipoRelatorio(detalheDAO.getIdDespesa(), detalheDAO.getIdFuncionario());

            detalheDAO.setIdOrdem(idOrdemInsert);

            log.info("Inserindo DetalheDespesaMensal: request = {}", detalheDAO);
            repository.insertDetalheDespesasMensais(asList(detalheDAO));

            if (bIsParcelaAdiada) {
                var valorParcelaAdiantada = repository.getMaxValorParcela(detalheDAO.getIdDespesaParcelada(), detalheDAO.getIdFuncionario());
                repository.updateDetalheDespesasMensaisParcelaAdiada(detalheDAO.getIdDespesa(), detalheDAO.getIdDetalheDespesa(), detalheDAO.getIdDespesaParcelada(), "Despesa parcelada adiantada no fluxo de parcelas.", "", valorParcelaAdiantada, detalheDAO.getIdFuncionario());
            }

            this.ordenarRegistrosAtualizacao(detalheDAO.getIdDespesa(), detalheDAO.getIdDetalheDespesa(), detalheDAO.getIdFuncionario(), "prazo");
        }

        // Grava o log somente das despesas alteradas com status PENDENTE
        if (detalheDAO.getTpStatus().equalsIgnoreCase(PENDENTE)) {
            repository.insertDetalheDespesasMensaisLogs(detalheDAO.getIdDespesa(), detalheDAO.getIdDetalheDespesa(), detalheDAO.getIdOrdem(), detalheDAO.getIdFuncionario(), detalheDAO.getVlTotal());
        }

        //Valida se existem observacoes inseridas pelo editor de valores, caso sim, concatena com as observações existentes na base.
        if (!isEmpty(detalheDAO.getDsObservacoesEditorValores())) {
            var observacoesDAO = this.getObservacoesDetalheDespesa(detalheDAO.getIdDespesa(), detalheDAO.getIdDetalheDespesa(), detalheDAO.getIdOrdem(), detalheDAO.getIdFuncionario()).getObservacoes();

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((isEmpty(observacoesDAO) ? "" : observacoesDAO));
            stringBuilder.append(detalheDAO.getDsObservacoesEditorValores().replace("\\n", "\n"));

            var request = ObservacoesDetalheDespesaRequest.builder()
                    .idDespesa(detalheDAO.getIdDespesa())
                    .idDetalheDespesa(detalheDAO.getIdDetalheDespesa())
                    .idFuncionario(detalheDAO.getIdFuncionario())
                    .idOrdem(detalheDAO.getIdOrdem())
                    .dsObservacoes(stringBuilder.toString())
                    .build();

            this.gravarObservacoesDetalheDespesa(request);
        }
    }

    public void ordenarRegistrosAtualizacao(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String ordem) {
        // Metodo especifico para ordenar os dados apos alguma atualizacao (inclusao\\update\\exclusao)
        Integer iOrdemNova = 1;
        for (DetalheDespesasMensaisDAO detalheDespesas : repository.getDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario, parserOrdem(ordem))) {
            log.info("Ordenando registros listaDetalheDespesas: idDespesa = {}, idDetalheDespesa = {}, idOrdemAntiga = {}, idOrdemNova = {}", detalheDespesas.getIdDespesa(), detalheDespesas.getIdDetalheDespesa(), detalheDespesas.getIdOrdem(), iOrdemNova);
            this.alterarOrdemRegistroDetalheDespesas(detalheDespesas.getIdDespesa(), detalheDespesas.getIdDetalheDespesa(), detalheDespesas.getIdOrdem(), iOrdemNova, detalheDespesas.getIdFuncionario());
            iOrdemNova++;
        }
    }

    public void ordenarListaDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String ordem) {
        Integer iOrdemNova = 1;
        List<DetalheDespesasMensaisDAO> listDespesasOrdenadas = new ArrayList<>();

        //log.info("Excluindo todos os logs anteriores para nova gravacao...");
        //repository.deleteDetalheDespesasMensaisLogs(idDespesa, idDetalheDespesa, idFuncionario);

        for (DetalheDespesasMensaisDAO detalheDespesas : repository.getDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario, parserOrdem(ordem))) {
            log.info("Mapeando registros para ordenar: ordenarPor: {} >> idDespesa = {}, idDetalheDespesa = {}, idOrdemAntiga = {}, idOrdemNova = {}", ordem, detalheDespesas.getIdDespesa(), detalheDespesas.getIdDetalheDespesa(), detalheDespesas.getIdOrdem(), iOrdemNova);
            detalheDespesas.setIdOrdem(iOrdemNova);
            listDespesasOrdenadas.add(detalheDespesas);

            log.info("Incluindo log detalheDespesasMensaisLogs - request: {}", detalheDespesas);
            repository.insertDetalheDespesasMensaisLogs(detalheDespesas.getIdDespesa(), detalheDespesas.getIdDetalheDespesa(), detalheDespesas.getIdOrdem(), detalheDespesas.getIdFuncionario(), detalheDespesas.getVlTotal());
            iOrdemNova++;
        }

        //Exclui todos os registros
        log.info("Excluindo todos os registros para gravacao...");
        repository.deleteDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario);

        log.info("Incluindo todos os registros com a nova ordenacao");
        repository.insertDetalheDespesasMensais(listDespesasOrdenadas);
    }

    public List<CategoriaDespesasDAO> getSubTotalCategoriaDespesa(Integer idDespesa, Integer idFuncionario) {
        return repository.getSubTotalCategoriaDespesa(idDespesa, idFuncionario);
    }

    public void gravarDespesasMensais(DespesasMensaisDAO mensaisDAO) {
        if (mensaisDAO.getTpLinhaSeparacao().equalsIgnoreCase("N")) {
            if (mensaisDAO.getTpEmprestimo().equalsIgnoreCase("S") && mensaisDAO.getIdEmprestimo() > 0) {
                var idEmprestimo = repository.getCodigoEmprestimo(mensaisDAO.getDsTituloDespesa(), mensaisDAO.getIdFuncionario());
                if (idEmprestimo > 0) {
                    mensaisDAO.setDsNomeDespesa(DESCRICAO_DESPESA_EMPRESTIMO);
                    mensaisDAO.setVlLimite(repository.getCalculoTotalDespesa(mensaisDAO.getIdDespesa(), mensaisDAO.getIdDetalheDespesa(), mensaisDAO.getIdFuncionario()).toString());
                    mensaisDAO.setTpReferenciaSaldoMesAnterior("N");
                }
            }
        }

        if (this.isDespesaExistente(mensaisDAO)) {
            log.info("Atualizando despesa mensal >>>  {}", mensaisDAO);
            repository.updateDespesasMensais(mensaisDAO);
        } else {
            var idOrdemInsert = isNull(mensaisDAO.getIdOrdemExibicao()) ?
                    repository.getMaxOrdemDespesasMensais(mensaisDAO.getIdDespesa(), mensaisDAO.getIdFuncionario()) :
                    mensaisDAO.getIdOrdemExibicao();

            mensaisDAO.setIdOrdemExibicao(idOrdemInsert);

            log.info("Inserindo despesa mensal >>>  {}", mensaisDAO);
            repository.insertDespesasMensais(mensaisDAO);
        }

        this.validaDespesaTipoDebitoCartao(mensaisDAO, false);
    }

    public void deleteDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idFuncionario) throws Exception {
        despesasParceladasServices.isDespesaParceladaExcluida(idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
        this.validaDespesaTipoDebitoCartao(this.mensaisDAOMapper(idDespesa, idDetalheDespesa, idFuncionario), true);

        if (isNull(idOrdem) || idOrdem == -1) {
            repository.deleteDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario);
            repository.deleteDetalheDespesasMensaisLogs(idDespesa, idDetalheDespesa, idFuncionario);
            repository.deleteTodasObservacaoDetalheDespesaMensal(idDespesa, idDetalheDespesa, idFuncionario);
        } else {
            repository.deleteDetalheDespesasMensaisPorFiltro(idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
            repository.deleteDetalheDespesasMensaisLogsPorFiltro(idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
            repository.deleteObservacaoDetalheDespesasMensais(idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
        }

        repository.updateDetalheDespesasMensaisSemRelatorio(idDespesa, idDetalheDespesa, idFuncionario);
    }

    public StringResponse getObservacoesDetalheDespesa(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idFuncionario) {
        var result = repository.getObservacoesDetalheDespesa(idDespesa, idDetalheDespesa, idOrdem, idFuncionario);

        log.info("Consultando observacoes detalhe despesa >>> despesaID: {} - response: {}", idDespesa, result);
        return StringResponse.builder()
                .observacoes(result)
                .build();
    }

    public void gravarObservacoesDetalheDespesa(ObservacoesDetalheDespesaRequest request) {
        var qtdeObservacoes = repository.getQuantidadeObservacoesDetalheDespesa(request.getIdDespesa(), request.getIdDetalheDespesa(), request.getIdOrdem(), request.getIdFuncionario());

        if (qtdeObservacoes == 0) {
            repository.insertObservacaoDetalheDespesaMensal(request);
        } else {
            repository.updateObservacaoDetalheDespesaMensal(request);
        }

        log.info("Observacoes da despesa gravada com sucesso! >>> despesaID: {}", request.getIdDespesa());
    }

    public void validaDespesaTipoDebitoCartao(DespesasMensaisDAO mensaisDAO, Boolean bExcluirDespesa) {
        if (ObjectUtils.isEmpty(mensaisDAO) || mensaisDAO.getTpLinhaSeparacao().equalsIgnoreCase("S")) {
            return;
        }

        var despesaTipoDebtCart = repository.getDespesasFixasTipoDebitoCartao(mensaisDAO.getIdDespesa(), mensaisDAO.getIdFuncionario());

        if (mensaisDAO.getTpDebitoCartao().equalsIgnoreCase("S")) {
            if (bExcluirDespesa) {
                repository.deleteDespesasFixasMensaisPorFiltro(despesaTipoDebtCart.getIdDespesa(), despesaTipoDebtCart.getIdOrdem(), despesaTipoDebtCart.getIdFuncionario());
                return;
            }

            var referencia = this.obterReferenciaMesProcessamento(mensaisDAO.getIdDespesa(), mensaisDAO.getIdFuncionario());

            var despesaFixa = DespesasFixasMensaisRequest.builder()
                    .idDespesa(referencia.getIdDespesa())
                    .idDetalheDespesaDebitoCartao(mensaisDAO.getIdDetalheDespesa())
                    .dsDescricao(TIPO_RESERVA_AUTOMATICA_DEBITO_CARTAO)
                    .vlTotal(mensaisDAO.getTpAnotacao().equalsIgnoreCase("S") ? "0,00" :
                            convertDecimalToString(repository.getCalculoValorDespesaTipoCartaoDebito(referencia.getIdDespesa(), mensaisDAO.getIdDetalheDespesa(), referencia.getIdFuncionario())))
                    .tpStatus("+")
                    .tpFixasObrigatorias("S")
                    .dsMes(referencia.getDsMes())
                    .dsAno(referencia.getDsAno())
                    .idFuncionario(referencia.getIdFuncionario())
                    .idOrdem(ObjectUtils.isEmpty(despesaTipoDebtCart) ? 0 : despesaTipoDebtCart.getIdOrdem())
                    .tpDespesaDebitoCartao("S")
                    .build();

            if (isNull(despesaTipoDebtCart)) {
                var idOrdemInsert = repository.getMaxOrdemDespesasFixasMensais(mensaisDAO.getIdDespesa(), mensaisDAO.getIdFuncionario());
                despesaFixa.setIdOrdem(idOrdemInsert);
                repository.insertDespesasFixasMensais(despesaFixa);
            } else {
                repository.updateDespesasFixasMensais(despesaFixa);
            }
        } else if (isNotNull(despesaTipoDebtCart)) {
            repository.deleteDespesasFixasMensaisTipoDebito(mensaisDAO.getIdDespesa(), mensaisDAO.getIdDetalheDespesa(), mensaisDAO.getIdFuncionario());
        }
    }

    public void baixarPagamentoDespesas(PagamentoDespesasRequest request) throws Exception {
        if (request.getTpStatus().equalsIgnoreCase(PENDENTE)) {
            var bProcessamentoAdiantamentoParcelas = request.getIsProcessamentoAdiantamentoParcelas();

            if ((bProcessamentoAdiantamentoParcelas.equals(true) && request.getIdDetalheDespesa() > 0) || bProcessamentoAdiantamentoParcelas.equals(false)) {
                var existeParcelasAdiadas = repository.getValidaDetalheDespesaComParcelaAdiada(request.getIdDespesa(), request.getIdDetalheDespesa(), request.getIdFuncionario());
                if (existeParcelasAdiadas.equalsIgnoreCase("N")) {
                    repository.updateStatusPagamentoDetalheDespesa(request.getVlTotal(), request.getVlTotalPago(), PAGO, request.getDsObservacoes(), request.getDsObservacoesComplementar(), request.getIdDespesa(), request.getIdDetalheDespesa(), request.getIdOrdem(), request.getIdFuncionario());
                }
            }

            if (bProcessamentoAdiantamentoParcelas.equals(false)) {
                despesasParceladasServices.validaStatusDespesaParcelada(request.getIdDespesa(), request.getIdDetalheDespesa(), request.getIdDespesaParcelada(), request.getIdParcela(), request.getIdFuncionario(), PAGO, false);
            }
        }

        /*Atualiza o stts pagamento para as linhas de separacao*/
        repository.updateStatusBaixaLinhaSeparacao(request.getIdDespesa(), request.getIdFuncionario());
    }

    public void baixarPagamentoDespesasConsolidadas(PagamentoDespesasRequest request) throws Exception {
        if (isNotNull(request.getIdConsolidacao()) && request.getIdConsolidacao() > 0 && request.getTpStatus().equals(PENDENTE)) {
            var listDespesasConsolidadas = consolidacaoService.obterListDetalheDespesasConsolidadas(request.getIdDespesa(), request.getIdDetalheDespesa(), request.getIdConsolidacao(), request.getIdFuncionario());

            for (DetalheDespesasMensaisDAO despesa : listDespesasConsolidadas) {
                log.info("Processando pagamento detalhe despesa consolidada - {}", despesa);

                var requestBaixa = PagamentoDespesasRequest.builder()
                        .idDespesa(despesa.getIdDespesa())
                        .idDetalheDespesa(despesa.getIdDetalheDespesa())
                        .idDespesaParcelada(despesa.getIdDespesaParcelada())
                        .idConsolidacao(despesa.getIdConsolidacao())
                        .idParcela(despesa.getIdParcela())
                        .idOrdem(despesa.getIdOrdem())
                        .idFuncionario(despesa.getIdFuncionario())
                        .vlTotal(despesa.getVlTotal())
                        .vlTotalPago(despesa.getVlTotal())
                        .tpStatus(despesa.getTpStatus())
                        .dsObservacoes(request.getDsObservacoes())
                        .dsObservacoesComplementar(request.getDsObservacoesComplementar())
                        .isProcessamentoAdiantamentoParcelas(false)
                        .build();

                this.baixarPagamentoDespesas(requestBaixa);
            }
        }
    }

    public void desfazerBaixaPagamentoDespesas(PagamentoDespesasRequest request) throws Exception {
        if (request.getTpStatus().equalsIgnoreCase(PAGO)) {
            var bProcessamentoAdiantamentoParcelas = request.getIsProcessamentoAdiantamentoParcelas();

            if ((bProcessamentoAdiantamentoParcelas.equals(true) && request.getIdDetalheDespesa() > 0) || bProcessamentoAdiantamentoParcelas.equals(false)) {
                var existeParcelasAdiadas = repository.getValidaDetalheDespesaComParcelaAdiada(request.getIdDespesa(), request.getIdDetalheDespesa(), request.getIdFuncionario());
                if (existeParcelasAdiadas.equalsIgnoreCase("N")) {
                    repository.updateStatusPagamentoDetalheDespesa(request.getVlTotal(), request.getVlTotalPago(), PENDENTE, request.getDsObservacoes(), request.getDsObservacoesComplementar(), request.getIdDespesa(), request.getIdDetalheDespesa(), request.getIdOrdem(), request.getIdFuncionario());
                }
            }

            if (bProcessamentoAdiantamentoParcelas.equals(false)) {
                despesasParceladasServices.validaStatusDespesaParcelada(request.getIdDespesa(), request.getIdDetalheDespesa(), request.getIdDespesaParcelada(), request.getIdParcela(), request.getIdFuncionario(), PENDENTE, false);
            }
        }

        /*Atualiza o stts pagamento para as linhas de separacao*/
        repository.updateStatusBaixaLinhaSeparacao(request.getIdDespesa(), request.getIdFuncionario());
    }

    public void desfazerBaixaPagamentoDespesasConsolidadas(PagamentoDespesasRequest request) throws Exception {
        if (request.getIdConsolidacao() > 0) {
            var listDespesasConsolidadas = consolidacaoService.obterListDetalheDespesasConsolidadas(request.getIdDespesa(), request.getIdDetalheDespesa(), request.getIdConsolidacao(), request.getIdFuncionario());

            for (DetalheDespesasMensaisDAO detalhe : listDespesasConsolidadas) {
                log.info("Desfazendo pagamento detalhe despesa consolidada - {}", detalhe);

                request = PagamentoDespesasRequest.builder()
                        .idDespesa(detalhe.getIdDespesa())
                        .idDetalheDespesa(detalhe.getIdDetalheDespesa())
                        .idDespesaParcelada(detalhe.getIdDespesaParcelada())
                        .idConsolidacao(detalhe.getIdConsolidacao())
                        .idParcela(detalhe.getIdParcela())
                        .idOrdem(detalhe.getIdOrdem())
                        .idFuncionario(detalhe.getIdFuncionario())
                        .vlTotal(detalhe.getVlTotal())
                        .vlTotalPago(VALOR_ZERO)
                        .tpStatus(detalhe.getTpStatus())
                        .dsObservacoes("")
                        .isProcessamentoAdiantamentoParcelas(false)
                        .build();

                this.desfazerBaixaPagamentoDespesas(request);
            }
        }
    }

    public void alterarOrdemRegistroDetalheDespesas(Integer idDespesa, Integer idDetalheDespesa, Integer iOrdemAtual, Integer iOrdemNova, Integer idFuncionario) {
        var iOrdemTemp1 = 9998;
        var iOrdemTemp2 = 9999;

        //Substitui o ID da despesa POSICAO ATUAL
        repository.updateDetalheDespesasMensaisOrdenacao(idDespesa, idDetalheDespesa, null, iOrdemAtual, iOrdemTemp1, idFuncionario);

        //Substitui o ID da despesa POSICAO NOVA
        repository.updateDetalheDespesasMensaisOrdenacao(idDespesa, idDetalheDespesa, null, iOrdemNova, iOrdemTemp2, idFuncionario);

        /*Nesta etapa realiza a alteração da posição fazendo o DE x PARA com base nos ID's temporarios*/
        repository.updateDetalheDespesasMensaisOrdenacao(idDespesa, idDetalheDespesa, null, iOrdemTemp1, iOrdemNova, idFuncionario);
        repository.updateDetalheDespesasMensaisOrdenacao(idDespesa, idDetalheDespesa, null, iOrdemTemp2, iOrdemAtual, idFuncionario);

        //Altera a ordem dos registros de LOGs
        this.alterarOrdemRegistroDetalheDespesasLogs(idDespesa, idDetalheDespesa, iOrdemAtual, iOrdemNova, idFuncionario);

        //Se houver observacoes altera a ordem do registro
        if (!isEmpty(repository.getObservacoesDetalheDespesa(idDespesa, idDetalheDespesa, iOrdemAtual, idFuncionario))) {
            this.alterarOrdemRegistroObservacoesDetalheDespesas(idDespesa, idDetalheDespesa, iOrdemAtual, iOrdemNova, idFuncionario);
        }
    }

    private void alterarOrdemRegistroObservacoesDetalheDespesas(Integer idDespesa, Integer idDetalheDespesa, Integer iOrdemAtual, Integer iOrdemNova, Integer idFuncionario) {
        var iOrdemTemp1 = 9998;
        var iOrdemTemp2 = 9999;

        //Substitui o ID da despesa POSICAO ATUAL
        repository.updateObservacaoDetalheDespesasMensaisOrdenacao(idDespesa, idDetalheDespesa, iOrdemAtual, iOrdemTemp1, idFuncionario);

        //Substitui o ID da despesa POSICAO NOVA
        repository.updateObservacaoDetalheDespesasMensaisOrdenacao(idDespesa, idDetalheDespesa, iOrdemNova, iOrdemTemp2, idFuncionario);

        /*Nesta etapa realiza a alteração da posição fazendo o DE x PARA com base nos ID's temporarios*/
        repository.updateObservacaoDetalheDespesasMensaisOrdenacao(idDespesa, idDetalheDespesa, iOrdemTemp1, iOrdemNova, idFuncionario);
        repository.updateObservacaoDetalheDespesasMensaisOrdenacao(idDespesa, idDetalheDespesa, iOrdemTemp2, iOrdemAtual, idFuncionario);
    }

    private void alterarOrdemRegistroDetalheDespesasLogs(Integer idDespesa, Integer idDetalheDespesa, Integer iOrdemAtual, Integer iOrdemNova, Integer idFuncionario) {
        var iOrdemTemp1 = 9998;
        var iOrdemTemp2 = 9999;

        //Substitui o ID da despesa POSICAO ATUAL
        repository.updateDetalheDespesasMensaisLogsOrdenacao(idDespesa, idDetalheDespesa, iOrdemAtual, iOrdemTemp1, idFuncionario);

        //Substitui o ID da despesa POSICAO NOVA
        repository.updateDetalheDespesasMensaisLogsOrdenacao(idDespesa, idDetalheDespesa, iOrdemNova, iOrdemTemp2, idFuncionario);

        /*Nesta etapa realiza a alteração da posição fazendo o DE x PARA com base nos ID's temporarios*/
        repository.updateDetalheDespesasMensaisLogsOrdenacao(idDespesa, idDetalheDespesa, iOrdemTemp1, iOrdemNova, idFuncionario);
        repository.updateDetalheDespesasMensaisLogsOrdenacao(idDespesa, idDetalheDespesa, iOrdemTemp2, iOrdemAtual, idFuncionario);
    }

    public StringResponse obterSubTotalDespesa(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String ordem) {
        var result = new BigDecimal(0);

        if (ordem.equals("relatorio")) {
            result = repository.getCalculoDespesaTipoRelatorio(idDespesa, idDetalheDespesa, idFuncionario);
        } else {
            result = repository.getCalculoTotalDespesa(idDespesa, idDetalheDespesa, idFuncionario);
        }

        return StringResponse.builder()
                .vlSubTotalDespesa(convertDecimalToString(result))
                .build();

    }

    public ExtratoDespesasDAO obterExtratoDespesasMes(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String tipo) {
        StringBuffer mensagemBuffer = new StringBuffer();
        var extrato = new ExtratoDespesasDAO();

        if (tipo.equalsIgnoreCase("cadastroParcelas")) {
            extrato = repository.getExtratoDespesasParceladasMes(getMesAtual(), getAnoAtual(), idFuncionario);

            mensagemBuffer.append(NESTE_MES_SERA_QUITADO);
            mensagemBuffer.append(extrato.getVlDespesas()).append("R$");
        } else {
            var despesasFixas = repository.getDespesasFixasMensaisPorID(idDespesa, idFuncionario);
            if (despesasFixas.size() == 0) {
                extrato.setMensagem(SEM_DESPESA_MENSAGEM_PADRAO);
                return extrato;
            }

            if (tipo.equalsIgnoreCase("lancamentosMensais")) {
                var qtdeDespesasParceladasMes = repository.getQuantidadeDespesasParceladasMes(idDespesa, idFuncionario);
                var qtdeDespesasQuitacaoMes = repository.getQuantidadeDespesasParceladasQuitacaoMes(idDespesa, idFuncionario);

                extrato.setQtDespesas(qtdeDespesasQuitacaoMes.getQtdeParcelas());
                extrato.setVlDespesas(convertDecimalToString(qtdeDespesasQuitacaoMes.getVlParcelas()));

                mensagemBuffer.append(NESTE_MES_SERA_QUITADO);
                mensagemBuffer.append(qtdeDespesasQuitacaoMes.getQtdeParcelas()).append("/").append(qtdeDespesasParceladasMes);
                mensagemBuffer.append(" Despesas Parceladas, Totalizando: ").append(convertToMoedaBR(qtdeDespesasQuitacaoMes.getVlParcelas())).append("R$");
                /*mensagemBuffer.append("                                   ");
                mensagemBuffer.append("[ Despesa (2022): ").append(convertDecimalToString(repository.getCalculoReceitaNegativaMES((idDespesa - 12), idFuncionario))).append("R$ ]");*/

                extrato.setMensagem(mensagemBuffer.toString());

            } else if (tipo.equalsIgnoreCase("detalheDespesas")) {
                extrato = repository.getExtratoDespesasMes(idDespesa, idDetalheDespesa, idFuncionario);

                var qtdeTotalDespParceladasMes = repository.getQuantidadeDetalheDespesasParceladasMes(idDespesa, idDetalheDespesa, idFuncionario);

                mensagemBuffer.append("Ref: ").append(despesasFixas.get(0).getDsMes().concat("/").concat(despesasFixas.get(0).getDsAno())
                        .concat(" - " + NESTE_MES_SERA_QUITADO));
                mensagemBuffer.append(extrato.getQtDespesas()).append("/").append(qtdeTotalDespParceladasMes);
                mensagemBuffer.append(" Despesas Parceladas, Totalizando: ").append(extrato.getVlDespesas()).append("R$");
            }
        }

        extrato.setMensagem(mensagemBuffer.toString());

        log.info("Consultando extrato despesa mes >> response: {}", extrato);
        return extrato;
    }

    private Boolean isDetalheDespesaExistente(DetalheDespesasMensaisDAO detalhe) {
        var filtro = DetalheDespesasMensaisDAO.builder()
                .idDespesa(detalhe.getIdDespesa())
                .idDetalheDespesa(detalhe.getIdDetalheDespesa())
                .idFuncionario(detalhe.getIdFuncionario())
                .idOrdem(detalhe.getIdOrdem())
                .build();

        var detalheDespesasMensais = repository.getDetalheDespesaMensalPorFiltro(filtro);
        if (isNull(detalheDespesasMensais)) {
            return false;
        }
        return true;
    }

    private Boolean isDespesaExistente(DespesasMensaisDAO despesa) {
        var despesaMensal = repository.getDespesaMensalPorFiltro(despesa.getIdDespesa(), despesa.getIdDetalheDespesa(), despesa.getIdFuncionario());
        if (isNull(despesaMensal)) {
            return false;
        }
        return true;
    }

    private DespesasMensaisDAO mensaisDAOMapper(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario) {
        var detalheDespesaMensal = this.obterDetalheDespesaMensal(idDespesa, idDetalheDespesa, idFuncionario, "default");
        if (ObjectUtils.isEmpty(detalheDespesaMensal.getDespesaMensal())) {
            return null;
        }

        return DespesasMensaisDAO.builder()
                .idDespesa(idDespesa)
                .idDetalheDespesa(idDetalheDespesa)
                .idFuncionario(idFuncionario)
                .tpLinhaSeparacao(ObjectUtils.isEmpty(detalheDespesaMensal.getDespesaMensal().getTpLinhaSeparacao()) ? null : detalheDespesaMensal.getDespesaMensal().getTpLinhaSeparacao())
                .tpDebitoCartao(detalheDespesaMensal.getDespesaMensal().getTpDebitoCartao())
                .tpAnotacao(ObjectUtils.isEmpty(detalheDespesaMensal.getDespesaMensal().getTpAnotacao()) ? "N" : detalheDespesaMensal.getDespesaMensal().getTpAnotacao())
                .build();
    }

    private DespesasFixasMensaisDAO obterReferenciaMesProcessamento(Integer idDespesa, Integer idFuncionario) {
        return repository.getDespesaFixaMensalPorFiltro(idDespesa, 1, idFuncionario);
    }


    public static String parserOrdem(String ordem) {
        if (ObjectUtils.isEmpty(ordem)) {
            return "a.id_Ordem";
        }

        return ordem.equals("prazo") ? "(c.nr_Parcela + '/' + CAST(b.nr_TotalParcelas AS VarChar(10))), a.id_Ordem"
                : ordem.equals("relatorio") ? "a.id_DespesaLinkRelatorio, a.id_DespesaParcelada, a.id_Ordem ASC" : "a.id_Ordem";
    }

    private String removeNBSP_html(String valor) {
        return valor.replace("\u00a0", "");
    }
}
