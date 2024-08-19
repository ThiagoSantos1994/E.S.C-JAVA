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

import static br.com.esc.backend.utils.GlobalUtils.getAnoAtual;
import static br.com.esc.backend.utils.MotorCalculoUtils.*;
import static br.com.esc.backend.utils.ObjectUtils.*;
import static br.com.esc.backend.utils.VariaveisGlobais.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LancamentosFinanceirosServices {

    private final AplicacaoRepository repository;

    public LancamentosFinanceirosDTO obterLancamentosFinanceiros(String dsMes, String dsAno, Integer idFuncionario) {
        LancamentosFinanceirosDTO dto = new LancamentosFinanceirosDTO();

        List<DespesasFixasMensaisDAO> despesasFixasMensais = repository.getDespesasFixasMensais(dsMes, dsAno, idFuncionario);
        if (despesasFixasMensais.size() == 0) {
            dto.setVlSaldoPositivo(VALOR_ZERO);
            dto.setVlTotalDespesas(VALOR_ZERO);
            dto.setVlTotalPendentePagamento(VALOR_ZERO);
            dto.setVlSaldoDisponivelMes(VALOR_ZERO);
            dto.setStatusSaldoMes(POSITIVO);
            return dto;
        }

        var idDespesa = despesasFixasMensais.get(0).getIdDespesa();
        var idDespesaAnterior = (idDespesa - 1);

        var saldoInicialMes = repository.getCalculoSaldoInicialMES(idDespesa, idFuncionario);
        var receita = calcularReceitaPositivaMes(repository.getCalculoReceitaPositivaMES(idDespesa, idFuncionario));
        var despesa = repository.getCalculoReceitaNegativaMES(idDespesa, idFuncionario);
        var despesaPoupancaPositiva = repository.getCalculoDespesaTipoPoupanca(idDespesa, idFuncionario);
        var pendente = repository.getCalculoReceitaPendentePgtoMES(idDespesa, idFuncionario);
        // subtrai do saldo disponivel (só para exibição) a soma da despesa tipo poupanca positiva enquanto estiver pendente de pagamento - 07/08/2024
        var saldoDisponivelMes = receita.subtract(despesa).subtract(despesaPoupancaPositiva);

        dto.setIdDespesa(idDespesa);
        dto.setDsMesReferencia(dsMes);
        dto.setDsAnoReferencia(dsAno);
        dto.setVlSaldoPositivo(convertToMoedaBR(receita));
        dto.setVlTotalDespesas(convertToMoedaBR(despesa));
        dto.setVlTotalPendentePagamento(convertToMoedaBR(pendente));
        dto.setVlSaldoInicialMes(convertToMoedaBR(saldoInicialMes));
        dto.setPcUtilizacaoDespesasMes(new DecimalFormat("00").format(this.obterPercentualUtilizacaoDespesaMes(saldoInicialMes, receita, despesa)).concat("%"));
        dto.setVlSaldoDisponivelMes(convertToMoedaBR(saldoDisponivelMes));
        dto.setDespesasFixasMensais(despesasFixasMensais);
        dto.setStatusSaldoMes(saldoDisponivelMes.toString().contains("-") ? "Negativo" : "Positivo");
        dto.setLancamentosMensais(this.obterLancamentosMensais(idDespesa, idDespesaAnterior, idFuncionario));

        /*Especifico para aplicação VB6*/
        dto.setSizeDespesasFixasMensaisVB(despesasFixasMensais.size());
        dto.setSizeLancamentosMensaisVB(dto.getLancamentosMensais().size());

        return dto;
    }

    private List<LancamentosMensaisDAO> obterLancamentosMensais(Integer idDespesa, Integer idDespesaAnterior, Integer idFuncionario) {
        List<LancamentosMensaisDAO> lancamentosMensaisList = new ArrayList<>();

        for (LancamentosMensaisDAO detalhes : repository.getLancamentosMensais(idDespesa, idFuncionario)) {
            var idDetalheDespesa = detalhes.getIdDetalheDespesa();
            detalhes.setIdDespesa(idDespesa);

            var bLinhaSeparacao = detalhes.getTpLinhaSeparacao();
            if (bLinhaSeparacao.equalsIgnoreCase("S")) {
                lancamentosMensaisList.add(detalhes);
                continue;
            }

            var bDespesaRelatorio = detalhes.getTpRelatorio();
            if (bDespesaRelatorio.equalsIgnoreCase("S")) {
                detalhes.setVlTotalDespesa(repository.getCalculoDespesaTipoRelatorio(idDespesa, idDetalheDespesa, idFuncionario));
            }

            var bRefValorDespesaMesAnterior = detalhes.getTpReferenciaSaldoMesAnterior();
            if (bRefValorDespesaMesAnterior.equalsIgnoreCase("S")) {
                var vlLimiteMesAnterior = repository.getCalculoTotalDespesa(idDespesaAnterior, idDetalheDespesa, idFuncionario);
                detalhes.setVlLimite(vlLimiteMesAnterior.toString());
            }

            detalhes.setVlTotalDespesaPendente(repository.getCalculoTotalDespesaPendente(idDespesa, idDetalheDespesa, idFuncionario));
            detalhes.setVlTotalDespesaPaga(repository.getCalculoTotalDespesaPaga(idDespesa, idDetalheDespesa, idFuncionario));
            detalhes.setStatusPagamento(repository.getStatusDetalheDespesaPendentePagamento(idDespesa, idDetalheDespesa, idFuncionario) ? "Pendente" : "Pago");

            if (detalhes.getTpEmprestimo().equalsIgnoreCase("N")) {
                var percentual = calculaPorcentagem(convertStringToDecimal(detalhes.getVlLimite()), detalhes.getVlTotalDespesa());
                detalhes.setPercentualUtilizacao(new DecimalFormat("0.0").format(percentual).concat("%"));
                detalhes.setStatusPercentual(this.validarPercentualUtilizacao(percentual));
            }

            if (detalhes.getDsNomeDespesa().equalsIgnoreCase(DESCRICAO_DESPESA_EMPRESTIMO)) {
                detalhes.setDsNomeDespesa(repository.getTituloDespesaEmprestimoPorID(detalhes.getIdEmprestimo(), idFuncionario));
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
        repository.updateTituloDespesasMensais(idDetalheDespesa, idFuncionario, dsNomeDespesa, getAnoAtual());
    }

    public String validaTituloDespesaDuplicado(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String dsTituloDespesa) {
        Integer response = repository.getValidaTituloDespesaDuplicado(idDespesa, idDetalheDespesa, idFuncionario, dsTituloDespesa);

        if (response > 0) {
            return TITULO_DESPESA_DUPLICADO;
        }

        return OK;
    }

    public TituloDespesaResponse getTituloDespesa(Integer idFuncionario) {
        var tituloDespesaList = repository.getTituloDespesa(idFuncionario);

        List<String> tituloDespesa = new ArrayList<>();
        for (String titulo : tituloDespesaList) {
            tituloDespesa.add(titulo);
        }

        TituloDespesaResponse tituloDespesaResponse = new TituloDespesaResponse();
        tituloDespesaResponse.setSizeTituloDespesaVB(tituloDespesa.size());
        tituloDespesaResponse.setTituloDespesa(tituloDespesa);

        return tituloDespesaResponse;
    }

    public TituloDespesaResponse getTituloEmprestimo(Integer idFuncionario) {
        var tituloList = repository.getTituloDespesaEmprestimoAReceber(idFuncionario);

        List<String> tituloDespesa = new ArrayList<>();
        for (String titulo : tituloList) {
            tituloDespesa.add(titulo);
        }

        TituloDespesaResponse tituloDespesaResponse = new TituloDespesaResponse();
        tituloDespesaResponse.setSizeTituloDespesaVB(tituloDespesa.size());
        tituloDespesaResponse.setTituloDespesa(tituloDespesa);

        return tituloDespesaResponse;
    }

    public TituloDespesaResponse getTituloDespesaRelatorio(Integer idDespesa, Integer idFuncionario) {
        var tituloList = repository.getNomeDespesaRelatorio(idDespesa, idFuncionario);

        return TituloDespesaResponse.builder().
                despesas(tituloList).
                build();
    }

    private BigDecimal obterPercentualUtilizacaoDespesaMes(BigDecimal saldoInicialMes, BigDecimal receita, BigDecimal despesa) {
        var iPercentualUtilizadoBase = calculaPorcentagem(receita, (saldoInicialMes.subtract(receita)), 2);
        return calculaPorcentagem(saldoInicialMes, (despesa.subtract(saldoInicialMes)), 2).add(iPercentualUtilizadoBase);
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

    private String validarPercentualUtilizacao(BigDecimal porcentagem) {
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
}
