package br.com.esc.backend.service;

import br.com.esc.backend.domain.*;
import br.com.esc.backend.repository.AplicacaoRepository;
import br.com.esc.backend.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static br.com.esc.backend.utils.GlobalUtils.getAnoAtual;
import static br.com.esc.backend.utils.GlobalUtils.getMesAtual;
import static br.com.esc.backend.utils.MotorCalculoUtils.*;
import static br.com.esc.backend.utils.ObjectUtils.*;
import static br.com.esc.backend.utils.VariaveisGlobais.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DetalheDespesasServices {

    private final AplicacaoRepository repository;
    private final DespesasParceladasServices despesasParceladasServices;

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

        var detalheDespesasMensaisList = repository.getDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario, parserOrdem(ordem));
        return DetalheDespesasMensaisDTO.builder()
                .sizeDetalheDespesaMensalVB(detalheDespesasMensaisList.size())
                .despesaMensal(despesaMensal.get(0))
                .detalheDespesaMensal(detalheDespesasMensaisList)
                .build();
    }

    public void gravarDetalheDespesasMensais(DetalheDespesasMensaisDAO detalheDAO, boolean isParcelaAmortizacao) throws Exception {
        var bIsParcelaAdiada = false;

        if (detalheDAO.getTpRelatorio().equalsIgnoreCase("S")) {
            // Despesas do tipo relatorio nao permite a gravacao\atualizacao na base de dados
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
            bIsParcelaAdiada = parcela.getTpParcelaAdiada().equalsIgnoreCase("S") ? true : false;
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
            repository.insertDetalheDespesasMensais(detalheDAO);

            if (bIsParcelaAdiada) {
                var valorParcelaAdiantada = repository.getMaxValorParcela(detalheDAO.getIdDespesaParcelada(), detalheDAO.getIdFuncionario());
                repository.updateDetalheDespesasMensaisParcelaAdiada(detalheDAO.getIdDespesa(), detalheDAO.getIdDetalheDespesa(), detalheDAO.getIdDespesaParcelada(), "Despesa parcelada adiantada no fluxo de parcelas.", "", valorParcelaAdiantada, detalheDAO.getIdFuncionario());
            }

            this.ordenarListaDetalheDespesasMensais(detalheDAO.getIdDespesa(), detalheDAO.getIdDetalheDespesa(), detalheDAO.getIdFuncionario(), "prazo");
        }
    }

    public void ordenarListaDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String ordem) {
        Integer iOrdemNova = 1;
        for (DetalheDespesasMensaisDAO detalheDespesas : repository.getDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario, parserOrdem(ordem))) {
            log.info("Ordenando registros listaDetalheDespesas: idDespesa = {}, idDetalheDespesa = {}, idOrdemAntiga = {}, idOrdemNova = {}", detalheDespesas.getIdDespesa(), detalheDespesas.getIdDetalheDespesa(), detalheDespesas.getIdOrdem(), iOrdemNova);
            repository.updateDetalheDespesasMensaisOrdenacao(detalheDespesas.getIdDespesa(), detalheDespesas.getIdDetalheDespesa(), detalheDespesas.getIdDespesaParcelada(), detalheDespesas.getIdOrdem(), iOrdemNova, detalheDespesas.getIdFuncionario());
            iOrdemNova++;
        }
    }

    public void gravarDespesasMensais(DespesasMensaisDAO mensaisDAO) {
        if (mensaisDAO.getTpLinhaSeparacao().equalsIgnoreCase("N")) {
            if (mensaisDAO.getTpEmprestimo().equalsIgnoreCase("S") && mensaisDAO.getIdEmprestimo().intValue() > 0) {
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
        } else {
            repository.deleteDetalheDespesasMensaisPorFiltro(idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
        }
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
                    .dsDescricao(TIPO_RESERVA_AUTOMATICA_DEBITO_CARTAO)
                    .vlTotal(mensaisDAO.getTpAnotacao().equalsIgnoreCase("S") ? "0,00" :
                            convertDecimalToString(repository.getCalculoValorDespesaTipoCartaoDebito(referencia.getIdDespesa(), mensaisDAO.getIdDetalheDespesa(), referencia.getIdFuncionario())))
                    .tpStatus("+")
                    .tpFixasObrigatorias("N")
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
        Boolean isExtratoMesAtual = false;
        var extrato = new ExtratoDespesasDAO();

        if (tipo.equalsIgnoreCase("cadastroParcelas")) {
            extrato = repository.getExtratoDespesasParceladasMes(getMesAtual(), getAnoAtual(), idFuncionario);

            mensagemBuffer.append(NESTE_MES_SERA_QUITADO);
            mensagemBuffer.append(extrato.getVlDespesas() + "R$");
        } else {
            var despesasFixas = repository.getDespesasFixasMensaisPorID(idDespesa, idFuncionario);
            if (despesasFixas.size() == 0) {
                extrato.setMensagem(SEM_DESPESA_MENSAGEM_PADRAO);
                return extrato;
            }

            /*Regra para validar se o mes de pesquisa é o mes atual e tratar a mensagem*/
            if (despesasFixas.get(0).getDsMes().equalsIgnoreCase(getMesAtual()) && despesasFixas.get(0).getDsAno().equalsIgnoreCase(getAnoAtual())) {
                isExtratoMesAtual = true;
            }

            if (tipo.equalsIgnoreCase("lancamentosMensais")) {
                var qtdeDespesasParceladasMes = repository.getQuantidadeDespesasParceladasMes(idDespesa, idFuncionario);
                var qtdeDespesasQuitacaoMes = repository.getQuantidadeDespesasParceladasQuitacaoMes(idDespesa, idFuncionario);

                extrato.setQtDespesas(qtdeDespesasQuitacaoMes.getQtdeParcelas());
                extrato.setVlDespesas(convertDecimalToString(qtdeDespesasQuitacaoMes.getVlParcelas()));

                mensagemBuffer.append(isExtratoMesAtual ? NESTE_MES_SERA_QUITADO : NESTE_MES_FOI_QUITADO);
                mensagemBuffer.append(qtdeDespesasQuitacaoMes.getQtdeParcelas() + "/" + qtdeDespesasParceladasMes);
                mensagemBuffer.append(" Despesas Parceladas, Totalizando: " + convertToMoedaBR(qtdeDespesasQuitacaoMes.getVlParcelas()) + "R$");
                /*mensagemBuffer.append("                                   ");
                mensagemBuffer.append("[ Despesa (2022): ").append(convertDecimalToString(repository.getCalculoReceitaNegativaMES((idDespesa - 12), idFuncionario))).append("R$ ]");*/

                extrato.setMensagem(mensagemBuffer.toString());

            } else if (tipo.equalsIgnoreCase("detalheDespesas")) {
                extrato = repository.getExtratoDespesasMes(idDespesa, idDetalheDespesa, idFuncionario);

                var qtdeTotalDespParceladasMes = repository.getQuantidadeDetalheDespesasParceladasMes(idDespesa, idDetalheDespesa, idFuncionario);

                mensagemBuffer.append(isExtratoMesAtual ? NESTE_MES_SERA_QUITADO : NESTE_MES_FOI_QUITADO);
                mensagemBuffer.append(extrato.getQtDespesas() + "/" + qtdeTotalDespParceladasMes);
                mensagemBuffer.append(" Despesas Parceladas, Totalizando: " + extrato.getVlDespesas() + "R$");
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

        var mensaisDAO = DespesasMensaisDAO.builder()
                .idDespesa(idDespesa)
                .idDetalheDespesa(idDetalheDespesa)
                .idFuncionario(idFuncionario)
                .tpLinhaSeparacao(ObjectUtils.isEmpty(detalheDespesaMensal.getDespesaMensal().getTpLinhaSeparacao()) ? null : detalheDespesaMensal.getDespesaMensal().getTpLinhaSeparacao())
                .tpDebitoCartao(detalheDespesaMensal.getDespesaMensal().getTpDebitoCartao())
                .tpAnotacao(ObjectUtils.isEmpty(detalheDespesaMensal.getDespesaMensal().getTpAnotacao()) ? "N" : detalheDespesaMensal.getDespesaMensal().getTpAnotacao())
                .build();

        return mensaisDAO;
    }

    private DespesasFixasMensaisDAO obterReferenciaMesProcessamento(Integer idDespesa, Integer idFuncionario) {
        return repository.getDespesaFixaMensalPorFiltro(idDespesa, 1, idFuncionario);
    }


    private String parserOrdem(String ordem) {
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
