package br.com.esc.backend.exception;

public class CredenciaisInvalidasException extends AutenticacaoException {
    public CredenciaisInvalidasException() {
        super("Usuario e/ou senha invalidos.");
    }

    public CredenciaisInvalidasException(String message) {
        super(message);
    }
}

