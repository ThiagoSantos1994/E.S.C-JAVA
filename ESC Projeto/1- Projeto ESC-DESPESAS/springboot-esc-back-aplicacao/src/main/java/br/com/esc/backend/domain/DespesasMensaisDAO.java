package br.com.esc.backend.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DespesasMensaisDAO {
    private Integer id_DetalheDespesa;
    private String ds_NomeDespesa;
    private String vl_Limite;
    private BigDecimal vl_TotalDespesa;
    private BigDecimal vl_TotalDespesaPendente;
    private BigDecimal vl_TotalDespesaPaga;
    private String percentualUtilizacao;
    private Integer id_OrdemExibicao;
    private Integer id_Emprestimo;
    private String tp_Emprestimo;
    private String tp_Poupanca;
    private String tp_Anotacao;
    private String tp_DebitoAutomatico;
    private String tp_LinhaSeparacao;
    private String tp_DespesaReversa;
    private String tp_PoupancaNegativa;
    private String tp_Relatorio;
    private String tp_ReferenciaSaldoMesAnterior;
    private String tp_DespesaCompartilhada;
}
