package br.com.esc.backend.service;

import br.com.esc.backend.domain.*;
import br.com.esc.backend.exception.ErroNegocioException;
import br.com.esc.backend.repository.AplicacaoRepository;
import br.com.esc.backend.utils.ObjectUtils;
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
import static br.com.esc.backend.utils.MotorCalculoUtils.convertDecimalToString;
import static br.com.esc.backend.utils.MotorCalculoUtils.convertStringToDecimal;
import static br.com.esc.backend.utils.VariaveisGlobais.*;
import static java.lang.Integer.parseInt;

@Service
@RequiredArgsConstructor
@Slf4j
public class DespesasParceladasServices {

    private final AplicacaoRepository repository;
    private NumberFormat formatter = new DecimalFormat("000");

    public DetalheDespesasParceladasResponse obterDespesaParceladaPorFiltros(Integer idDespesa, String nomeDespesaParcelada, Integer idFuncionario) {
        var despesa = repository.getDespesaParcelada(idDespesa, nomeDespesaParcelada, idFuncionario);
        var parcelas = repository.getParcelasPorFiltro(despesa.getIdDespesaParcelada(), null, null, idFuncionario);

        var idDespesaParcelada = despesa.getIdDespesaParcelada();
        var valorDespesa = repository.getValorTotalDespesaParcelada(idDespesaParcelada, idFuncionario);
        var qtdeParcelas = parcelas.size();
        var nomeDespesaVinculada = (ObjectUtils.isEmpty(repository.getDespesaParceladaVinculada(idDespesaParcelada, idFuncionario)) ? "Despesa disponivel para importação*" :
                DESPESA_VINCULADA_A.concat(repository.getDespesaParceladaVinculada(idDespesaParcelada, idFuncionario)));
        var parcelaAtual = repository.getParcelaAtual(idDespesaParcelada, idFuncionario);

        return DetalheDespesasParceladasResponse.builder()
                .idDespesaParcelada(idDespesaParcelada)
                .qtdeParcelas(qtdeParcelas)
                .qtdeParcelasPagas(repository.getQuantidadeParcelasPagas(idDespesaParcelada, idFuncionario))
                .parcelaAtual(ObjectUtils.isEmpty(parcelaAtual) ? "000/" + qtdeParcelas : parcelaAtual.concat("/") + qtdeParcelas)
                .valorParcelaAtual(this.obterValorDespesa(idDespesaParcelada, 0, MesAnoAtual(), idFuncionario).getVlDespesaParcelada())
                .valorTotalDespesa(valorDespesa)
                .valorTotalDespesaPaga(repository.getValorTotalDespesaParceladaPaga(idDespesaParcelada, idFuncionario))
                .valorTotalDespesaPendente(repository.getValorTotalDespesaParceladaPendente(idDespesaParcelada, idFuncionario))
                .isDespesaComParcelaAmortizada(repository.getValidaDespesaParceladaAmortizacao(idDespesaParcelada, idFuncionario))
                .isDespesaComParcelaAdiantada(repository.getValidaParcelaAdiantamento(idDespesaParcelada, null, idFuncionario))
                .despesaVinculada(nomeDespesaVinculada)
                .despesas(despesa)
                .parcelas(parcelas)
                .build();
    }

