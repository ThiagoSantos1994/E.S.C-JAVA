package br.com.esc.back.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ListaDespesasFixasMensais {
    private String ds_Descricao;
    private String vl_Total;
    private String tp_Status;
    private Integer id_Despesa;
    private Integer id_Ordem;
}
