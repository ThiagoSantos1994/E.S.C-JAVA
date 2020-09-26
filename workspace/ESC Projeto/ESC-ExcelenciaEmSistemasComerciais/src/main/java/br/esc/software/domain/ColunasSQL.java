package br.esc.software.domain;

import java.io.Serializable;

public class ColunasSQL implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String nomeColuna;
	private String tipoColuna;
	private String tamanhoColuna;

	public String getNomeColuna() {
		return nomeColuna;
	}

	public void setNomeColuna(String nomeColuna) {
		this.nomeColuna = nomeColuna;
	}

	public String getTipoColuna() {
		return tipoColuna.toUpperCase();
	}

	public void setTipoColuna(String tipoColuna) {
		this.tipoColuna = tipoColuna;
	}

	public String getTamanhoColuna() {
		return tamanhoColuna.toUpperCase();
	}

	public void setTamanhoColuna(String tamanhoColuna) {
		this.tamanhoColuna = tamanhoColuna;
	}

	@Override
	public String toString() {
		return "ColunasSQL [nomeColuna=" + nomeColuna + ", tipoColuna=" + tipoColuna + ", tamanhoColuna="
				+ tamanhoColuna + "]";
	}

}
