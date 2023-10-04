package br.com.esc.login.restcontroller;

import br.com.esc.login.business.LoginBusiness;
import br.com.esc.login.domain.LoginRequest;
import br.com.esc.login.domain.LoginResponse;
import br.com.esc.login.domain.TrocarSenhaRequest;
import br.com.esc.login.domain.TrocarSenhaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
public class LoginApi {

    @Autowired
    LoginBusiness business;

    private static final Logger log = LoggerFactory.getLogger(LoginApi.class);
    private LoginResponse response = new LoginResponse();

    @PostMapping(path = "/login/autenticar", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> autenticarUsuario(@RequestBody LoginRequest request) throws Exception {
        LoginResponse response = business.autenticarUsuario(request);

        if (response.isAutorizado() == true) {
            return new ResponseEntity<LoginResponse>(response, HttpStatus.OK);
        }

        return new ResponseEntity<LoginResponse>(business.autenticarUsuario(request), HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(path = "/login/alterarSenha", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<TrocarSenhaResponse> trocarSenha(@RequestBody TrocarSenhaRequest request) throws Exception {
        return new ResponseEntity<TrocarSenhaResponse>(business.alterarSenha(request), HttpStatus.OK);
    }
}
