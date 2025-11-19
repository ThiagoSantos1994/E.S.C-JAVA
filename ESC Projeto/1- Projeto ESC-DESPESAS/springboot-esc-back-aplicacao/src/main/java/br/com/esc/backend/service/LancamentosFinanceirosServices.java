package br.com.esc.backend.service;

import br.com.esc.backend.domain.*;
import br.com.esc.backend.repository.AplicacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static br.com.esc.backend.utils.DataUtils.anoAtual;
import static br.com.esc.backend.utils.MotorCalculoUtils.*;
import static br.com.esc.backend.utils.ObjectUtils.*;
import static br.com.esc.backend.utils.VariaveisGlobais.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LancamentosFinanceirosServices {

    private final AplicacaoRepository repository;

    public LancamentosFinanceirosDTO obterLancamentosFinanceiros(String dsMes, String dsAno, Integer idFuncionario) {
        List<DespesasFixasMensaisDAO> despesasFixasMensais = repository.getDespesasFixasMensais(dsMes, dsAno, idFuncionario);
        LancamentosFinanceirosDTO dto = new LancamentosFinanceirosDTO();

        if (despesasFixasMensais.isEmpty()) {
            preencherDTOComValoresZerados(dto);
            return dto;
        }

        int idDespesa = despesasFixasMensais.get(0).getIdDespesa();
        int idDespesaAnterior = idDespesa - 1;

        BigDecimal saldoInicialMes = repository.getCalculoSaldoInicialMES(idDespesa, idFuncionario);
        BigDecimal receita = calcularReceitaPositivaMes(repository.getCalculoReceitaPositivaMES(idDespesa, idFuncionario));
        BigDecimal despesa = repository.getCalculoReceitaNegativaMES(idDespesa, idFuncionario);
        BigDecimal despesaPoupancaPositiva = repository.getCalculoDespesaTipoPoupanca(idDespesa, idFuncionario);
        BigDecimal pendente = repository.getCalculoReceitaPendentePgtoMES(idDespesa, idFuncionario);

        // subtrai do saldo disponivel (só para exibição) a soma da despesa tipo poupanca positiva enquanto estiver pendente de pagamento - 07/08/2024
        BigDecimal saldoDisponivel = receita.subtract(despesa).subtract(despesaPoupancaPositiva);

        RelatorioDespesasReceitasDAO relatorio = obterRelatorioDespesasReceitas(idDespesa, idFuncionario);
        List<LancamentosMensaisDAO> lancamentosMensais = obterLancamentosMensais(idDespesa, idDespesaAnterior, idFuncionario, receita);

        dto.setIdDespesa(idDespesa);
        dto.setDsMesReferencia(dsMes);
        dto.setDsAnoReferencia(dsAno);
        dto.setVlSaldoInicialMes(convertToMoedaBR(saldoInicialMes));
        dto.setVlSaldoPositivo(convertToMoedaBR(receita));
        dto.setVlTotalDespesas(convertToMoedaBR(despesa));
        dto.setVlTotalPendentePagamento(convertToMoedaBR(pendente));
        dto.setVlSaldoDisponivelMes(convertToMoedaBR(saldoDisponivel));
        dto.setPcUtilizacaoDespesasMes(new DecimalFormat("00").format(calculaPorcentagem(receita, despesa, 3)).concat("%"));
        dto.setStatusSaldoMes(saldoDisponivel.signum() < 0 ? NEGATIVO : POSITIVO);

        dto.setDespesasFixasMensais(despesasFixasMensais);
        dto.setLancamentosMensais(lancamentosMensais);
        dto.setRelatorioDespesasReceitas(relatorio);

        // Compatibilidade com VB6
        dto.setSizeDespesasFixasMensaisVB(despesasFixasMensais.size());
        dto.setSizeLancamentosMensaisVB(lancamentosMensais.size());

        return dto;
    }

    private void preencherDTOComValoresZerados(LancamentosFinanceirosDTO dto) {
        dto.setVlSaldoInicialMes(VALOR_ZERO);
        dto.setVlSaldoPositivo(VALOR_ZERO);
        dto.setVlTotalDespesas(VALOR_ZERO);
        dto.setVlTotalPendentePagamento(VALOR_ZERO);
        dto.setVlSaldoDisponivelMes(VALOR_ZERO);
        dto.setStatusSaldoMes(POSITIVO);
        dto.setPcUtilizacaoDespesasMes(PERC_ZERO);
        dto.setRelatorioDespesasReceitas(new RelatorioDespesasReceitasDAO());
    }

    private String formatarPorcentagem(BigDecimal valor) {
        return new DecimalFormat("0.0").format(valor) + "%";
    }

    private RelatorioDespesasReceitasDAO obterRelatorioDespesasReceitas(Integer idDespesa, Integer idFuncionario) {
        int idDespesaInicial = (idDespesa - 5);
        int totalMeses = (idDespesa - idDespesaInicial + 1);

        String[] meses = new String[totalMeses];
        BigDecimal[] receitas = new BigDecimal[totalMeses];
        BigDecimal[] despesas = new BigDecimal[totalMeses];

        for (int i = 0; i < totalMeses; i++) {
            int idAtual = idDespesa - i;

            receitas[i] = calcularReceitaPositivaMes(repository.getCalculoReceitaPositivaMES(idAtual, idFuncionario));
            despesas[i] = repository.getCalculoReceitaNegativaMES(idAtual, idFuncionario);
            meses[i] = repository.getMesAnoExtensoPorID(idAtual, idFuncionario);
        }

        RelatorioDespesasReceitasDAO relatorio = new RelatorioDespesasReceitasDAO();
        relatorio.setMeses(meses);
        relatorio.setReceitas(receitas);
        relatorio.setDespesas(despesas);

        return relatorio;
    }

    private List<LancamentosMensaisDAO> obterLancamentosMensais(Integer idDespesa, Integer idDespesaAnterior, Integer idFuncionario, BigDecimal receitaMes) {
        List<LancamentosMensaisDAO> lancamentosMensaisList = new ArrayList<>();

        for (LancamentosMensaisDAO detalhes : repository.getLancamentosMensais(idDespesa, idFuncionario)) {
            int idDetalheDespesa = detalhes.getIdDetalheDespesa();
            detalhes.setIdDespesa(idDespesa);

            if ("S".equalsIgnoreCase(detalhes.getTpLinhaSeparacao())) {
                lancamentosMensaisList.add(detalhes);
                continue;
            }

            if ("S".equalsIgnoreCase(detalhes.getTpRelatorio())) {
                detalhes.setVlTotalDespesa(repository.getCalculoDespesaTipoRelatorio(idDespesa, idDetalheDespesa, idFuncionario));
            }

            if ("S".equalsIgnoreCase(detalhes.getTpReferenciaSaldoMesAnterior())) {
                var vlLimiteMesAnterior = repository.getCalculoTotalDespesa(idDespesaAnterior, idDetalheDespesa, idFuncionario);
                detalhes.setVlLimite(vlLimiteMesAnterior.toString());
            }

            detalhes.setVlTotalDespesaPendente(repository.getCalculoTotalDespesaPendente(idDespesa, idDetalheDespesa, idFuncionario));
            detalhes.setVlTotalDespesaPaga(repository.getCalculoTotalDespesaPaga(idDespesa, idDetalheDespesa, idFuncionario));
            detalhes.setStatusPagamento(repository.getStatusDetalheDespesaPendentePagamento(idDespesa, idDetalheDespesa, idFuncionario) ? PENDENTE : PAGO);

            if ("N".equalsIgnoreCase(detalhes.getTpEmprestimo())) {
                BigDecimal limite = convertStringToDecimal(detalhes.getVlLimite());
                BigDecimal totalDespesa = detalhes.getVlTotalDespesa();

                //Percentual baseado no teto definido de gasto para a despesa (ou referencia mês anterior)
                BigDecimal percentual = calculaPorcentagem(limite, totalDespesa);
                detalhes.setPercentualUtilizacao(formatarPorcentagem(percentual));
                detalhes.setStatusPercentual(percentualUtilizacao(percentual));

                //Percentual calculado automaticamente com base no valor total da receita do mês
                BigDecimal percentualReceita = calculaPorcentagem(receitaMes, totalDespesa);
                detalhes.setPercentualReceita(formatarPorcentagem(percentualReceita));
                detalhes.setStatusPercentualReceita(percentualUtilizacaoReceita(percentualReceita));
            }

            if (DESCRICAO_DESPESA_EMPRESTIMO.equalsIgnoreCase(detalhes.getDsNomeDespesa())) {
                String tituloEmprestimo = repository.getTituloDespesaEmprestimoPorID(detalhes.getIdEmprestimo(), idFuncionario);
                detalhes.setDsNomeDespesa(tituloEmprestimo);
            }

            detalhes.setSVlTotalDespesa(convertToMoedaBR(detalhes.getVlTotalDespesa()));
            detalhes.setSVlTotalDespesaPaga(convertToMoedaBR(detalhes.getVlTotalDespesaPaga()));
            detalhes.setSVlTotalDespesaPendente(convertToMoedaBR(detalhes.getVlTotalDespesaPendente()));

            lancamentosMensaisList.add(detalhes);
        }

        return lancamentosMensaisList;
    }

    public void gravarDespesasFixasMensais(DespesasFixasMensaisRequest request) {
        if (!isEmpty(request.getDVlTotal())) {
            request.setVlTotal(convertDecimalToString(request.getDVlTotal()));
        }
        if (this.isDespesaFixaExistente(request)) {
            log.info("Atualizando despesas fixas mensais - request: {}", request);
            repository.updateDespesasFixasMensais(request);
        } else {
            if (null == request.getIdDespesa() || request.getIdDespesa() <= 0) {
                request.setIdOrdem(1);
                request.setIdDespesa((repository.getMaxIdDespesa(request.getIdFuncionario()) + 1));
            } else {
                request.setIdOrdem(repository.getMaxOrdemDespesasFixasMensais(request.getIdDespesa(), request.getIdFuncionario()));
            }

            log.info("Inserindo despesas fixas mensais - request: {}", request);
            repository.insertDespesasFixasMensais(request);
        }
    }

    public void ordenarListaDespesasMensais(Integer idDespesa, Integer idFuncionario) {
        Integer iOrdemNova = 1;

        for (DespesasFixasMensaisDAO fixas : repository.getDespesasFixasMensaisPorID(idDespesa, idFuncionario)) {
            log.info("Ordenando registros listaDespesasFixas: idDespesa = {}, idOrdemAntiga = {}, idOrdemNova = {}", fixas.getIdDespesa(), fixas.getIdOrdem(), iOrdemNova);
            repository.updateDespesasFixasMensaisOrdenacao(fixas.getIdDespesa(), fixas.getIdOrdem(), iOrdemNova, fixas.getIdFuncionario());
            iOrdemNova++;
        }

        iOrdemNova = 1;
        for (DespesasMensaisDAO despesas : repository.getDespesasMensais(idDespesa, idFuncionario, null)) {
            log.info("Ordenando registros listaDespesas: idDespesa = {}, idDetalheDespesa = {}, idOrdemAntiga = {}, idOrdemNova = {}", despesas.getIdDespesa(), despesas.getIdDetalheDespesa(), despesas.getIdOrdemExibicao(), iOrdemNova);
            repository.updateDespesasMensaisOrdenacao(despesas.getIdDespesa(), despesas.getIdDetalheDespesa(), despesas.getIdOrdemExibicao(), iOrdemNova, despesas.getIdFuncionario());
            iOrdemNova++;
        }
    }

    public void alterarOrdemRegistroDespesas(Integer idDespesa, Integer iOrdemAtual, Integer iOrdemNova, Integer idFuncionario) {
        var iOrdemTemp1 = 9998;
        var iOrdemTemp2 = 9999;

        //Substitui o ID da despesa POSICAO ATUAL
        repository.updateDespesasMensaisOrdenacao(idDespesa, null, iOrdemAtual, iOrdemTemp1, idFuncionario);

        //Substitui o ID da despesa POSICAO NOVA
        repository.updateDespesasMensaisOrdenacao(idDespesa, null, iOrdemNova, iOrdemTemp2, idFuncionario);

        /*Nesta etapa realiza a alteração da posição fazendo o DE x PARA com base nos ID's temporarios*/
        repository.updateDespesasMensaisOrdenacao(idDespesa, null, iOrdemTemp1, iOrdemNova, idFuncionario);
        repository.updateDespesasMensaisOrdenacao(idDespesa, null, iOrdemTemp2, iOrdemAtual, idFuncionario);
    }

    public StringResponse obterMesAnoPorID(Integer idDespesa, Integer idFuncionario) {
        var result = repository.getMesAnoPorID(idDespesa, idFuncionario);
        if (isNull(result)) {
            result = repository.getMesAnoPorIDTemp(idDespesa, idFuncionario);
            if (isNull(result)) {
                result = ERRO;
            }
        }

        log.info("Consultando MesAnoPorID >>> despesaID: {} - response: {}", idDespesa, result);
        return StringResponse.builder()
                .mesAno(result)
                .build();
    }

    public void alterarDespesaMensalReferencia(Integer idDespesa, Integer idDetalheDespesa, Integer idDetalheDespesaNova, Integer idFuncionario) {
        log.info("[Alterar Referencias Despesa Mensal] - Executando script para os parametros de entrada: idDespesa: {} - idDetalheDespesa: {} - idDetalheDespesaNova: {} - idFuncionario: {}", idDespesa, idDetalheDespesa, idDetalheDespesaNova, idFuncionario);

        repository.alterarReferenciasDespesaMensal(idDespesa, idDetalheDespesa, idDetalheDespesaNova, idFuncionario);

        log.info("[Alterar Referencias Despesa Mensal] - Alteração concluida com sucesso!");
    }

    public void alterarOrdemRegistroDespesasFixas(Integer idDespesa, Integer iOrdemAtual, Integer iOrdemNova, Integer idFuncionario) {
        var iOrdemTemp1 = 9998;
        var iOrdemTemp2 = 9999;

        //Substitui o ID da despesa POSICAO ATUAL
        repository.updateDespesasFixasMensaisOrdenacao(idDespesa, iOrdemAtual, iOrdemTemp1, idFuncionario);

        //Substitui o ID da despesa POSICAO NOVA
        repository.updateDespesasFixasMensaisOrdenacao(idDespesa, iOrdemNova, iOrdemTemp2, idFuncionario);

        /*Nesta etapa realiza a alteração da posição fazendo o DE x PARA com base nos ID's temporarios*/
        repository.updateDespesasFixasMensaisOrdenacao(idDespesa, iOrdemTemp1, iOrdemNova, idFuncionario);
        repository.updateDespesasFixasMensaisOrdenacao(idDespesa, iOrdemTemp2, iOrdemAtual, idFuncionario);
    }

    public String validaDespesaExistenteDebitoCartao(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario) {
        var despesa = repository.getValidaDespesaExistenteDebitoCartao(idDespesa, idFuncionario);
        var mensagem = OK;

        if (isNull(despesa)) {
            return mensagem;
        }

        if (despesa.compareTo(idDetalheDespesa) != 0) {
            mensagem = VALIDACAO_DESPESA_DEBITO_CARTAO_EXISTENTE;
        }

        log.info("validaDespesaExistenteDebtCart Mensagem Validacao: >>> {}", mensagem);
        return mensagem;
    }

    public String validarAlteracaoTituloDespesa(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String novoNomeDespesa) {
        var validaDespesaExistente = validaDespesaExistente(idDespesa, idDetalheDespesa, idFuncionario);
        if (!validaDespesaExistente.equalsIgnoreCase(OK)) {
            return validaDespesaExistente;
        }

        var idDetalheDespesaTemp = repository.getIDDetalheDespesaPorTitulo(novoNomeDespesa, idFuncionario);
        if (isNull(idDetalheDespesaTemp)) {
            return TITULO_DESPESA_INEXISTENTE;
        }

        log.info("Realizando a alteração do titulo da despesaID: {} >>> idDetalheDespesa {} ->>> {}", idDespesa, idDetalheDespesa, idDetalheDespesaTemp);

        repository.updateDespesaMensalTituloReuso(idDespesa, idDetalheDespesa, idDetalheDespesaTemp, novoNomeDespesa, idFuncionario);
        repository.updateDetalheDespesasMensaisID(idDespesa, idDetalheDespesa, idDetalheDespesaTemp, idFuncionario);

        return OK;
    }

    public void alterarTituloDespesa(Integer idDetalheDespesa, Integer idFuncionario, String dsNomeDespesa) {
        repository.updateTituloDespesasMensais(idDetalheDespesa, idFuncionario, dsNomeDespesa, anoAtual());
    }

    public String validaTituloDespesaDuplicado(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String dsTituloDespesa) {
        Integer response = repository.getValidaTituloDespesaDuplicado(idDespesa, idDetalheDespesa, idFuncionario, dsTituloDespesa);

        if (response > 0) {
            return TITULO_DESPESA_DUPLICADO;
        }

        return OK;
    }

    public TituloDespesaResponse getTituloDespesa(Integer idFuncionario) {
        List<String> tituloDespesa = repository.getTituloDespesa(idFuncionario);

        TituloDespesaResponse response = new TituloDespesaResponse();
        response.setSizeTituloDespesaVB(tituloDespesa.size());
        response.setTituloDespesa(tituloDespesa);

        return response;
    }


    public TituloDespesaResponse getTituloEmprestimo(Integer idFuncionario) {
        List<String> tituloDespesa = repository.getTituloDespesaEmprestimoAReceber(idFuncionario);

        TituloDespesaResponse response = new TituloDespesaResponse();
        response.setSizeTituloDespesaVB(tituloDespesa.size());
        response.setTituloDespesa(tituloDespesa);

        return response;
    }

    public TituloDespesaResponse getTituloDespesaRelatorio(Integer idDespesa, Integer idFuncionario) {
        var tituloList = repository.getNomeDespesaRelatorio(idDespesa, idFuncionario);

        return TituloDespesaResponse.builder().
                despesas(tituloList).
                build();
    }

    private Boolean isDespesaFixaExistente(DespesasFixasMensaisRequest request) {
        var despesaFixaMensal = repository.getDespesaFixaMensalPorFiltro(request.getIdDespesa(), request.getIdOrdem(), request.getIdFuncionario());
        return isNotNull(despesaFixaMensal);
    }

    private String validaDespesaExistente(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario) {
        var despesa = repository.getValidaDespesaExistente(idDespesa, idDetalheDespesa, idFuncionario);

        if (isNull(despesa)) {
            return VALIDACAO_DESPESA_INEXISTENTE;
        }

        return OK;
    }

    private String percentualUtilizacao(BigDecimal porcentagem) {
        var percentual = Integer.parseInt(new DecimalFormat("0").format(porcentagem));

        if (percentual >= 0 && percentual <= 25) {
            return PERC_BAIXO;
        } else if (percentual >= 26 && percentual <= 50) {
            return PERC_MEDIO;
        } else if (percentual >= 51 && percentual <= 80) {
            return PERC_ALTO;
        } else if (percentual >= 81) {
            return PERC_ALTISSIMO;
        }

        return ERRO;
    }

    private String percentualUtilizacaoReceita(BigDecimal porcentagem) {
        var percentual = Integer.parseInt(new DecimalFormat("0").format(porcentagem));

        if (percentual >= 0 && percentual <= 10) {
            return PERC_BAIXO;
        } else if (percentual >= 11 && percentual <= 20) {
            return PERC_MEDIO;
        } else if (percentual >= 21 && percentual <= 24) {
            return PERC_ALTO;
        } else if (percentual >= 25) {
            return PERC_ALTISSIMO;
        }

        return ERRO;
    }
}
