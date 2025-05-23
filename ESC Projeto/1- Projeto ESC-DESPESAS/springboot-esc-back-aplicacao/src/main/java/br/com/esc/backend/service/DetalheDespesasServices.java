package br.com.esc.backend.service;

import br.com.esc.backend.domain.*;
import br.com.esc.backend.exception.ErroNegocioException;
import br.com.esc.backend.repository.AplicacaoRepository;
import br.com.esc.backend.utils.DataUtils;
import br.com.esc.backend.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.esc.backend.service.DespesasParceladasServices.opcaoVisualizacaoParcelas;
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

    public DetalheDespesasMensaisDTO obterDetalheDespesaMensal(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String ordem, Boolean visualizarConsolidacao) {
        var despesaTipoRelatorio = "N";

        var despesaMensal = repository.getDespesasMensais(idDespesa, idFuncionario, idDetalheDespesa);

        if (despesaMensal.size() <= 0) {
            return new DetalheDespesasMensaisDTO();
        } else {
            despesaTipoRelatorio = despesaMensal.get(0).getTpRelatorio();

            despesaMensal.get(0).setVlTotalDespesa((despesaTipoRelatorio.equalsIgnoreCase("S")) ?
                    convertToMoedaBR(repository.getCalculoDespesaTipoRelatorio(idDespesa, idDetalheDespesa, idFuncionario)) :
                    convertToMoedaBR(convertStringToDecimal(repository.getValorTotalDespesa(idDespesa, idDetalheDespesa, idFuncionario))));

            despesaMensal.get(0).setVlLimiteExibicao((despesaMensal.get(0).getTpReferenciaSaldoMesAnterior().equalsIgnoreCase("S")) ?
                    this.obterSubTotalDespesa((idDespesa - 1), idDetalheDespesa, idFuncionario, (despesaTipoRelatorio.equals("S") ? "relatorio" : "default")).getVlSubTotalDespesa() :
                    despesaMensal.get(0).getVlLimite());

            despesaMensal.get(0).setDsExtratoDespesa(this.obterExtratoDespesasMes(idDespesa, idDetalheDespesa, idFuncionario, "detalheDespesas").getMensagem());
        }

        List<DetalheDespesasMensaisDAO> detalheDespesasMensaisList = despesaTipoRelatorio.equalsIgnoreCase("N") ?
                repository.getDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario, parserOrdem(ordem)) :
                repository.getDetalheDespesasMensaisTipoRelatorio(idDespesa, idDetalheDespesa, idFuncionario);

        if (visualizarConsolidacao) {
            detalheDespesasMensaisList = detalheDespesasMensaisList.stream()
                    .filter(d -> isNull(d.getIdDespesaConsolidacao()) || d.getIdDespesaConsolidacao() == 0)
                    .collect(Collectors.toList());
        } else {
            detalheDespesasMensaisList = detalheDespesasMensaisList.stream()
                    .filter(d -> isNull(d.getIdConsolidacao()) || d.getIdConsolidacao() == 0)
                    .collect(Collectors.toList());
        }

        var isContemDespesasConsolidadas = detalheDespesasMensaisList.stream()
                .filter(d -> isNotNull(d.getIdConsolidacao()) && d.getIdConsolidacao() > 0)
                .filter(d -> d.getTpLinhaSeparacao().equals("N"))
                .count();

        if (isContemDespesasConsolidadas > 0) {
            List<DetalheDespesasMensaisDAO> newDetalheDespesasMensaisList = new ArrayList<>();
            for (DetalheDespesasMensaisDAO detalheDespesas : detalheDespesasMensaisList) {
                if (detalheDespesas.getIdConsolidacao() > 0 && detalheDespesas.getTpLinhaSeparacao().equals("N")) {
                    var valorTotalDespesa = repository.getCalculoTotalDespesaConsolidada(detalheDespesas.getIdDespesa(), detalheDespesas.getIdDetalheDespesa(), detalheDespesas.getIdConsolidacao(), detalheDespesas.getIdFuncionario());
                    var valorTotalDespesaPaga = repository.getCalculoTotalDespesaConsolidadaPaga(detalheDespesas.getIdDespesa(), detalheDespesas.getIdDetalheDespesa(), detalheDespesas.getIdConsolidacao(), detalheDespesas.getIdFuncionario());

                    detalheDespesas.setVlTotal(convertDecimalToString(valorTotalDespesa));
                    detalheDespesas.setVlTotalPago(convertDecimalToString(valorTotalDespesaPaga));

                    //Adiciona a quantidade de despesas associadas a consolidação no nome da despesa
                    if (isNull(detalheDespesas.getDsTituloDespesa())) {
                        log.warn("Ocorreu um erro ao obter os dados da despesa consolidada, valide se existe na base.");
                        detalheDespesas.setDsTituloDespesa("Despesa Consolidada Não Localizada (ERRO).");
                    } else {
                        var qtdeDespesasConsolidadas = repository.getDespesasParceladasConsolidadas(idDespesa, idDetalheDespesa, detalheDespesas.getIdConsolidacao(), idFuncionario).size();
                        var tituloDespesaConsolidada = detalheDespesas.getDsTituloDespesa().concat(" (" + qtdeDespesasConsolidadas + ")");
                        detalheDespesas.setDsTituloDespesa(tituloDespesaConsolidada);
                    }
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

    public void gravarDetalheDespesasMensais(DetalheDespesasMensaisDAO detalheDAO) throws Exception {
        var bIsParcelaAdiada = false;
        var bIsParcelaAmortizada = false;
        var bIsDespesaParcelada = (detalheDAO.getIdDespesaParcelada() > 0);

        var despesa = repository.getDespesaMensalPorFiltro(detalheDAO.getIdDespesa(), detalheDAO.getIdDetalheReferencia(), detalheDAO.getIdFuncionario());
        if (null != despesa && despesa.getTpRelatorio().equalsIgnoreCase("S") && detalheDAO.getTpRelatorio().equalsIgnoreCase("S")) {
            //Se a despesa referencia for do tipo relatorio e o detalhe tambem, não permite a gravação dos dados.
            return;
        }

        if (bIsDespesaParcelada) {
            var referencia = this.obterReferenciaMesProcessamento(detalheDAO.getIdDespesa(), detalheDAO.getIdFuncionario());
            var dataVencimento = (referencia.getDsMes() + "/" + referencia.getDsAno());

            ParcelasDAO parcela = isNull(detalheDAO.getIdParcela()) ?
                    repository.getParcelaPorDataVencimento(detalheDAO.getIdDespesaParcelada(), dataVencimento, detalheDAO.getIdFuncionario()) :
                    repository.getParcelasPorFiltro(detalheDAO.getIdDespesaParcelada(), detalheDAO.getIdParcela(), null, detalheDAO.getIdFuncionario(), opcaoVisualizacaoParcelas(false, detalheDAO.getIdDespesaParcelada())).get(0);

            bIsParcelaAmortizada = parcela.getTpParcelaAmortizada().equalsIgnoreCase("S");

            if (!bIsParcelaAmortizada) {
                log.info("Parcela sem amortização, seguindo o fluxo validando a data de vencimento da parcela com a data da despesa.");

                if (!parcela.getDsDataVencimento().equals(dataVencimento)) {
                    throw new ErroNegocioException("Não foi localizada parcela com data de vencimento para ".concat(dataVencimento)
                            .concat(".\n\nVerifique o fluxo de parcelas e tente novamente."));
                }
            } else {
                log.info("Parcela amortizada, seguindo o fluxo sem validação de data de vencimento da parcela com a data da despesa.");

                //Permite a alteração do valor da parcela na Despesa Mensal somente se for uma parcela amortizada (19/05/2025)
                if (!isEmpty(detalheDAO.getVlTotal())) {
                    if (convertStringToDecimal(detalheDAO.getVlTotal()).compareTo(convertStringToDecimal(parcela.getVlParcela())) != 0) {
                        log.info("Identificado alteração no valor original da parcela: {} da despesa amortizada: {}. Valor Original: {} R$ - Novo Valor: {}R$ - O valor será alterado.", parcela.getIdParcela(), parcela.getIdDespesaParcelada(), parcela.getVlParcela(), detalheDAO.getVlTotal());

                        parcela.setVlParcela(detalheDAO.getVlTotal());
                        despesasParceladasServices.gravarParcela(parcela);
                    }
                }
            }

            detalheDAO.setVlTotal(parcela.getVlParcela());
            detalheDAO.setDsDescricao(DESCRICAO_DESPESA_PARCELADA);
            detalheDAO.setIdParcela(parcela.getIdParcela());
            bIsParcelaAdiada = parcela.getTpParcelaAdiada().equalsIgnoreCase("S");
            detalheDAO.setTpParcelaAdiada(bIsParcelaAdiada ? "S" : detalheDAO.getTpParcelaAdiada());
        }

        if (detalheDAO.getTpStatus().equalsIgnoreCase(PENDENTE)) {
            detalheDAO.setVlTotalPago(VALOR_ZERO);
            detalheDAO.setTpMeta(isEmpty(detalheDAO.getTpMeta()) ? "N" : detalheDAO.getTpMeta());
            detalheDAO.setTpParcelaAmortizada(bIsParcelaAmortizada ? "S" : "N");
            detalheDAO.setTpParcelaAdiada(isEmpty(detalheDAO.getTpParcelaAdiada()) ? "N" : detalheDAO.getTpParcelaAdiada());
        } else if (detalheDAO.getTpStatus().equalsIgnoreCase(PAGO)
                && (isEmpty(detalheDAO.getVlTotalPago()) || detalheDAO.getVlTotalPago().equalsIgnoreCase(VALOR_ZERO))) {
            //Se o status for alterado para PAGO e não for informado o valor pago, seta o valor total da despesa.
            detalheDAO.setVlTotalPago(detalheDAO.getVlTotal());
        }

        if (detalheDAO.getIdConsolidacao() > 0) {
            detalheDAO.setVlTotal(VALOR_ZERO);
            detalheDAO.setVlTotalPago(VALOR_ZERO);
            detalheDAO.setDsDescricao(DESCRICAO_DESPESA_CONSOLIDACAO);
        } else {
            //Retira os espaços em branco recebido na request pelo frontend
            detalheDAO.setVlTotal(this.removeNBSP_html(detalheDAO.getVlTotal()));
            detalheDAO.setVlTotalPago(this.removeNBSP_html(detalheDAO.getVlTotalPago()));
        }

        detalheDAO.setIsNovaLinhaEmBranco(isEmpty(detalheDAO.getIsNovaLinhaEmBranco()) ? Boolean.FALSE : detalheDAO.getIsNovaLinhaEmBranco());

        if (isNotNull(detalheDAO.getIdOrdem()) && this.isDetalheDespesaExistente(detalheDAO)) {
            log.info("Atualizando DetalheDespesaMensal: request = {}", detalheDAO);
            repository.updateDetalheDespesasMensais(detalheDAO);
            despesasParceladasServices.validaStatusDespesaParcelada(detalheDAO.getIdDespesa(), detalheDAO.getIdDetalheDespesa(), detalheDAO.getIdDespesaParcelada(), detalheDAO.getIdParcela(), detalheDAO.getIdFuncionario(), detalheDAO.getTpStatus(), false);
        } else {
            Integer idOrdemInclusao = 1;

            if (!detalheDAO.getIsNovaLinhaEmBranco()) {
                idOrdemInclusao = detalheDAO.getTpRelatorio().equalsIgnoreCase("S") ?
                        repository.getMaxOrdemDetalheDespesasTipoRelatorio(detalheDAO.getIdDespesa(), detalheDAO.getIdFuncionario()) :
                        detalheDAO.getIdDespesaParcelada().equals(0) ?
                                repository.getMaxOrdemDetalheDespesasMensaisNormal(detalheDAO.getIdDespesa(), detalheDAO.getIdDetalheDespesa(), detalheDAO.getIdFuncionario()) :
                                repository.getMaxOrdemDetalheDespesasMensais(detalheDAO.getIdDespesa(), detalheDAO.getIdDetalheDespesa(), detalheDAO.getIdFuncionario());
            }

            detalheDAO.setIdOrdem(idOrdemInclusao);

            log.info("Inserindo DetalheDespesaMensal: request = {}", detalheDAO);
            repository.insertDetalheDespesasMensais(asList(detalheDAO));

            if (bIsParcelaAdiada) {
                var valorParcelaAdiada = repository.getMaxValorParcela(detalheDAO.getIdDespesaParcelada(), detalheDAO.getIdFuncionario());
                repository.updateDetalheDespesasMensaisParcelaAdiada(detalheDAO.getIdDespesa(), detalheDAO.getIdDetalheDespesa(), detalheDAO.getIdDespesaParcelada(), "Despesa parcelada adiantada no fluxo de parcelas.", "", valorParcelaAdiada, detalheDAO.getIdFuncionario());
            }
        }

        this.gravarDetalheDespesasMensaisLogs(detalheDAO);
        this.gravarDetalheDespesasMensaisObservacao(detalheDAO);
    }

    private void gravarDetalheDespesasMensaisLogs(DetalheDespesasMensaisDAO detalheDAO) {
        // Grava o log somente das despesas alteradas com status PENDENTE
        if (detalheDAO.getTpStatus().equalsIgnoreCase(PENDENTE)) {
            var logsDAO = this.getHistoricoDetalheDespesa(detalheDAO.getIdDetalheDespesaLog(), detalheDAO.getIdDespesa(), detalheDAO.getIdDetalheDespesa(), detalheDAO.getIdFuncionario());

            StringBuilder sbLogs = new StringBuilder();
            sbLogs.append((isEmpty(logsDAO.getHistorico()) ? "" : logsDAO.getHistorico()));
            sbLogs.append((detalheDAO.getVlTotal().concat(" - ").concat(DataUtils.DataHoraAtual())).concat("\n").replace("\\n", "\n"));

            var qtdeLogs = repository.getQuantidadeLogsDetalheDespesa(detalheDAO.getIdDespesa(), detalheDAO.getIdDetalheDespesa(), detalheDAO.getIdDetalheDespesaLog(), detalheDAO.getIdFuncionario());
            if (qtdeLogs == 0) {
                Integer idLogNovo = repository.insertDetalheDespesasMensaisLogs(detalheDAO.getIdDespesa(), detalheDAO.getIdDetalheDespesa(), detalheDAO.getIdFuncionario(), sbLogs.toString());
                repository.updateDetalheDespesasMensaisLog(idLogNovo, detalheDAO.getIdDespesa(), detalheDAO.getIdDetalheDespesa(), detalheDAO.getIdOrdem(), detalheDAO.getIdFuncionario());
            } else {
                var request = LogsDetalheDespesaRequest.builder()
                        .idDetalheDespesaLog(detalheDAO.getIdDetalheDespesaLog())
                        .idDespesa(detalheDAO.getIdDespesa())
                        .idDetalheDespesa(detalheDAO.getIdDetalheDespesa())
                        .idFuncionario(detalheDAO.getIdFuncionario())
                        .idOrdem(detalheDAO.getIdOrdem())
                        .dsLogDespesa(sbLogs.toString())
                        .build();

                repository.updateLogsDetalheDespesaMensal(request);
            }
        }
    }

    private void gravarDetalheDespesasMensaisObservacao(DetalheDespesasMensaisDAO detalheDAO) {
        //Valida se existem observacoes inseridas pelo editor de valores, caso sim, concatena com as observações existentes na base.
        if (!isEmpty(detalheDAO.getDsObservacoesEditorValores())) {
            var observacoesDAO = this.getObservacoesDetalheDespesa(detalheDAO.getIdDespesa(), detalheDAO.getIdDetalheDespesa(), detalheDAO.getIdObservacao(), detalheDAO.getIdFuncionario()).getObservacoes();

            StringBuilder sbObservacoes = new StringBuilder();
            sbObservacoes.append((isEmpty(observacoesDAO) ? "" : observacoesDAO));
            sbObservacoes.append(detalheDAO.getDsObservacoesEditorValores().replace("\\n", "\n"));

            var request = ObservacoesDetalheDespesaRequest.builder()
                    .idObservacao(detalheDAO.getIdObservacao())
                    .idDespesa(detalheDAO.getIdDespesa())
                    .idDetalheDespesa(detalheDAO.getIdDetalheDespesa())
                    .idFuncionario(detalheDAO.getIdFuncionario())
                    .idOrdem(detalheDAO.getIdOrdem())
                    .dsObservacoes(sbObservacoes.toString())
                    .build();

            this.gravarObservacoesDetalheDespesa(request);
        }
    }

    public void ordenarListaDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String ordem) {
        Integer iOrdemNova = 1;
        List<DetalheDespesasMensaisDAO> listDespesasOrdenadas = new ArrayList<>();

        for (DetalheDespesasMensaisDAO detalheDespesas : repository.getDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario, parserOrdem(ordem))) {
            log.info("Mapeando registros para ordenar: ordenarPor: {} >> idDespesa = {}, idDetalheDespesa = {}, idOrdemAntiga = {}, idOrdemNova = {}", ordem, detalheDespesas.getIdDespesa(), detalheDespesas.getIdDetalheDespesa(), detalheDespesas.getIdOrdem(), iOrdemNova);

            detalheDespesas.setIdOrdem(iOrdemNova++);
            listDespesasOrdenadas.add(detalheDespesas);
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
        if (mensaisDAO.getTpLinhaSeparacao().equals("N")) {
            mensaisDAO.setIdEmprestimo(0);

            if (mensaisDAO.getTpEmprestimo().equals("S") || mensaisDAO.getTpEmprestimoAPagar().equals("S")) {
                mensaisDAO.setIdEmprestimo(mensaisDAO.getIdDetalheDespesa());
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
        isExcluirDetalheDespesaTipoRelatorio(idOrdem);

        despesasParceladasServices.isDespesaParceladaExcluida(idDespesa, idDetalheDespesa, idOrdem, idFuncionario);

        this.validaDespesaTipoDebitoCartao(this.mensaisDAOMapper(idDespesa, idDetalheDespesa, idFuncionario), true);

        if (isEmpty(idOrdem) || idOrdem == -1) {
            repository.deleteDetalheDespesasMensaisLogs(idDespesa, idDetalheDespesa, idFuncionario);
            repository.deleteTodasObservacaoDetalheDespesaMensal(idDespesa, idDetalheDespesa, idFuncionario);
            repository.deleteDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario);
            repository.updateDetalheDespesasMensaisSemRelatorio(idDespesa, idDetalheDespesa, idFuncionario);
        } else {
            var detalheDespesa = repository.getDetalheDespesaMensalPorFiltro(DetalheDespesasMensaisDAO.builder()
                    .idDespesa(idDespesa)
                    .idDetalheDespesa(idDetalheDespesa)
                    .idOrdem(idOrdem).build());

            repository.deleteDetalheDespesasMensaisLogsPorFiltro(idDespesa, idDetalheDespesa, detalheDespesa.getIdDetalheDespesaLog(), idFuncionario);
            repository.deleteObservacaoDetalheDespesasMensais(idDespesa, idDetalheDespesa, detalheDespesa.getIdObservacao(), idFuncionario);
            repository.deleteDetalheDespesasMensaisPorFiltro(idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
        }
    }

    public StringResponse getObservacoesDetalheDespesa(Integer idDespesa, Integer idDetalheDespesa, Integer idObservacao, Integer idFuncionario) {
        var result = repository.getObservacoesDetalheDespesa(idDespesa, idDetalheDespesa, idObservacao, idFuncionario);

        log.info("Consultando observacoes detalhe despesa >>> despesaID: {} - response: {}", idDespesa, result);
        return StringResponse.builder()
                .observacoes(result)
                .build();
    }

    public StringResponse getHistoricoDetalheDespesa(Integer idDetalheDespesaLog, Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario) {
        log.info("Consultando historico (Log) detalhe despesa >>> despesaID: {} - idDetalheDespesa: {} - idDetalheDespesaLog: {}", idDespesa, idDetalheDespesa, idDetalheDespesaLog);
        return StringResponse.builder()
                .historico(repository.getLogsDetalheDespesa(idDetalheDespesaLog, idDespesa, idDetalheDespesa, idFuncionario))
                .build();
    }

    public void gravarObservacoesDetalheDespesa(ObservacoesDetalheDespesaRequest request) {
        var qtdeObservacoes = repository.getQuantidadeObservacoesDetalheDespesa(request.getIdDespesa(), request.getIdDetalheDespesa(), request.getIdObservacao(), request.getIdFuncionario());

        if (qtdeObservacoes == 0) {
            Integer idObservacaoNova = repository.insertObservacaoDetalheDespesaMensal(request);
            repository.updateDetalheDespesasMensaisObservacao(idObservacaoNova, request.getIdDespesa(), request.getIdDetalheDespesa(), request.getIdOrdem(), request.getIdFuncionario());
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

    public void baixarPagamentoDespesa(PagamentoDespesasRequest request) throws Exception {
        if (request.getTpStatus().equalsIgnoreCase(PENDENTE)) {
            var isDespesaConsolidada = (request.getIdConsolidacao() > 0);
            if (isDespesaConsolidada) {
                request.setVlTotal(VALOR_ZERO);
                request.setVlTotalPago(VALOR_ZERO);
            }

            if (request.getIdDespesaParcelada() > 0) {
                var isParcelaAdiada = repository.getValidaParcelaAdiada(request.getIdDespesaParcelada(), request.getIdParcela(), request.getIdFuncionario());
                if (isParcelaAdiada.equalsIgnoreCase("S")) {
                    log.warn("Operação não permitida para despesas parceladas que foram adiadas..");
                    return;
                }
            }

            repository.updateStatusPagamentoDetalheDespesa(request.getVlTotal(), request.getVlTotalPago(), PAGO, request.getDsObservacoes(), request.getDsObservacoesComplementar(), request.getIdDespesa(), request.getIdDetalheDespesa(), request.getIdOrdem(), request.getIdFuncionario());
            despesasParceladasServices.validaStatusDespesaParcelada(request.getIdDespesa(), request.getIdDetalheDespesa(), request.getIdDespesaParcelada(), request.getIdParcela(), request.getIdFuncionario(), PAGO, false);
        }

        /*Atualiza o stts pagamento para as linhas de separacao*/
        repository.updateStatusBaixaLinhaSeparacao(request.getIdDespesa(), request.getIdFuncionario());
    }

    public void baixarPagamentoDespesasConsolidadas(PagamentoDespesasRequest request) throws Exception {
        if (request.getIdConsolidacao() > 0 && request.getTpStatus().equals(PENDENTE)) {
            log.info("Processando pagamento detalhe despesa consolidada - idConsolidacao: {}", request.getIdConsolidacao());

            var listDespesasConsolidadas = consolidacaoService.obterListDetalheDespesasConsolidadas(request.getIdDespesa(), request.getIdDetalheDespesa(), request.getIdConsolidacao(), request.getIdFuncionario());

            for (DetalheDespesasMensaisDAO despesa : listDespesasConsolidadas) {
                log.info("Processando pagamento despesa consolidada - {}", despesa);

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

                this.baixarPagamentoDespesa(requestBaixa);
            }
        }
    }

    public void desfazerBaixaPagamentoDespesas(PagamentoDespesasRequest request) throws Exception {
        if (request.getTpStatus().equalsIgnoreCase(PAGO)) {
            if (request.getIdDespesaParcelada() > 0) {
                var isParcelaAdiada = repository.getValidaParcelaAdiada(request.getIdDespesaParcelada(), request.getIdParcela(), request.getIdFuncionario());
                if (isParcelaAdiada.equalsIgnoreCase("S")) {
                    log.warn("Operação não permitida para despesas parceladas que foram adiadas..");
                    return;
                }
            }

            repository.updateStatusPagamentoDetalheDespesa(request.getVlTotal(), request.getVlTotalPago(), PENDENTE, request.getDsObservacoes(), request.getDsObservacoesComplementar(), request.getIdDespesa(), request.getIdDetalheDespesa(), request.getIdOrdem(), request.getIdFuncionario());
            despesasParceladasServices.validaStatusDespesaParcelada(request.getIdDespesa(), request.getIdDetalheDespesa(), request.getIdDespesaParcelada(), request.getIdParcela(), request.getIdFuncionario(), PENDENTE, false);
        }

        /*Atualiza o stts pagamento para as linhas de separacao*/
        repository.updateStatusBaixaLinhaSeparacao(request.getIdDespesa(), request.getIdFuncionario());
    }

    public void desfazerBaixaPagamentoDespesasConsolidadas(PagamentoDespesasRequest request) throws Exception {
        if (request.getIdConsolidacao() > 0) {
            var listDespesasConsolidadas = consolidacaoService.obterListDetalheDespesasConsolidadas(request.getIdDespesa(), request.getIdDetalheDespesa(), request.getIdConsolidacao(), request.getIdFuncionario());

            for (DetalheDespesasMensaisDAO detalhe : listDespesasConsolidadas) {
                log.info("Desfazendo pagamento detalhe despesa consolidada request - {}", detalhe);

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

    public DetalheDespesasMensaisDAO parserToDetalheDespesasConsolidadas(DetalheDespesasMensaisRequest request, DetalheDespesasMensaisDAO despesa) {
        // Parser realizado para gravar as despesas parceladas amortizadas com a atualização do request

        despesa.setIdDespesaLinkRelatorio(request.getIdDespesaLinkRelatorio());
        despesa.setTpStatus(request.getTpStatus());
        despesa.setVlTotalPago(request.getVlTotalPago());
        despesa.setTpAnotacao(request.getTpAnotacao());
        despesa.setTpMeta(request.getTpMeta());
        despesa.setTpCategoriaDespesa(request.getTpCategoriaDespesa());
        despesa.setTpReprocessar(request.getTpReprocessar());
        despesa.setTpRelatorio(request.getTpRelatorio());
        despesa.setDsObservacao(request.getDsObservacao());
        despesa.setDsObservacao2(request.getDsObservacao2());
        despesa.setTpParcelaAdiada(request.getTpParcelaAdiada());
        despesa.setTpParcelaAmortizada(request.getTpParcelaAmortizada());

        return despesa;
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
        var detalheDespesaMensal = this.obterDetalheDespesaMensal(idDespesa, idDetalheDespesa, idFuncionario, "default", true);
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

    public TituloDespesaResponse getNomeDespesasMensaisParaAssociacao(Integer idDespesa, Integer idFuncionario, Integer anoReferencia) {
        List<TituloDespesa> listaDespesas = repository.getNomeDespesaMensalParaAssociacao(idDespesa, idFuncionario, anoReferencia);

        List<String> listTituloDespesa = new ArrayList<>();
        for (TituloDespesa despesas : listaDespesas) {
            listTituloDespesa.add(despesas.getTituloDespesa());
        }

        log.info("ListaDespesasMensais: {}", listTituloDespesa);
        return TituloDespesaResponse.builder()
                .despesas(listaDespesas)
                .sizeTituloDespesaVB(listaDespesas.size())
                .tituloDespesa(listTituloDespesa)
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

    public static Integer isDetalheDespesaTipoRelatorio(Integer idOrdem) {
        if (idOrdem.equals(998) || idOrdem.equals(999)) {
            throw new ErroNegocioException("Não é permitido alterar a ordem de uma despesa vinculada ao relatório.");
        }

        return idOrdem;
    }

    public void isExcluirDetalheDespesaTipoRelatorio(Integer idOrdem) {
        if (idOrdem.equals(998) || idOrdem.equals(999)) {
            throw new ErroNegocioException("Não é permitido excluir esta despesa neste relatório, desassocie o item na despesa de referencia.");
        }
    }

    private String removeNBSP_html(String valor) {
        return valor.replace("\u00a0", "");
    }
}
