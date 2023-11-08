package br.com.esc.backend.exception;

public class CamposObrigatoriosException extends RuntimeException {
    public CamposObrigatoriosException(String message) {
        super(message);
    }
}
