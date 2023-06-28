package br.esc.software.domain.relatorio;

public class DetalheDespesas {
    private String ds_NomeDespesa;
    private Integer id_Ordem;

    public String getDs_NomeDespesa() {
        return ds_NomeDespesa;
    }

    public void setDs_NomeDespesa(String ds_NomeDespesa) {
        this.ds_NomeDespesa = ds_NomeDespesa;
    }

    public Integer getId_Ordem() {
        return id_Ordem;
    }

    public void setId_Ordem(Integer id_Ordem) {
        this.id_Ordem = id_Ordem;
    }
}
