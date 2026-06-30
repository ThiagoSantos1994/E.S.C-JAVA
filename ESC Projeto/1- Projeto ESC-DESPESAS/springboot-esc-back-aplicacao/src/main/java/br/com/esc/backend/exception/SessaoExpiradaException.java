package br.com.esc.backend.exception;

public class SessaoExpiradaException extends AutenticacaoException {
    public SessaoExpiradaException() {
        super("Sessão expirada por tempo excedido.");
    }

    public SessaoExpiradaException(String message) {
        super(message);
    }
}

