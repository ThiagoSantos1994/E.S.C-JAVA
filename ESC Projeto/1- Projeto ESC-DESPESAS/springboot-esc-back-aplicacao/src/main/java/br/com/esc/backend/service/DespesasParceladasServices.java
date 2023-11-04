package br.com.esc.backend.service;

import br.com.esc.backend.domain.DetalheDespesasMensaisDAO;
import br.com.esc.backend.domain.ParcelasDAO;
import br.com.esc.backend.repository.AplicacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import static br.com.esc.backend.utils.DataUtils.*;
import static br.com.esc.backend.utils.ObjectUtils.isNull;
import static br.com.esc.backend.utils.VariaveisGlobais.*;
import static java.lang.Integer.parseInt;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@RequiredArgsConstructor
public class DespesasParceladasServices {

    private final AplicacaoRepository repository;

    public void isDespesaParceladaExcluida(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idFuncionario) throws Exception {
        if (isNull(idOrdem) || idOrdem == -1) {
            /*Regra especifica para exclusao de todas as despesas parceladas da despesa com idOrdem = -1 ou Nulo*/
            for (DetalheDespesasMensaisDAO detalhe : repository.getDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario)) {
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
        if (!isEmpty(detalhe) && detalhe.getTpLinhaSeparacao().equalsIgnoreCase("N") && detalhe.getIdDespesaParcelada().intValue() > 0) {
            this.validaStatusDespesaParcelada(detalhe.getIdDespesa(), detalhe.getIdDetalheDespesa(), detalhe.getIdDespesaParcelada(), detalhe.getIdParcela(), detalhe.getIdFuncionario(), detalhe.getTpStatus(), true);
        }
    }

    public void validaStatusDespesaParcelada(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario, String statusParcela, Boolean bExcluirDespesa) throws Exception {
        if (isNull(idDespesa) || idDespesa.equals(0) || idParcela.equals(0)) {
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

    private void validarBaixaCadastroDespesaParcelada(Integer idDespesaParcelada, Integer idFuncionario) {
        var qtdeParcelas = repository.getQuantidadeParcelasEmAberto(idDespesaParcelada, idFuncionario);

        if (qtdeParcelas.intValue() <= 0) {
            repository.updateDespesasParceladasEncerrado(idDespesaParcelada, idFuncionario);
        }

        repository.updateDespesasParceladasEmAberto(idFuncionario);
    }

    public void adiantarFluxoParcelas(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario) throws Exception {
        var isValidaAdiantamentoMes = repository.getValidaDespesaParceladaAdiantada(idDespesa, idDetalheDespesa, idDespesaParcelada, idFuncionario);

        if (isValidaAdiantamentoMes.equalsIgnoreCase("N")) {
            var parcela = repository.getUltimaParcelaDespesaParcelada(idDespesaParcelada, idFuncionario);
            var novaParcelaRequest = this.parserToNovaParcelaAmortizacao(parcela);

            /*Grava a nova parcela adiantada*/
            log.info("Gravando nova parcela adiantamento >>> " + novaParcelaRequest);
            repository.insertNovaParcela(novaParcelaRequest);

            /*Baixa a parcela atual e altera o stts para adiantada*/
            var observacoesBaixa = PARCELA_ADIANTADA_DESTE_MES_PARA.concat(novaParcelaRequest.getDsDataVencimento());
            repository.updateParcelaStatusAdiantado(idDespesa, idDetalheDespesa, observacoesBaixa, idParcela, idDespesaParcelada, idFuncionario);

            /*Altera a flag de ParcelaAdiada no detalhe das despesas mensais e baixa o pagamento e marca como despesa de anotacao*/
            var logProcessamento = "Operacao realizada em: " + DataHoraAtual() + " - Usuario: ** " + repository.getUsuarioLogado(idFuncionario);
            repository.updateDetalheDespesasMensaisParcelaAdiada(idDespesa, idDetalheDespesa, idDespesaParcelada, observacoesBaixa, logProcessamento, novaParcelaRequest.getVlParcela(), idFuncionario);

            /*Atualiza a contagem de parcelas adiantadas*/
            repository.updateQuantidadeParcelasAdiantadas(idDespesaParcelada, idFuncionario);
        } else {
            var descricaoDespesa = repository.getDespesaParcelada(idDespesaParcelada, idFuncionario).getDsTituloDespesaParcelada();
            throw new Exception("A parcela da despesa " + descricaoDespesa + " ja foi adiantada neste mês. Não é permitido adiantar a mesma parcela 2X no mesmo mês.");
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
            var descricaoDespesa = repository.getDespesaParcelada(idDespesaParcelada, idFuncionario).getDsTituloDespesaParcelada();
            throw new Exception("A parcela da despesa " + descricaoDespesa + " não pode ser processada. Motivo: Não consta adiantamento de parcelas para esta despesa.");
        }
    }

    private ParcelasDAO parserToNovaParcelaAmortizacao(ParcelasDAO parcela) throws ParseException {
        NumberFormat formatter = new DecimalFormat("000");

        Integer idParcelaNova = (parcela.getIdParcela() + 1);
        String nrParcelaNova = formatter.format(parseInt(parcela.getNrParcela()) + 1);
        String dataVencimentoNova = convertDateToString(retornaDataPersonalizada("01/" + parcela.getDsDataVencimento(), 1)).substring(3, 10);
        String valorParcelaAdiantada = repository.getParcelaPorDataVencimento(parcela.getIdDespesaParcelada(), parcela.getDsDataVencimento(), parcela.getIdFuncionario()).getVlParcela();

        parcela.setIdParcela(idParcelaNova);
        parcela.setNrParcela(nrParcelaNova);
        parcela.setDsDataVencimento(dataVencimentoNova);
        parcela.setVlParcela(valorParcelaAdiantada);
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

}
