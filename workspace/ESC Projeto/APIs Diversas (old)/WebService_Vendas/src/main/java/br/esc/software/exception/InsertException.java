package br.esc.software.exception;

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsertException extends Exception implements Serializable {

	private static final long serialVersionUID = 1L;
	static final Logger logger = LoggerFactory.getLogger(InsertException.class);

	public InsertException(Exception e) {
		super(e);
	}

	public InsertException(String message, Exception cause) {
		super(message, cause);
	}

}