    public void isDespesaParceladaExcluida(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idFuncionario) throws Exception {
        if (ObjectUtils.isNull(idOrdem) || idOrdem == -1) {
            /*Regra especifica para exclusao de todas as despesas parceladas da despesa com idOrdem = -1 ou Nulo*/
            for (DetalheDespesasMensaisDAO detalhe : repository.getDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario, "a.id_Ordem")) {
                if (detalhe.getTpLinhaSeparacao().equalsIgnoreCase("N") && detalhe.getIdDespesaParcelada().intValue() > 0) {
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
        if (!ObjectUtils.isEmpty(detalhe) && detalhe.getTpLinhaSeparacao().equalsIgnoreCase("N") && detalhe.getIdDespesaParcelada().intValue() > 0) {
            this.validaStatusDespesaParcelada(detalhe.getIdDespesa(), detalhe.getIdDetalheDespesa(), detalhe.getIdDespesaParcelada(), detalhe.getIdParcela(), detalhe.getIdFuncionario(), detalhe.getTpStatus(), true);
        }
    }

    public void validaStatusDespesaParcelada(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario, String statusParcela, Boolean bExcluirDespesa) throws Exception {
        if (ObjectUtils.isNull(idDespesa) || idDespesa.equals(0) || idParcela.equals(0)) {
            return;
        }

        var isParcelaComAmortizacao = repository.getValidaParcelaAmortizacao(idDespesaParcelada, idParcela, idFuncionario);
        var isParcelaComAdiantamento = repository.getValidaParcelaAdiantamento(idDespesaParcelada, idParcela, idFuncionario);

        if (bExcluirDespesa.equals(true)) {
            if (isParcelaComAdiantamento.equalsIgnoreCase("S")) {
                //Nao altera status de despesas com adiantamento, somente se for realizado o fluxo para desfazer o adiantamento
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
        Boolean bNovoProcessamento = true;
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

    public void adiantarFluxoParcelas(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario) throws Exception {
        var isValidaAdiantamentoMes = repository.getValidaDetalheDespesaParceladaAdiantada(idDespesa, idDetalheDespesa, idDespesaParcelada, idFuncionario);

        if (isValidaAdiantamentoMes.equalsIgnoreCase("N")) {
            var parcelaAtual = repository.getParcelasPorFiltro(idDespesaParcelada, idParcela, null, idFuncionario);
            if (parcelaAtual.get(0).getTpBaixado().equalsIgnoreCase("S")) {
                throw new ErroNegocioException("Não é possivel adiantar uma parcela que ja foi paga.");
            }

            var parcela = repository.getUltimaParcelaDespesaParcelada(idDespesaParcelada, idFuncionario);
            var novaParcelaRequest = this.parserToNovaParcelaAdiantamento(parcela);

            /*Grava a nova parcela adiantada*/
            log.info("Gravando nova parcela adiantamento >>> " + novaParcelaRequest);
            repository.insertParcela(novaParcelaRequest);

            /*Baixa a parcela atual e altera o stts para adiantada*/
            var observacoesBaixa = PARCELA_ADIANTADA_DESTE_MES_PARA.concat(novaParcelaRequest.getDsDataVencimento());
            repository.updateParcelaStatusAdiantado(idDespesa, idDetalheDespesa, observacoesBaixa, idParcela, idDespesaParcelada, idFuncionario);

            /*Altera a flag de ParcelaAdiada no detalhe das despesas mensais e baixa o pagamento e marca como despesa de anotacao*/
            var logProcessamento = "Operacao realizada em: " + DataHoraAtual() + " - Usuario: ** " + repository.getUsuarioLogado(idFuncionario);
            repository.updateDetalheDespesasMensaisParcelaAdiada(idDespesa, idDetalheDespesa, idDespesaParcelada, observacoesBaixa, logProcessamento, novaParcelaRequest.getVlParcela(), idFuncionario);

            /*Atualiza a contagem de parcelas adiantadas*/
            repository.updateQuantidadeParcelasAdiantadas(idDespesaParcelada, idFuncionario);
        } else {
            var descricaoDespesa = repository.getDespesaParcelada(idDespesaParcelada, null, idFuncionario).getDsTituloDespesaParcelada();
            throw new ErroNegocioException("A parcela da despesa " + descricaoDespesa + " ja foi adiantada neste mês. Não é permitido adiantar a mesma parcela 2X no mesmo mês.");
        }
    }

    public void desfazerAdiantamentoFluxoParcelas(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario) throws Exception {
        var isParcelaAdiantada = repository.getValidaParcelaAdiantamento(idDespesaParcelada, idParcela, idFuncionario);

        if (isParcelaAdiantada.equalsIgnoreCase("S")) {
            var parcela = repository.getUltimaParcelaDespesaParcelada(idDespesaParcelada, idFuncionario);
            var valorParcelaAdiantada = repository.getParcelaPorDataVencimento(parcela.getIdDespesaParcelada(), parcela.getDsDataVencimento(), parcela.getIdFuncionario()).getVlParcela();

            /*Exclui a parcela adicionada no final do fluxo*/
            log.info("Excluindo ultima parcela adiantamento >>> " + parcela);
            repository.deleteParcela(parcela.getIdDespesaParcelada(), parcela.getIdParcela(), parcela.getIdFuncionario());

            /*Exclui a parcela adiantada do detalhe despesas mensais (caso tenha sido importada)*/
            repository.deleteParcelaDetalheDespesasMensaisAdiantada(parcela.getIdDespesaParcelada(), parcela.getIdParcela(), idFuncionario);

            /*Desfaz a baixa da parcela com amortizacao e volta o stts para PENDENTE*/
            repository.updateParcelaStatusPendenteParcelaAdiada(idDespesa, idDetalheDespesa, idDespesaParcelada, idParcela, valorParcelaAdiantada, idFuncionario);

            /*Altera a flag de ParcelaAdiada para N no detalhe das despesas mensais e desfaz o pagamento e a despesa de anotacao*/
            repository.updateDetalheDespesasMensaisDesfazerAdiantamento(idDespesa, idDetalheDespesa, idDespesaParcelada, idParcela, valorParcelaAdiantada, idFuncionario);

            /*Atualiza a contagem de parcelas adiantadas*/
            repository.updateQuantidadeParcelasDesfazerAdiantamento(idDespesaParcelada, idFuncionario);
        } else {
            var descricaoDespesa = repository.getDespesaParcelada(idDespesaParcelada, null, idFuncionario).getDsTituloDespesaParcelada();
            throw new ErroNegocioException("A parcela da despesa " + descricaoDespesa + " não pode ser processada. Motivo: Não consta adiantamento de parcelas para esta despesa.");
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
        List<TituloDespesa> listaDespesas;

        if (tipo.equalsIgnoreCase("ativas")) {
            listaDespesas = repository.getNomeDespesasParceladasParaImportacao(idFuncionario);
        } else {
            listaDespesas = repository.getNomeDespesasParceladas(idFuncionario);
        }

        List<String> listTituloDespesa = new ArrayList<>();
        for (TituloDespesa despesas : listaDespesas) {
            listTituloDespesa.add(despesas.getTituloDespesa());
        }

        log.info("ListaNomesDespesasParceladas: {}", listaDespesas);
        return TituloDespesaResponse.builder()
                .despesas(listaDespesas)
                .sizeTituloDespesaVB(listaDespesas.size())
                .tituloDespesa(listTituloDespesa)
                .build();
    }

    public StringResponse validarTituloDespesaParceladaExistente(String dsTituloDespesaParcelada, Integer idDespesaParcelada, Integer idFuncionario) {
        var response = repository.getValidaTituloDespesaParceladaExistente(dsTituloDespesaParcelada, idDespesaParcelada, idFuncionario).compareTo(0) == 0 ? false : true;
        return StringResponse.builder()
                .isTituloJaExistente(response)
                .build();
    }

    public void quitarDespesaParcelada(Integer idDespesaParcelada, Integer idFuncionario, String valorQuitacao) {
        var bDespesaPagaComOuSemDesconto = true;
        BigDecimal valorTotalDespesa = convertStringToDecimal(repository.getValorTotalDespesaParceladaPendente(idDespesaParcelada, idFuncionario));
        BigDecimal valorDesconto = valorTotalDespesa.subtract(convertStringToDecimal(valorQuitacao));

        var observacoes = "Baixa realizada # Pago: " + valorQuitacao + " - Desc: " + convertDecimalToString(valorDesconto) + " #- Quitação TOTAL :)";

        for (ParcelasDAO parcela : repository.getParcelasPorFiltro(idDespesaParcelada, null, "N", idFuncionario)) {
            var idParcela = parcela.getIdParcela();

            if (bDespesaPagaComOuSemDesconto) {
                log.info("Quitacao parcelas >> {}", observacoes);
                repository.updateParcelaStatusQuitado(observacoes, idDespesaParcelada, idParcela, valorQuitacao, idFuncionario);

                log.info("Buscando parcela importada e realizando a baixa do pagamento...");
                DetalheDespesasMensaisDAO filtro = DetalheDespesasMensaisDAO.builder()
                        .idDespesaParcelada(idDespesaParcelada)
                        .idParcela(idParcela)
                        .idFuncionario(idFuncionario)
                        .build();

                var detalheDespesaMensalParcela = repository.getDetalheDespesaMensalPorFiltro(filtro);
                repository.updateStatusPagamentoDetalheDespesa(valorQuitacao, valorQuitacao, PAGO, observacoes, "Baixa Automatica - Quitacao TOTAL", detalheDespesaMensalParcela.getIdDespesa(), detalheDespesaMensalParcela.getIdDetalheDespesa(), detalheDespesaMensalParcela.getIdOrdem(), idFuncionario);

                bDespesaPagaComOuSemDesconto = false;
                continue;
            }

            observacoes = STATUS_BAIXA_REALIZADA_PELO_SISTEMA.concat(" #" + idParcela + "#");
            log.info("Quitacao parcelas >> {}", observacoes);
            repository.updateParcelaStatusQuitado(observacoes, idDespesaParcelada, idParcela, VALOR_ZERO, idFuncionario);
        }

        validarBaixaCadastroDespesaParcelada(idDespesaParcelada, idFuncionario);
    }

    public StringResponse obterValorDespesa(Integer idDespesaParcelada, Integer idParcela, String mesAnoReferencia, Integer idFuncionario) {
        var valorDespesa = VALOR_ZERO;

        if (idParcela == 0 && ObjectUtils.isEmpty(mesAnoReferencia)) {
            valorDespesa = repository.getDespesaParcelada(idDespesaParcelada, null, idFuncionario).getVlFatura();

            return StringResponse.builder()
                    .vlDespesaParcelada(valorDespesa)
                    .build();
        }

        var sWhere = "";
        if (idParcela > 0 && ObjectUtils.isEmpty(mesAnoReferencia)) {
            sWhere = "id_DespesaParcelada = " + idDespesaParcelada + " AND id_Parcelas = " + idParcela + " AND id_Funcionario = " + idFuncionario;
        } else if (idParcela == 0 && !ObjectUtils.isEmpty(mesAnoReferencia)) {
            sWhere = "id_DespesaParcelada = " + idDespesaParcelada + " AND id_Funcionario = " + idFuncionario + " AND ds_DataVencimento = '" + mesAnoReferencia + "'";
        }
        valorDespesa = repository.getValorParcelaPorFiltro(sWhere);

        if (ObjectUtils.isEmpty(valorDespesa)) {
            valorDespesa = repository.getMaxValorParcela(idDespesaParcelada, idFuncionario);
        }

        return StringResponse.builder()
                .vlDespesaParcelada(valorDespesa)
                .build();
    }

    public void excluirParcela(Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario) {
        repository.deleteParcela(idDespesaParcelada, idParcela, idFuncionario);
        repository.deleteDetalheDespesaParcelada(idDespesaParcelada, idParcela, idFuncionario);

        this.validarBaixaCadastroDespesaParcelada(idDespesaParcelada, idFuncionario);
    }

    public void excluirDespesaParcelada(Integer idDespesaParcelada, Integer idFuncionario) {
        repository.deleteDespesasParceladas(idDespesaParcelada, idFuncionario);
        repository.deleteTodasParcelas(idDespesaParcelada, idFuncionario);
        repository.deleteTodosDetalheDespesaParcelada(idDespesaParcelada, idFuncionario);
    }

    public void gravarDespesaParcelada(DespesaParceladaDAO despesa) {
        var idDespesaExistente = repository.getDespesaParcelada(despesa.getIdDespesaParcelada(), null, despesa.getIdFuncionario());

        if (ObjectUtils.isEmpty(idDespesaExistente)) {
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
        var listParcelas = repository.getParcelasPorFiltro(parcela.getIdDespesaParcelada(), parcela.getIdParcela(), null, parcela.getIdFuncionario());

        if (ObjectUtils.isEmpty(listParcelas)) {
            log.info("Gravando Nova Parcela >> Request: {}", parcela);
            repository.insertParcela(parcela);
        } else {
            log.info("Atualizando Parcela >> Request: {}", parcela);
            repository.updateParcela(parcela);

            if (listParcelas.get(0).getVlParcela().equalsIgnoreCase(parcela.getVlParcela())) {
                /*Se nao houver alteracao no valor da parcela, atualiza o valor independente do status do pagamento.*/
                repository.updateValorTotalDetalheDespesasMensaisParcelas(parcela.getVlParcela(), parcela.getIdDespesaParcelada(), parcela.getIdParcela(), parcela.getIdFuncionario(), null);
            } else {
                repository.updateValorTotalDetalheDespesasMensaisParcelas(parcela.getVlParcela(), parcela.getIdDespesaParcelada(), parcela.getIdParcela(), parcela.getIdFuncionario(), PENDENTE);
            }
        }
    }

    public StringResponse obterRelatorioDespesasParceladasQuitacao(Integer idDespesa, Integer idFuncionario) {
        var relatorioDAO = repository.getRelatorioDespesasParceladasQuitacao(idDespesa, idFuncionario);
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

    private ParcelasDAO parserToNovaParcelaAdiantamento(ParcelasDAO parcela) throws ParseException {
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

    private void validarBaixaCadastroDespesaParcelada(Integer idDespesaParcelada, Integer idFuncionario) {
        var qtdeParcelas = repository.getQuantidadeParcelasEmAberto(idDespesaParcelada, idFuncionario);

        if (qtdeParcelas.intValue() <= 0) {
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
}
