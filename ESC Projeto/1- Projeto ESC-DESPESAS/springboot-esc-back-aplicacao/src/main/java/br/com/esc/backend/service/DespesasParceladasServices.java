package br.com.esc.backend.service;

import br.com.esc.backend.domain.*;
import br.com.esc.backend.exception.ErroNegocioException;
import br.com.esc.backend.repository.AplicacaoRepository;
import br.com.esc.backend.utils.DataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static br.com.esc.backend.utils.DataUtils.*;
import static br.com.esc.backend.utils.MotorCalculoUtils.*;
import static br.com.esc.backend.utils.ObjectUtils.isEmpty;
import static br.com.esc.backend.utils.ObjectUtils.isNull;
import static br.com.esc.backend.utils.VariaveisGlobais.*;
import static java.lang.Integer.parseInt;

@Service
@RequiredArgsConstructor
@Slf4j
public class DespesasParceladasServices {

    private final AplicacaoRepository repository;
    private NumberFormat formatter = new DecimalFormat("000");

    public DetalheDespesasParceladasResponse obterDespesaParceladaPorFiltros(Integer idDespesa, String nomeDespesaParcelada, Integer idFuncionario, Boolean isPendentes) {
        var despesa = repository.getDespesaParcelada(idDespesa, nomeDespesaParcelada, idFuncionario);
        var parcelas = repository.getParcelasPorFiltro(despesa.getIdDespesaParcelada(), null, null, idFuncionario, opcaoVisualizacaoParcelas(isPendentes, despesa.getIdDespesaParcelada()));
        var idDespesaParcelada = despesa.getIdDespesaParcelada();
        var valorDespesa = repository.getValorTotalDespesaParcelada(idDespesaParcelada, idFuncionario);
        var qtdeParcelas = parcelas.size();
        var nomeDespesaVinculada = (isEmpty(repository.getDespesaParceladaVinculada(idDespesaParcelada, idFuncionario))
                ? "Despesa disponivel para importação*" :
                DESPESA_VINCULADA_A.concat(repository.getDespesaParceladaVinculada(idDespesaParcelada, idFuncionario)));
        var parcelaAtual = repository.getParcelaAtual(idDespesaParcelada, idFuncionario);

        return DetalheDespesasParceladasResponse.builder()
                .idDespesaParcelada(idDespesaParcelada)
                .qtdeParcelas(qtdeParcelas)
                .qtdeParcelasPagas(repository.getQuantidadeParcelasPagas(idDespesaParcelada, idFuncionario))
                .parcelaAtual(isEmpty(parcelaAtual) ? "000/" + qtdeParcelas : parcelaAtual.concat("/") + qtdeParcelas)
                .valorParcelaAtual(this.obterValorDespesa(idDespesaParcelada, 0, MesAnoAtual(), idFuncionario).getVlDespesaParcelada())
                .valorTotalDespesa(valorDespesa)
                .valorTotalDespesaPaga(repository.getValorTotalDespesaParceladaPaga(idDespesaParcelada, idFuncionario))
                .valorTotalDespesaPendente(repository.getValorTotalDespesaParceladaPendente(idDespesaParcelada, idFuncionario))
                .isDespesaComParcelaAmortizada(repository.getValidaDespesaParceladaAmortizacao(idDespesaParcelada, idFuncionario))
                .isDespesaComParcelaAdiantada(repository.getValidaParcelaAdiada(idDespesaParcelada, null, idFuncionario))
                .despesaVinculada(nomeDespesaVinculada)
                .despesas(despesa)
                .parcelas(parcelas)
                .build();
    }

    public void isDespesaParceladaExcluida(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idFuncionario) throws Exception {
        if (isNull(idOrdem) || idOrdem == -1) {
            /*Regra especifica para exclusao de todas as despesas parceladas da despesa com idOrdem = -1 ou Nulo*/
            for (DetalheDespesasMensaisDAO detalhe : repository.getDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario, "a.id_Ordem")) {
                if (detalhe.getTpLinhaSeparacao().equalsIgnoreCase("N") && detalhe.getIdDespesaParcelada() > 0) {
                    this.validaStatusDespesaParcelada(detalhe.getIdDespesa(), detalhe.getIdDetalheDespesa(), detalhe.getIdDespesaParcelada(), detalhe.getIdParcela(), detalhe.getIdFuncionario(), detalhe.getTpStatus(), true);
                }
            }
            return;
        }

