package br.com.esc.backend.exception;

public class UsuarioBloqueadoException extends AutenticacaoException {
    public UsuarioBloqueadoException() {
        super("Usuario com status bloqueado ou excluido da base de dados.");
    }
}

