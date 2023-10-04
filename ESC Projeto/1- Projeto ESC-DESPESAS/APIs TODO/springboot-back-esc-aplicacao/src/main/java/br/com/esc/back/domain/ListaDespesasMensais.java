package br.com.esc.back.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListaDespesasMensais {
    private Integer id_Despesa;
    private Integer id_OrdemExibicao;
    private Integer id_DetalheDespesa;
    private Integer id_Emprestimo;
    private String ds_NomeDespesa;
    private String vl_Limite;
    private String vl_Total;
    private String vl_TotalPendente;
    private String vl_TotalPago;
    private String percentual;
    private String tp_Emprestimo;
    private String tp_Poupanca;
    private String tp_Anotacao;
    private String tp_DebitoAutomatico;
    private String tp_LinhaSeparacao;
    private String tp_DespesaReversa;
    private String tp_PoupancaNegativa;
    private String tp_Relatorio;
    private String tp_Status;
}
