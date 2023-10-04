package br.com.esc.back.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListaDetalheDespesasMensais {
    private Integer id_Despesa;
    private Integer id_Ordem;
    private Integer id_DetalheDespesa;
    private Integer id_DespesaParcelada;
    private Integer id_Parcela;
    private Integer id_Funcionario;
    private String vl_Total;
    private String ds_Descricao;
    private String vl_TotalPago;
    private String tp_Status;
    private Integer id_DespesaLinkRelatorio;
    private String ds_Observacao;
    private String ds_Observacao2;
    private String tp_Reprocessar;
    private String tp_Anotacao;
    private String tp_Meta;
    private String tp_ParcelaAdiada;
    private String tp_ParcelaAmortizada;
    private String tp_Relatorio;
    private String tp_LinhaSeparacao;
}
