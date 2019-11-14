package br.com.votorantim.vucl.base.mascarar.dados.processor;

import java.util.ArrayList;
import java.util.List;

import br.com.votorantim.vucl.base.mascarar.dados.model.DadosRequest;
import br.com.votorantim.vucl.base.mascarar.dados.model.DadosResponse;
import br.com.votorantim.vucl.base.mascarar.dados.model.StringUtils;

public class MascararDados {
	static String TELEFONE = "telefone";
	static String EMAIL = "email";
	static String CONTRATO = "contrato";
	static String ENDERECO = "endereco";

	public ArrayList<DadosResponse> MascararDados(List<DadosRequest> request) {

		ArrayList<DadosResponse> response = new ArrayList<DadosResponse>();
		
		for (DadosRequest dados : request) {
			DadosResponse dadosResponse = new DadosResponse();

			String parametro = dados.getParametro();
			String chave = dados.getChave();
			String valor = dados.getValor();

			dadosResponse.setParametro(parametro);
			dadosResponse.setChave(chave);
			dadosResponse.setValor(this.tratarDados(parametro, valor));
			response.add(dadosResponse);
		}

		return response;
	}

	private String tratarDados(String parametro, String valor) {
		StringUtils utils = new StringUtils();
		String valorMascarado = "";
		int iTamanhoValor = valor.length();

		try {
			if (parametro.equals(TELEFONE)) {

				iTamanhoValor = (iTamanhoValor - 4);
				valorMascarado = utils.repeat("*", 4);
				valorMascarado = valorMascarado + utils.obterFinal(valor, iTamanhoValor);
				return valorMascarado;

			} else if (parametro.equals(EMAIL)) {

				iTamanhoValor = (iTamanhoValor - 5);
				valorMascarado = utils.obterInicio(valor, 1);
				valorMascarado = valorMascarado + utils.repeat("*", 4);
				valorMascarado = valorMascarado + utils.obterFinal(valor, iTamanhoValor);
				return valorMascarado;

			} else if (parametro.equals(CONTRATO)) {

				iTamanhoValor = (iTamanhoValor - 4);
				valorMascarado = valorMascarado + utils.repeat("*", iTamanhoValor);
				valorMascarado = valorMascarado + utils.obterFinal(valor, 4);
				return valorMascarado;

			} else if (parametro.equals(ENDERECO)) {

				iTamanhoValor = (iTamanhoValor - 5);
				valorMascarado = utils.obterInicio(valor, 1);
				valorMascarado = valorMascarado + utils.repeat("*", 4);
				valorMascarado = valorMascarado + utils.obterFinal(valor, iTamanhoValor);
				return valorMascarado;

			} else {

				iTamanhoValor = (iTamanhoValor / 2);
				valorMascarado = utils.obterInicio(valor, iTamanhoValor);
				valorMascarado = valorMascarado + utils.repeat("*", iTamanhoValor);
				return valorMascarado;

			}
		} catch (Exception e) {
			return valor;
		}
	}
}
