package br.com.esc.login.business;

import br.com.esc.login.domain.*;
import br.com.esc.login.integration.ObterTokenService;
import br.com.esc.login.repository.LoginRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


@Component
public class LoginBusiness {

    @Autowired
    LoginRepository repository;
    @Autowired
    ObterTokenService tokenOAuth;

    private boolean obterTokenAutentication = true;

    private static final Logger log = LoggerFactory.getLogger(LoginBusiness.class);

    public LoginResponse autenticarUsuario(LoginRequest request) throws Exception {
        log.info("Obtendo dados login >> USUARIO: " + request.getUsuario());
        return validarAutenticidade(repository.obterDadosLogin(request));
    }

    public TrocarSenhaResponse alterarSenha(TrocarSenhaRequest request) throws Exception {
        TrocarSenhaResponse response = new TrocarSenhaResponse();
        log.info("Alterando senha >> USUARIO: " + request.getUsuario());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsuario(request.getUsuario());
        loginRequest.setSenha(request.getSenhaAtual());

        this.obterTokenAutentication = false;
        LoginResponse dadosLogin = this.autenticarUsuario(loginRequest);

        if (!dadosLogin.isAutorizado()) {
            response.setDs_Status(dadosLogin.getMensagem());
            response.setBSucesso(FALSE);
            log.info("Alterando senha >> STTS: " + dadosLogin.getMensagem());
        } else {
            repository.alterarSenha(request);
            response.setBSucesso(TRUE);
            response.setDs_Status("Senha atualizada com sucesso!");
            log.info("Alterando senha >> STTS: Senha atualizada com sucesso");
        }

        return response;
    }

    private LoginResponse validarAutenticidade(List<DadosLogin> dadosLogin) throws Exception {
        log.info("Validando autenticidade do usuario...");

        LoginResponse response = new LoginResponse();
        response.setAutorizado(FALSE);
        response.setMensagem("Usuario ou senha invalidos.");

        if (dadosLogin.isEmpty()) {
            log.info("Usuario não localizado na base de dados...");
            return response;
        }

        for (DadosLogin dados : dadosLogin) {
            response.setId_Login(dados.getId_Login());

            if (dados.getTp_UsuarioBloqueado().equals("S") || dados.getTp_FuncionarioExcluido().equals("S")) {
                response.setMensagem("Usuario bloqueado, entre em contato com o administrador do sistema.");
                return response;
            }
        }

        /*Autenticação SUCESSO - Obtendo token bearer*/
        log.info("Usuario autenticado com sucesso!");
        response.setAutorizado(Boolean.TRUE);
        response.setMensagem("Usuario autenticado com sucesso!");
        response.setAutenticacao("Bearer " + this.obterToken());

        return response;
    }

    private String obterToken() throws Exception {
        if (this.obterTokenAutentication) {
            TokenOAuth tokenBearer = tokenOAuth.obterTokenAPI();
            return tokenBearer.getAccess_token();
        }
        return "-";
    }
}
