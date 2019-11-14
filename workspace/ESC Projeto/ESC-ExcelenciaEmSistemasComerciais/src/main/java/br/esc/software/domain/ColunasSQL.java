package br.esc.software.domain;

public class ColunasSQL {
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

}
