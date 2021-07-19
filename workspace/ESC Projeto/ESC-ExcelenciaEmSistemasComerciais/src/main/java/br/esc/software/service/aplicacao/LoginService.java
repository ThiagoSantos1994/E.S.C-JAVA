package br.esc.software.service.aplicacao;

import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.domain.aplicacao.LoginResponse;
import br.esc.software.repository.aplicacao.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class LoginService {

    @Autowired
    LoginRepository repository;

    public LoginResponse autenticarLogin(String usuario, String senha) throws SQLException, ExcecaoGlobal {
        return this.repository.autenticar(usuario, senha);
    }

    public LoginResponse obterDados(String id) throws SQLException, ExcecaoGlobal {
        return this.repository.obterDadosUsuario(id);
    }
}
