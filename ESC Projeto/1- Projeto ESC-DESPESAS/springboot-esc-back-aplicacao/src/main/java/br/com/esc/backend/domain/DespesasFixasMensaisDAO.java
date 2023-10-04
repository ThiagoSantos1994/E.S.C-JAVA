package br.com.esc.backend.domain;

import lombok.Data;

@Data
public class DespesasFixasMensaisDAO {
    private Integer id_Despesa;
    private Integer id_Ordem;
    private String ds_Descricao;
    private String vl_Total;
    private String tp_Status;
    private String tp_FixasObrigatorias;
    private String tp_DespesaDebitoCartao;
}
