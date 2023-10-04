package br.com.votorantim.vucl.base.mascarar.dados.model;

public class StringUtils {

	public static boolean isBlank(String valor) {
		return isNull(valor) || valor.trim().isEmpty();
	}

	public static boolean isNotBlank(String valor) {
		return !isBlank(valor);
	}

	public static String nullToEmpty(String valor) {

		if (isNull(valor)) {
			return "";
		}

		return valor;

	}

	public static String blankToEmpty(String valor) {

		if (isBlank(valor)) {
			return "";
		}

		return valor;

	}

	public static String trimToNull(String valor) {

		if (isBlank(valor)) {
			return null;
		}

		return valor.trim();

	}

	public static String trimToBlank(String valor) {

		if (isBlank(valor)) {
			return "";
		}

		return valor.trim();

	}

	public static boolean isAnyBlank(String... valores) {

		if (isNull(valores) || valores.length == 0) {
			return true;
		}

		for (String valor : valores) {
			if (isBlank(valor)) {
				return true;
			}
		}

		return false;
	}

	public static String obterInicio(String valor, int tamanho) {
		String texto = "";

		if (isBlank(valor)) {
			return "";
		}

		if (valor.length() <= tamanho) {
			return valor;
		}

		for (int i = 0; i < valor.length(); i++) {
			char letra = valor.charAt(i);
			if (letra == ' ') {
				texto = texto + " ";
			} else {
				texto = texto + letra;
			}
		}
		return texto.substring(0, tamanho);
	}

	public static String obterFinal(String valor, int tamanho) {
		String texto = "";

		if (isBlank(valor)) {
			return "";
		}

		if (valor.length() <= tamanho) {
			return valor;
		}

		for (int i = 0; i < valor.length(); i++) {
			char letra = valor.charAt(i);
			if (letra == ' ') {
				texto = texto + " ";
			} else {
				texto = texto + letra;
			}
		}
		return texto.substring(valor.length() - tamanho);
	}

	public static String obterInicioFinal(String valor, int tamInicio, int tamFinal) {

		if (isBlank(valor)) {
			return "";
		}

		if (valor.length() <= (tamInicio + tamFinal)) {
			return valor;
		}

		return obterInicio(valor, tamInicio) + "..." + obterFinal(valor, tamFinal);
	}

	public static boolean equals(String valor1, String valor2) {

		if (isNull(valor1)) {
			return isNull(valor2);
		}

		return valor1.equals(valor2);
	}

	public static boolean notEquals(String valor1, String valor2) {
		return !equals(valor1, valor2);
	}

	public static boolean equalsIgnoreCase(String valor1, String valor2) {

		if (isNull(valor1)) {
			return isNull(valor2);
		}

		return valor1.equalsIgnoreCase(valor2);
	}

	private static boolean isNull(String... valor1) {
		if (null == valor1) {
			return true;
		}
		return false;
	}

	public static String repeat(String valor, int qtde) {

		if (isNull(valor)) {
			return null;
		}

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < qtde; i++) {
			sb.append(valor);
		}

		return sb.toString();

	}
}
