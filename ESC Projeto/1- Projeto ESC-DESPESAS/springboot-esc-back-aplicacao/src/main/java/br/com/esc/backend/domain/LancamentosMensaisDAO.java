package br.com.esc.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LancamentosMensaisDAO {
    private Integer idDespesa;
    private Integer idDetalheDespesa;
    private Integer idOrdemExibicao;
    private Integer idEmprestimo;
    private Integer idConsolidacao;
    private Integer idFuncionario;
    private String dsTituloDespesa;
    private String dsNomeDespesa;
    private String vlLimite;
    private BigDecimal vlTotalDespesa;
    private BigDecimal vlTotalDespesaPendente;
    private BigDecimal vlTotalDespesaPaga;
    private String sVlTotalDespesa;
    private String sVlTotalDespesaPendente;
    private String sVlTotalDespesaPaga;
    private String statusPagamento;
    private String percentualUtilizacao;
    private String statusPercentual;
    private String percentualReceita;
    private String statusPercentualReceita;
    private String tpEmprestimo;
    private String tpEmprestimoAPagar;
    private String tpPoupanca;
    private String tpAnotacao;
    private String tpDebitoAutomatico;
    private String tpLinhaSeparacao;
    private String tpDespesaReversa;
    private String tpDebitoCartao;
    private String tpPoupancaNegativa;
    private String tpRelatorio;
    private String tpReferenciaSaldoMesAnterior;
    private String tpDespesaCompartilhada;
    private String tpDespesaConsolidacao;
}
