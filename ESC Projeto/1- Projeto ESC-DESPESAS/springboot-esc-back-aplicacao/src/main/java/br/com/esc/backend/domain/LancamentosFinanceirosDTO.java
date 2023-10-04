package br.com.esc.backend.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class LancamentosFinanceirosDTO {
    private Integer idDespesa;
    private String dsMesReferencia;
    private String dsAnoReferencia;
    private BigDecimal vlSaldoPositivo;
    private BigDecimal vlTotalDespesas;
    private BigDecimal vlTotalPendentePagamento;
    private BigDecimal vlSaldoDisponivelMes;
    private BigDecimal vlSaldoInicialMes;
    private String pcUtilizacaoDespesasMes;
    private String labelQuitacaoParcelasMes;
    private Integer sizeDespesasFixasMensaisVB;
    private Integer sizeDetalheDespesasMensaisVB;
    private List<DespesasFixasMensaisDAO> despesasFixasMensais;
    private List<DespesasMensaisDAO> detalheDespesasMensais;
}
