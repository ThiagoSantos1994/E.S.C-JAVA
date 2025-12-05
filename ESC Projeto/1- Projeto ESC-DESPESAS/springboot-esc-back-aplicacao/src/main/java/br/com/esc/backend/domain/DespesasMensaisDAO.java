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
public class DespesasMensaisDAO {
    private Integer idDespesa;
    private Integer idDetalheDespesa;
    private String dsTituloDespesa;
    private String dsNomeDespesa;
    private String dsExtratoDespesa;
    private String vlLimite;
    private String vlLimiteExibicao;
    private String vlTotalDespesa;
    private Integer idOrdemExibicao;
    private Integer idFuncionario;
    private Integer idEmprestimo;
    private Integer idConsolidacao;
    private String tpReprocessar;
    private String tpEmprestimo;
    private String tpEmprestimoAPagar;
    private String tpPoupanca;
    private String tpAnotacao;
    private String tpDebitoAutomatico;
    private String tpMeta;
    private String tpLinhaSeparacao;
    private String tpDespesaReversa;
    private String tpPoupancaNegativa;
    private String tpRelatorio;
    private String tpDebitoCartao;
    private String tpReferenciaSaldoMesAnterior;
    private String tpVisualizacaoTemp;
    private String tpDespesaCompartilhada;
    private String tpDespesaConsolidacao;
}
