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
    private String vlSaldoPositivo;
    private String vlTotalDespesas;
    private String vlTotalPendentePagamento;
    private String vlSaldoDisponivelMes;
    private String vlSaldoInicialMes;
    private String pcUtilizacaoDespesasMes;
    private String labelQuitacaoParcelasMes;
    private String statusSaldoMes;
    private Integer sizeDespesasFixasMensaisVB;
    private Integer sizeLancamentosMensaisVB;
    private RelatorioDespesasReceitasDAO relatorioDespesasReceitas;
    private List<DespesasFixasMensaisDAO> despesasFixasMensais;
    private List<LancamentosMensaisDAO> lancamentosMensais;
}
