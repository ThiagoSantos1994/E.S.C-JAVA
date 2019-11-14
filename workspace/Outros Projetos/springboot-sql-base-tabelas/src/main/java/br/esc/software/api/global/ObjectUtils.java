package br.esc.software.api.global;

public class ObjectUtils {
	public static boolean isNull(Object obj) {
		return obj == null;
	}

	public static boolean isNotNull(Object obj) {
		return !isNull(obj);
	}

	public static String toString(Object obj) {
		if (isNull(obj)) {
			return null;
		}
		return obj.toString();
	}

	public static String pularLinha(int i) {
		if(i <= 0) {
			i = 1;
		}
		
		String texto = "";
		for (int j = 0; j < i; j++) {
			texto += "\\n";
		}
		
		return texto;
	}
	
	public static String pularLinha() {
		return "\\n";
	}
}
