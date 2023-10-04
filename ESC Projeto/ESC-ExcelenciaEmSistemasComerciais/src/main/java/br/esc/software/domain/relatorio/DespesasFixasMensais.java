package br.esc.software.domain.relatorio;

public class DespesasFixasMensais {
    private Integer id_Despesa;
    private String ds_Mes;
    private String ds_Ano;

    public Integer getId_Despesa() {
        return id_Despesa;
    }

    public void setId_Despesa(Integer id_Despesa) {
        this.id_Despesa = id_Despesa;
    }

    public String getDs_Mes() {
        return ds_Mes;
    }

    public void setDs_Mes(String ds_Mes) {
        this.ds_Mes = ds_Mes;
    }

    public String getDs_Ano() {
        return ds_Ano;
    }

    public void setDs_Ano(String ds_Ano) {
        this.ds_Ano = ds_Ano;
    }
}
