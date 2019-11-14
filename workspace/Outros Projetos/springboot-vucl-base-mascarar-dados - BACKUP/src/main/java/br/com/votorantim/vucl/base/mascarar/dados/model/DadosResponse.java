package br.com.votorantim.vucl.base.mascarar.dados.model;

public class DadosResponse {

	public String parametro;
	public String chave;
	public String valor;

	public String getParametro() {
		return parametro;
	}

	public void setParametro(String parametro) {
		this.parametro = parametro;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
	
	@Override
	public String toString() {
		return "DadosResponse{" + "parametro=" + parametro + ", chave=" + chave + ", valor=" + valor + '}';
	}
}
