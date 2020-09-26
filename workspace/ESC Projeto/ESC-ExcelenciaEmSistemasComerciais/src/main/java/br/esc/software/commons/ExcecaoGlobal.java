package br.esc.software.commons;

import static br.esc.software.commons.GlobalUtils.LogErro;

public class ExcecaoGlobal extends Exception {

	private static final long serialVersionUID = 1L;

	public ExcecaoGlobal() {
		super();
	}

	public ExcecaoGlobal(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		LogErro(message);
	}

	public ExcecaoGlobal(String message, Throwable cause) {
		super(message, cause);
		LogErro(message);
	}

	public ExcecaoGlobal(String message) {
		super(message);
		LogErro(message);
	}

	public ExcecaoGlobal(Throwable cause) {
		super(cause);
		LogErro(cause.toString());
	}

}
