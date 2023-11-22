package br.com.esc.backend.exception;

public class ErroNegocioException extends RuntimeException {
    public ErroNegocioException(String message) {
        super(message);
    }
}
