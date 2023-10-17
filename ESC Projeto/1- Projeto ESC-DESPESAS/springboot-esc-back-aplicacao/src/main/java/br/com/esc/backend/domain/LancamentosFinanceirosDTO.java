package br.com.esc.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private Integer sizeLancamentosMensaisVB;
    private List<DespesasFixasMensaisDAO> despesasFixasMensais;
    private List<LancamentosMensaisDAO> lancamentosMensais;
}
