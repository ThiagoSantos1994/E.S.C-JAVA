package br.esc.software.domain.relatorio;

public class Despesas {
    private String nomeDespesa;
    private Integer id_DetalheDespesa;

    public String getNomeDespesa() {
        return nomeDespesa;
    }

    public void setNomeDespesa(String nomeDespesa) {
        this.nomeDespesa = nomeDespesa;
    }

    public Integer getId_DetalheDespesa() {
        return id_DetalheDespesa;
    }

    public void setId_DetalheDespesa(Integer id_DetalheDespesa) {
        this.id_DetalheDespesa = id_DetalheDespesa;
    }
}
