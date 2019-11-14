package br.esc.software.exceptions;

import static br.esc.software.global.Global.*;

public class ErroGenericoException extends Exception {

	private static final long serialVersionUID = 3999966333927429050L;

	public ErroGenericoException() {
		super();
		LogErro("Ocorreu um erro generico na classe" + getClass());
	}

	public ErroGenericoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		LogErro(message);
	}
	
	public ErroGenericoException(String message, Throwable cause) {
		super(message, cause);
		LogErro(message);
	}

	public ErroGenericoException(String message) {
		super(message);
		LogErro(message);
	}

	public ErroGenericoException(Throwable cause) {
		super(cause);
		String motivo = cause.toString();
		LogErro(motivo);
	}
	
}
