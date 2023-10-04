package br.esc.software;

import java.util.StringTokenizer;

public class TESTES {

	public void TestesGerais() {
		String endereco = "RUA NAPOLEAO BONAPARTE";
		int iTamanhoOriginal = endereco.length();
		int iTamanhoValor = (iTamanhoOriginal - 5);
//
////		System.out.println(endereco);
////		
//		String valorMascarado = obterInicio(endereco, 1);
//		valorMascarado = valorMascarado + repeat(endereco.substring(1, 5), "*", 4);
//		valorMascarado = valorMascarado + obterFinal(endereco, iTamanhoValor);
//
//		System.out.println("MASCARAMENTO SUGERIDO PELA ARQUITETURA");
//		System.out.println(valorMascarado);
//
//		System.out.println("\nMASCARAMENTO METADE STRING");
//		System.out.println(mascaraMetadeValor(endereco));
//
//		System.out.println("\nMASCARAMENTO CENTRO STRING");
//		System.out.println(mascaraCentroValor(endereco));

		String valorMascarado = "";
		String logradouro = "";
		
		
		StringTokenizer token = new StringTokenizer(endereco, " ");
		while (token.hasMoreTokens()) {
			String string = token.nextToken();
			
			iTamanhoValor = 0;
			if(string.length() - 5 > 0) {
				iTamanhoValor = (string.length() - 5);
			}
			
			valorMascarado = obterInicio(string, 1);
			valorMascarado = valorMascarado + repeat(string.substring(1, string.length()), "*", 4);
			valorMascarado = valorMascarado + obterFinal(string, iTamanhoValor) + " ";
			logradouro = logradouro + valorMascarado;
		}
		System.out.println(logradouro);
		
	}

	public static String repeat(String valor, String mascara, int qtde) {
		StringBuilder sb = new StringBuilder();

		if (isNull(valor)) {
			return "";
		} else {
			if (valor.length() < qtde) {
				qtde = valor.length();
			}
			for (int i = 0; i < qtde; i++) {
				if (valor.charAt(i) == ' ') {
					sb.append(" ");
				} else {
					sb.append("*");
				}
			}
		}
		return sb.toString();
	}

	private static boolean isNull(String... valor1) {
		if (null == valor1) {
			return true;
		}
		return false;
	}

	private static String mascaraMetadeValor(String valor) {
		int iTamanhoOriginal = valor.length();
		int iTamanhoValor = 0;
		String valorMascarado;

		iTamanhoValor = (iTamanhoOriginal / 2);
		valorMascarado = obterInicio(valor, iTamanhoValor);

		iTamanhoValor = (iTamanhoOriginal - iTamanhoValor);
		return valorMascarado = valorMascarado + repeat(obterFinal(valor, iTamanhoValor), "*", iTamanhoValor);

	}

	private static String mascaraCentroValor(String valor) {
		int iTamanhoOriginal = valor.length();
		int iTamanhoInicio = (iTamanhoOriginal / 3);
		int iTamanhoFinal = iTamanhoInicio;
		int iTamanhoMascarado = (iTamanhoOriginal - (iTamanhoInicio + iTamanhoFinal));

		return obterInicioFinal(valor, iTamanhoInicio, iTamanhoMascarado, iTamanhoFinal);
	}

	public static String repeat(String valor, int qtde) {
		StringBuilder sb = new StringBuilder();

		if (isNull(valor)) {
			return "";
		} else {
			for (int i = 0; i < qtde; i++) {
				sb.append(valor);
			}
			return sb.toString();
		}
	}

	public static String obterInicio(String valor, int tamanho) {
		String texto = valor;

		if (valor.length() <= tamanho) {
			return valor;
		}
		return texto.substring(0, tamanho);
	}

	public static String obterFinal(String valor, int tamanho) {
		String texto = valor;

		if (valor.length() < tamanho) {
			tamanho = valor.length();
		}
		return texto.substring(valor.length() - tamanho);
	}

	public static String obterInicioFinal(String valor, int tamInicio, int tamMascarado, int tamFinal) {
		StringBuilder sb = new StringBuilder();

		sb.append(obterInicio(valor, tamInicio));
		sb.append(repeat(valor.substring(tamInicio, (tamInicio + tamMascarado)), "*", tamMascarado));
		sb.append(obterFinal(valor, tamFinal));
		return sb.toString();
	}
}
