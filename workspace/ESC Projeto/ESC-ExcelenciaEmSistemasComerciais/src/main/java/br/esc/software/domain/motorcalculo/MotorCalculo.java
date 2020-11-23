package br.esc.software.domain.motorcalculo;

public class MotorCalculo {

    private Double vlTotalDespesas;
    private Double vlReceitaPositiva;
    private Double vlTotalAplicadoPoupanca;
    private Double vlTotalEmprestimosAReceber;
    private Double vlOutrosEmprestimosAReceber;
    private Double vlEmprestimosAPagar;
    private String vlEstimativaPoupanca;

    public Double getVlTotalDespesasDouble() {
        return vlTotalDespesas;
    }

    public Double getVlReceitaPositivaDouble() {
        return vlReceitaPositiva;
    }

    public String getVlTotalDespesas() {
        return vlTotalDespesas.toString().replace(".", ",");
    }

    public void setVlTotalDespesas(Double vlTotalDespesas) {
        this.vlTotalDespesas = vlTotalDespesas;
    }

    public String getVlReceitaPositiva() {
        return vlReceitaPositiva.toString().replace(".", ",");
    }

    public void setVlReceitaPositiva(Double vlReceitaPositiva) {
        this.vlReceitaPositiva = vlReceitaPositiva;
    }

    public String getVlTotalAplicadoPoupanca() {
        return vlTotalAplicadoPoupanca.toString().replace(".", ",");
    }

    public Double getVlTotalAplicadoPoupancaDouble() {
        return vlTotalAplicadoPoupanca;
    }

    public void setVlTotalAplicadoPoupanca(Double vlTotalAplicadoPoupanca) {
        this.vlTotalAplicadoPoupanca = vlTotalAplicadoPoupanca;
    }

    public String getVlTotalEmprestimosAReceber() {
        return vlTotalEmprestimosAReceber.toString().replace(".", ",");
    }

    public Double getVlTotalEmprestimosAReceberDouble() {
        return vlTotalEmprestimosAReceber;
    }

    public void setVlTotalEmprestimosAReceber(Double vlTotalEmprestimosAReceber) {
        this.vlTotalEmprestimosAReceber = vlTotalEmprestimosAReceber;
    }

    public String getVlOutrosEmprestimosAReceber() {
        return vlOutrosEmprestimosAReceber.toString().replace(".", ",");
    }

    public void setVlOutrosEmprestimosAReceber(Double vlOutrosEmprestimosAReceber) {
        this.vlOutrosEmprestimosAReceber = vlOutrosEmprestimosAReceber;
    }

    public String getVlEmprestimosAPagar() {
        return vlEmprestimosAPagar.toString().replace(".", ",");
    }

    public void setVlEmprestimosAPagar(Double vlEmprestimosAPagar) {
        this.vlEmprestimosAPagar = vlEmprestimosAPagar;
    }

    public String getVlEstimativaPoupanca() {
        return vlEstimativaPoupanca.toString().replace(".", ",");
    }

    public void setVlEstimativaPoupanca(Double vlEstimativaPoupanca) {
        this.vlEstimativaPoupanca = vlEstimativaPoupanca.toString().replace(".", ",");
    }

}
