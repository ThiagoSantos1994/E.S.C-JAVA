package br.esc.software.domain.exportador;

import java.io.Serializable;

public class TabelasSQL implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nomeTabela;

    public String getNomeTabela() {
        return nomeTabela;
    }

    public void setNomeTabela(String nomeTabela) {
        this.nomeTabela = nomeTabela;
    }

    @Override
    public String toString() {
        return "TabelasSQL [nomeTabela=" + nomeTabela + "]";
    }
}