        var filtro = DetalheDespesasMensaisDAO.builder()
                .idDespesa(idDespesa)
                .idDetalheDespesa(idDetalheDespesa)
                .idFuncionario(idFuncionario)
                .idOrdem(idOrdem)
                .build();

        var detalhe = repository.getDetalheDespesaMensalPorFiltro(filtro);
        if (!isEmpty(detalhe) && detalhe.getTpLinhaSeparacao().equalsIgnoreCase("N") && detalhe.getIdDespesaParcelada() > 0) {
            this.validaStatusDespesaParcelada(detalhe.getIdDespesa(), detalhe.getIdDetalheDespesa(), detalhe.getIdDespesaParcelada(), detalhe.getIdParcela(), detalhe.getIdFuncionario(), detalhe.getTpStatus(), true);
        }
    }

    public void validaStatusDespesaParcelada(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario, String statusParcela, Boolean bExcluirDespesa) throws Exception {
        if (isNull(idDespesa) || idDespesa.equals(0) || idParcela.equals(0)) {
            return;
        }

        var isParcelaComAmortizacao = repository.getValidaParcelaAmortizacao(idDespesaParcelada, idParcela, idFuncionario);
        var isParcelaAdiada = repository.getValidaParcelaAdiada(idDespesaParcelada, idParcela, idFuncionario);

        if (bExcluirDespesa.equals(true)) {
            if (isParcelaAdiada.equalsIgnoreCase("S")) {
                //Nao altera status de despesas adiadas, somente se for realizado o fluxo para desfazer o adiamento
                return;
            }

            if (isParcelaComAmortizacao.equalsIgnoreCase("S")) {
                repository.updateStatusParcelaSemAmortizacao(idDespesaParcelada, idParcela, idFuncionario);
            }
            statusParcela = PENDENTE;
        }

        if (statusParcela.equalsIgnoreCase(PENDENTE)) {
            repository.updateParcelaStatusPendente(idDespesa, idDetalheDespesa, idDespesaParcelada, idParcela, idFuncionario);
        } else {
            if (isParcelaComAmortizacao.equalsIgnoreCase("S")) {
                repository.updateParcelaStatusPago(STATUS_BAIXA_AMORTIZADA_PELO_SISTEMA.concat(" #" + idParcela + "#"), idDespesa, idDetalheDespesa, idDespesaParcelada, idParcela, idFuncionario);
            } else {
                repository.updateParcelaStatusPago(STATUS_BAIXA_REALIZADA_PELO_SISTEMA.concat(" #" + idParcela + "#"), idDespesa, idDetalheDespesa, idDespesaParcelada, idParcela, idFuncionario);
            }
        }

        validarBaixaCadastroDespesaParcelada(idDespesaParcelada, idFuncionario);
    }

    public ExplodirFluxoParcelasResponse gerarFluxoParcelas(Integer idDespesaParcelada, String valorParcela, Integer qtdeParcelas, String dataReferencia, Integer idFuncionario) throws ParseException {
        boolean bNovoProcessamento = true;
        List<ParcelasDAO> listParcelas = new ArrayList<>();

        //Valida se existe parcelas gravadas, caso sim, trata a logica como reprocessamento
        Integer iParcelasAtual = repository.getQuantidadeParcelas(idDespesaParcelada, idFuncionario);
        if (iParcelasAtual.compareTo(0) == 1) {
            bNovoProcessamento = false;
            if (qtdeParcelas.compareTo(iParcelasAtual) <= 0) {
                throw new ErroNegocioException("Não é permitido uma quantidade de parcelas inferior ou igual a " + iParcelasAtual + " Parcela(s).");
            }
        }

        var dataVencimento = tratarDataPrimeiroVencimento(dataReferencia);
        var iParcelas = (bNovoProcessamento ? 1 : (iParcelasAtual + 1));
        for (int i = iParcelas; i <= qtdeParcelas; i++) {
            var parcela = ParcelasDAO.builder()
                    .idDespesaParcelada(idDespesaParcelada)
                    .idDespesa(0)
                    .idDetalheDespesa(0)
                    .idParcela(i)
                    .idFuncionario(idFuncionario)
                    .nrParcela(formatter.format(i))
                    .vlParcela(valorParcela)
                    .vlDesconto(VALOR_ZERO)
                    .dsDataVencimento(this.mesAnoParcela(dataVencimento, i))
                    .dsObservacoes("")
                    .tpBaixado("N")
                    .tpQuitado("N")
                    .tpParcelaAdiada("N")
                    .tpParcelaAmortizada("N")
                    .build();

            log.info("Parcela gerada >>> {}", parcela);
            listParcelas.add(parcela);
        }

        return ExplodirFluxoParcelasResponse.builder()
                .parcelas(listParcelas)
                .build();
    }

    public void adiarFluxoParcelas(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario) throws Exception {
        var isValidaDespesaAdiada = repository.getValidaDetalheDespesaParceladaAdiada(idDespesa, idDetalheDespesa, idDespesaParcelada, idFuncionario);

        if (isValidaDespesaAdiada.equalsIgnoreCase("N")) {
            var parcelaAtual = repository.getParcelasPorFiltro(idDespesaParcelada, idParcela, null, idFuncionario, opcaoVisualizacaoParcelas(false, idDespesaParcelada)).get(0);
            if (parcelaAtual.getTpBaixado().equalsIgnoreCase("S")) {
                log.error("Não é possivel adiar uma parcela que ja foi paga anteriormente...");
                return;
            }

            var parcela = repository.getUltimaParcelaDespesaParcelada(idDespesaParcelada, idFuncionario);
            var novaParcelaRequest = this.parserToNovaParcelaAdiadas(parcela);

            if (convertStringToDecimal(novaParcelaRequest.getVlParcela()).compareTo(convertStringToDecimal(parcelaAtual.getVlParcela())) != 0) {
                novaParcelaRequest.setVlParcela(parcelaAtual.getVlParcela());
            }

            /*Grava a nova parcela adiada*/
            log.info("Gravando nova parcela adiada >>> " + novaParcelaRequest);
            repository.insertParcela(novaParcelaRequest);

            /*Baixa a parcela atual e altera o stts para adiada*/
            var observacoesBaixa = PARCELA_ADIADA_DESTE_MES_PARA.concat(novaParcelaRequest.getDsDataVencimento());
            repository.updateParcelaStatusAdiada(idDespesa, idDetalheDespesa, observacoesBaixa, idParcela, idDespesaParcelada, idFuncionario);

            /*Altera a flag de ParcelaAdiada no detalhe das despesas mensais e baixa o pagamento e marca como despesa de anotacao*/
            var logProcessamento = "Operacao realizada em: " + DataHoraAtual() + " - Usuario: ** " + repository.getUsuarioLogado(idFuncionario);
            repository.updateDetalheDespesasMensaisParcelaAdiada(idDespesa, idDetalheDespesa, idDespesaParcelada, observacoesBaixa, logProcessamento, novaParcelaRequest.getVlParcela(), idFuncionario);

            /*Atualiza a contagem de parcelas adiadas*/
            repository.updateQuantidadeParcelasAdiadas(idDespesaParcelada, idFuncionario);
        } else {
            var descricaoDespesa = repository.getDespesaParcelada(idDespesaParcelada, null, idFuncionario).getDsTituloDespesaParcelada();
            throw new ErroNegocioException("A parcela da despesa " + descricaoDespesa + " ja foi adiada neste mês. Não é permitido adiar a mesma parcela 2X no mesmo mês.");
        }
    }

    public void desfazerFluxoParcelasAdiadas(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario) throws Exception {
        var isParcelaAdiada = repository.getValidaParcelaAdiada(idDespesaParcelada, idParcela, idFuncionario);

        if (isParcelaAdiada.equalsIgnoreCase("S")) {
            var parcela = repository.getUltimaParcelaDespesaParcelada(idDespesaParcelada, idFuncionario);
            var valorParcelaAdiada = repository.getParcelaPorDataVencimento(parcela.getIdDespesaParcelada(), parcela.getDsDataVencimento(), parcela.getIdFuncionario()).getVlParcela();

            /*Exclui a parcela adicionada no final do fluxo*/
            log.info("Excluindo ultima parcela adiada >>> " + parcela);
            repository.deleteParcela(parcela.getIdDespesaParcelada(), parcela.getIdParcela(), parcela.getIdFuncionario());

            /*Exclui a parcela adiada do detalhe despesas mensais (caso tenha sido importada)*/
            repository.deleteParcelaDetalheDespesasMensais(parcela.getIdDespesaParcelada(), parcela.getIdParcela(), idFuncionario);

            /*Desfaz a baixa da parcela com amortizacao e volta o stts para PENDENTE*/
            repository.updateParcelaStatusPendenteParcelaAdiada(idDespesa, idDetalheDespesa, idDespesaParcelada, idParcela, valorParcelaAdiada, idFuncionario);

            /*Altera a flag de ParcelaAdiada para N no detalhe das despesas mensais e desfaz o pagamento e a despesa de anotacao*/
            repository.updateDetalheDespesasMensaisDesfazerAdiamento(idDespesa, idDetalheDespesa, idDespesaParcelada, idParcela, valorParcelaAdiada, idFuncionario);

            /*Atualiza a contagem de parcelas adiantadas*/
            repository.updateQuantidadeParcelasDesfazerAdiamento(idDespesaParcelada, idFuncionario);
        } else {
            var descricaoDespesa = repository.getDespesaParcelada(idDespesaParcelada, null, idFuncionario).getDsTituloDespesaParcelada();
            throw new ErroNegocioException("A parcela da despesa " + descricaoDespesa + " não pode ser processada. \n\nMotivo: Esta parcela não foi adiada, verifique qual parcela foi adiada e tente novamente.");
        }
    }

    public DespesasParceladasResponse getDespesasParceladas(Integer idFuncionario, String status) {
        var listDespesasDAO = repository.getDespesasParceladas(idFuncionario, status.equalsIgnoreCase("default") ? "tp_Baixado = 'N'" : "tp_Baixado = 'S'");

        return DespesasParceladasResponse.builder()
                .despesas(listDespesasDAO)
                .sizeDespesasParceladasVB(listDespesasDAO.size())
                .build();
    }

    public StringResponse getNomeDespesaParceladaPorFiltro(Integer idDespesaParcelada, Integer idFuncionario) {
        var nomeDespesa = repository.getNomeDespesaParceladaPorFiltro(idDespesaParcelada, idFuncionario);

        log.info("NomeDespesaResponse: {}", nomeDespesa);
        return StringResponse.builder()
                .nomeDespesaParcelada(nomeDespesa)
                .build();
    }

    public TituloDespesaResponse getNomeDespesasParceladasParaImportacao(Integer idFuncionario, String tipo) {
        List<TituloDespesa> listaDespesas = (tipo.equalsIgnoreCase("ativas") ? repository.getNomeDespesasParceladasParaImportacao(idFuncionario)
                : repository.getNomeDespesasParceladas(idFuncionario));

        List<TituloConsolidacao> listaConsolidacoes = (tipo.equalsIgnoreCase("ativas") ? repository.getNomeConsolidacoesParaImportacao(idFuncionario) :
                repository.getNomeConsolidacoes(idFuncionario));

        for (TituloConsolidacao consolidacao : listaConsolidacoes) {
            var tituloDespesa = TituloDespesa.builder()
                    .idDespesa(-consolidacao.getIdConsolidacao()) // para consolidacao foi necessario adicionar o - para tratar no frontend
                    .idConsolidacao(consolidacao.getIdConsolidacao())
                    .tituloDespesa(consolidacao.getTituloConsolidacao())
                    .build();

            listaDespesas.add(tituloDespesa);
        }

        List<String> listTituloDespesa = new ArrayList<>();
        for (TituloDespesa despesas : listaDespesas) {
            listTituloDespesa.add(despesas.getTituloDespesa());
        }

        log.info("ListaNomesDespesasParceladas e Consolidacoes: {}", listaDespesas);
        return TituloDespesaResponse.builder()
                .despesas(listaDespesas)
                .sizeTituloDespesaVB(listaDespesas.size())
                .tituloDespesa(listTituloDespesa)
                .build();
    }

    public StringResponse validarTituloDespesaParceladaExistente(String dsTituloDespesaParcelada, Integer idDespesaParcelada, Integer idFuncionario) {
        var response = repository.getValidaTituloDespesaParceladaExistente(dsTituloDespesaParcelada, idDespesaParcelada, idFuncionario).compareTo(0) != 0;
        return StringResponse.builder()
                .isTituloJaExistente(response)
                .build();
    }

    public void quitarTotalmenteDespesaParcelada(Integer idDespesaParcelada, Integer idFuncionario, String valorQuitacao) {
        var bDespesaPagaComOuSemDesconto = true;
        BigDecimal valorTotalDespesa = convertStringToDecimal(repository.getValorTotalDespesaParceladaPendente(idDespesaParcelada, idFuncionario));
        BigDecimal valorDesconto = valorTotalDespesa.subtract(convertStringToDecimal(valorQuitacao));

        var observacoes = "Baixa realizada # Pago: " + valorQuitacao + " - Desc: " + convertDecimalToString(valorDesconto) + " #- Quitação TOTAL :)";

        for (ParcelasDAO parcela : repository.getParcelasPorFiltro(idDespesaParcelada, null, "N", idFuncionario, opcaoVisualizacaoParcelas(false, idDespesaParcelada))) {
            var idParcela = parcela.getIdParcela();

            if (bDespesaPagaComOuSemDesconto) {
                log.info("Buscando despesa parcelada importada atual (Em Aberto) para realizar a baixa do pagamento...");
                DetalheDespesasMensaisDAO filtro = DetalheDespesasMensaisDAO.builder()
                        .idDespesaParcelada(idDespesaParcelada)
                        .idParcela(idParcela)
                        .idFuncionario(idFuncionario)
                        .build();

                var detalheDespesaMensalParcela = repository.getDetalheDespesaMensalPorFiltro(filtro);
                repository.updateStatusPagamentoDetalheDespesa(valorQuitacao, valorQuitacao, PAGO, observacoes, "Baixa Automatica - Quitacao TOTAL - ".concat(DataUtils.DataHoraAtual()), detalheDespesaMensalParcela.getIdDespesa(), detalheDespesaMensalParcela.getIdDetalheDespesa(), detalheDespesaMensalParcela.getIdOrdem(), idFuncionario);

                log.info("Baixando pagamento da parcela.. >> {}", parcela);
                repository.updateParcelaStatusQuitado(observacoes, idDespesaParcelada, idParcela, valorQuitacao, idFuncionario);

                bDespesaPagaComOuSemDesconto = false;
                continue;
            }

            observacoes = STATUS_BAIXA_REALIZADA_PELO_SISTEMA.concat(" #" + idParcela + "#");

            log.info("Baixando pagamento da parcela.. >> {}", parcela);
            repository.updateParcelaStatusQuitado(observacoes, idDespesaParcelada, idParcela, VALOR_ZERO, idFuncionario);

            log.info("Excluindo despesa parcelada importada das demais despesas mensais...");
            repository.deleteParcelaDetalheDespesasMensais(idDespesaParcelada, idParcela, idFuncionario);
        }

        validarBaixaCadastroDespesaParcelada(idDespesaParcelada, idFuncionario);
    }

    public StringResponse obterValorDespesa(Integer idDespesaParcelada, Integer idParcela, String mesAnoReferencia, Integer idFuncionario) {
        var valorDespesa = VALOR_ZERO;

        if (idParcela == 0 && isEmpty(mesAnoReferencia)) {
            valorDespesa = repository.getDespesaParcelada(idDespesaParcelada, null, idFuncionario).getVlFatura();

            return StringResponse.builder()
                    .vlDespesaParcelada(valorDespesa)
                    .build();
        }

        var sWhere = "";
        if (idParcela > 0 && isEmpty(mesAnoReferencia)) {
            sWhere = "id_DespesaParcelada = " + idDespesaParcelada + " AND id_Parcelas = " + idParcela + " AND id_Funcionario = " + idFuncionario;
        } else if (idParcela == 0 && !isEmpty(mesAnoReferencia)) {
            sWhere = "id_DespesaParcelada = " + idDespesaParcelada + " AND id_Funcionario = " + idFuncionario + " AND ds_DataVencimento = '" + mesAnoReferencia + "' AND tp_ParcelaAdiada = 'N'";
        }
        valorDespesa = repository.getValorParcelaPorFiltro(sWhere);

        if (isEmpty(valorDespesa)) {
            valorDespesa = repository.getMaxValorParcela(idDespesaParcelada, idFuncionario);
        }

        return StringResponse.builder()
                .vlDespesaParcelada(valorDespesa)
                .build();
    }

    public void excluirParcela(Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario) {
        repository.deleteParcela(idDespesaParcelada, idParcela, idFuncionario);
        repository.deleteParcelaDetalheDespesasMensais(idDespesaParcelada, idParcela, idFuncionario);
    }

    public void excluirDespesaParcelada(Integer idDespesaParcelada, Integer idFuncionario) {
        repository.deleteDespesasParceladas(idDespesaParcelada, idFuncionario);
        repository.deleteTodasParcelas(idDespesaParcelada, idFuncionario);
        repository.deleteTodosDetalheDespesaParcelada(idDespesaParcelada, idFuncionario);
    }

    public void gravarDespesaParcelada(DespesaParceladaDAO despesa) {
        var idDespesaExistente = repository.getDespesaParcelada(despesa.getIdDespesaParcelada(), null, despesa.getIdFuncionario());

        if (isEmpty(idDespesaExistente)) {
            log.info("Gravando Nova Despesa Parcelada >> Request: {}", despesa);
            despesa.setDtCadastro(DataHoraAtual());
            repository.insertDespesaParcelada(despesa);
        } else {
            log.info("Atualizando Despesa Parcelada >> Request: {}", despesa);
            repository.updateDespesaParcelada(despesa);
        }

        Integer iQtdeParcelasAtual = repository.getQuantidadeParcelas(despesa.getIdDespesaParcelada(), despesa.getIdFuncionario());
        if (iQtdeParcelasAtual > 0 && despesa.getNrTotalParcelas().compareTo(iQtdeParcelasAtual) == 1) {
            //Atualiza o valor das parcelas existentes antes de gravar a(s) nova(s) parcela(s) reprocessada(s)
            log.info(">>Reprocessamento de Parcelas - atualizando Valor das Parcelas existentes.");
            repository.updateParcelasReprocessamento(despesa.getVlParcela(), despesa.getIdDespesaParcelada(), despesa.getIdFuncionario());
        }
    }

    public void gravarParcela(ParcelasDAO parcela) {
        var listParcelas = repository.getParcelasPorFiltro(parcela.getIdDespesaParcelada(), parcela.getIdParcela(), null, parcela.getIdFuncionario(), opcaoVisualizacaoParcelas(false, parcela.getIdDespesaParcelada()));

        if (isEmpty(listParcelas)) {
            log.info("Gravando Nova Parcela >> Request: {}", parcela);
            repository.insertParcela(parcela);
        } else {
            var valorAtualParcela = convertStringToDecimal(listParcelas.get(0).getVlParcela());

            //Só altera o valor da parcela se ela estiver com o stts em aberto.
            if (valorAtualParcela.compareTo(convertStringToDecimal(parcela.getVlParcela())) != 0) {
                if (parcela.getTpBaixado().equals("N")) {
                    BigDecimal calculo = convertStringToDecimal(parcela.getVlParcela()).subtract(valorAtualParcela);
                    parcela.setVlDesconto(convertDecimalToStringComSinal(calculo));

                    repository.updateValorTotalDetalheDespesasMensaisParcelas(parcela.getVlParcela(), parcela.getIdDespesaParcelada(), parcela.getIdParcela(), parcela.getIdFuncionario(), PENDENTE);
                } else {
                    log.info("Não foi possivel alterar o valor da parcela {} porque ela ja foi paga.", parcela.getIdParcela());
                    parcela.setVlParcela(convertDecimalToString(valorAtualParcela));
                }
            }

            parcela = validarCheckAmortizacao(parcela);

            log.info("Atualizando Parcela >> Request: {}", parcela);
            repository.updateParcela(parcela);
        }
    }

    public StringResponse obterRelatorioDespesasParceladasQuitacao(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario) {
        var relatorioDAO = repository.getRelatorioDespesasParceladasQuitacao(idDespesa, idDetalheDespesa, idFuncionario);
        StringBuffer buffer = new StringBuffer();

        for (RelatorioDespesasParceladasQuitacaoDAO dao : relatorioDAO) {
            StringBuilder builder = new StringBuilder();
            builder.append(dao.getValorDespesa()).append("  -  ").append(dao.getDsTituloDespesaParcelada());
            builder.append(System.lineSeparator());

            buffer.append(builder);
        }

        return StringResponse.builder()
                .relatorioDespesas(buffer.toString())
                .build();
    }

    private ParcelasDAO parserToNovaParcelaAdiadas(ParcelasDAO parcela) throws ParseException {
        NumberFormat formatter = new DecimalFormat("000");

        Integer idParcelaNova = (parcela.getIdParcela() + 1);
        String nrParcelaNova = formatter.format(parseInt(parcela.getNrParcela()) + 1);
        String dataVencimentoUltimaParcela = repository.getUltimaParcelaDespesaParcelada(parcela.getIdDespesaParcelada(), parcela.getIdFuncionario()).getDsDataVencimento();
        String dataVencimentoNova = convertDateToString(retornaDataPersonalizada("01/" + parcela.getDsDataVencimento(), 1)).substring(3, 10);

        parcela.setIdParcela(idParcelaNova);
        parcela.setNrParcela(nrParcelaNova);
        parcela.setDsDataVencimento(dataVencimentoNova);
        parcela.setVlParcela(repository.getParcelaPorDataVencimento(parcela.getIdDespesaParcelada(), dataVencimentoUltimaParcela, parcela.getIdFuncionario()).getVlParcela());
        parcela.setVlDesconto("-");
        parcela.setDsObservacoes("");
        parcela.setTpParcelaAdiada("N");
        parcela.setTpParcelaAmortizada("N");
        parcela.setTpBaixado("N");
        parcela.setTpQuitado("N");
        parcela.setIdDetalheDespesa(0);
        parcela.setIdDespesa(0);

        return parcela;
    }

    public List<ParcelasDAO> obterParcelasParaAmortizacao(Integer idDespesaParcelada, Integer idFuncionario) {
        return repository.getParcelasParaAmortizacao(idDespesaParcelada, idFuncionario);
    }

    public void validarBaixaCadastroDespesaParcelada(Integer idDespesaParcelada, Integer idFuncionario) {
        var qtdeParcelas = repository.getQuantidadeParcelasEmAberto(idDespesaParcelada, idFuncionario);

        if (qtdeParcelas <= 0) {
            repository.updateDespesasParceladasEncerrado(idDespesaParcelada, idFuncionario);
        }

        repository.updateDespesasParceladasEmAberto(idFuncionario);
    }

    private String tratarDataPrimeiroVencimento(String dataReferencia) {
        return "01/".concat(dataReferencia).replace("-", "/");
    }

    private String mesAnoParcela(String dataReferencia, Integer qtdeMeses) throws ParseException {
        return convertDateToString_MMYYYY(retornaDataPersonalizada(dataReferencia, qtdeMeses - 1));
    }

    private ParcelasDAO validarCheckAmortizacao(ParcelasDAO parcela) {
        ParcelasDAO parcelaEditada = parcela;

        /*Caso seja alterada a check de amortizacao pelo front, altera a observacao da parcela*/
        if (parcela.getTpParcelaAmortizada().equalsIgnoreCase("S") && parcela.getDsObservacoes().contains(STATUS_BAIXA_REALIZADA_PELO_SISTEMA)) {
            parcelaEditada.setDsObservacoes(parcela.getDsObservacoes().replace("realizada", "<AMORTIZADA>"));
        } else if (parcela.getTpParcelaAmortizada().equalsIgnoreCase("N") && parcela.getDsObservacoes().contains(STATUS_BAIXA_AMORTIZADA_PELO_SISTEMA)) {
            parcelaEditada.setDsObservacoes(parcela.getDsObservacoes().replace("<AMORTIZADA>", "realizada"));
        }

        return parcelaEditada;
    }

    public static String opcaoVisualizacaoParcelas(Boolean isPendentes, Integer idDespesaParcelada) {
        if (isEmpty(isPendentes) || isPendentes.equals(Boolean.FALSE)) {
            return "AND id_Parcelas IS NOT NULL";
        } else {
            return "AND id_Parcelas >= (SELECT MAX(id_Parcelas) FROM tbd_Parcelas WHERE id_DespesaParcelada = " + idDespesaParcelada + "and tp_Baixado = 'S')";
        }
    }
}
