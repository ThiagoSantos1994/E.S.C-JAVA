package br.com.esc.back.business;

import br.com.esc.back.domain.DadosLogin;
import br.com.esc.back.domain.LoginRequest;
import br.com.esc.back.domain.LoginResponse;
import br.com.esc.back.repository.LoginRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.Boolean.FALSE;


@Component
public class LoginBusiness {

    @Autowired
    LoginRepository repository;

    private boolean obterTokenAutentication = true;

    private static final Logger log = LoggerFactory.getLogger(LoginBusiness.class);

    public LoginResponse autenticarUsuario(LoginRequest request) throws Exception {
        log.info("Obtendo dados login >> USUARIO: " + request.getUsuario());
        return validarAutenticidade(repository.obterDadosLogin(request));
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

        log.info("Usuario autenticado com sucesso!");
        response.setAutorizado(Boolean.TRUE);
        response.setMensagem("Usuario autenticado com sucesso!");
        response.setAutenticacao("Bearer ");

        return response;
    }

}
