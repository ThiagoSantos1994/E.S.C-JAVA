package com.br.esc.login.business;

import com.br.esc.login.domain.DadosLogin;
import com.br.esc.login.domain.LoginRequest;
import com.br.esc.login.domain.LoginResponse;
import com.br.esc.login.domain.TokenOAuth;
import com.br.esc.login.integration.ObterTokenService;
import com.br.esc.login.repository.LoginRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class LoginBusiness {

    @Autowired
    LoginRepository repository;
    @Autowired
    ObterTokenService tokenOAuth;

    private static final Logger log = LoggerFactory.getLogger(LoginBusiness.class);

    public LoginResponse autenticarUsuario(LoginRequest request) throws Exception {
        log.info("Obtendo dados login >> USUARIO: " + request.getUsuario());

        List<DadosLogin> dadosLogin = repository.obterDadosLogin(request);

        LoginResponse response = validarAutenticidade(dadosLogin);

        return response;
    }

    private LoginResponse validarAutenticidade(List<DadosLogin> dadosLogin) throws Exception {
        log.info("Validando autenticidade do usuario...");

        LoginResponse response = new LoginResponse();
        response.setAutorizado(Boolean.FALSE);
        response.setMensagem("Usuario ou senha invalidos.");

        if (dadosLogin.isEmpty()) {
            log.info("Usuario não localizado na base de dados...");
            return response;
        }

        for (DadosLogin dados : dadosLogin) {
            response.setIdUsuario(dados.getId_Login());

            if (dados.getTp_UsuarioBloqueado().equals("S") || dados.getTp_FuncionarioExcluido().equals("S")) {
                response.setMensagem("Usuario bloqueado, entre em contato com o administrador do sistema.");
                return response;
            }
        }

        /*Autenticação SUCESSO - Obtendo token bearer*/
        log.info("Obtendo TOKEN OAuth API...");
        ResponseEntity<TokenOAuth> tokenBearer = tokenOAuth.obterTokenAPI();
        response.setAutenticacao(tokenBearer.getBody().getAccess_token());

        /*Autenticação SUCESSO*/
        response.setAutorizado(Boolean.TRUE);
        response.setMensagem("Usuario autenticado com sucesso!");

        return response;
    }
}
