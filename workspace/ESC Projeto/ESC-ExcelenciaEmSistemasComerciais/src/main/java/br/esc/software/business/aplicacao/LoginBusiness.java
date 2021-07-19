package br.esc.software.business.aplicacao;

import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.commons.utils.TokenJWTUtils;
import br.esc.software.domain.aplicacao.LoginRequest;
import br.esc.software.domain.aplicacao.LoginResponse;
import br.esc.software.service.aplicacao.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static br.esc.software.commons.utils.GlobalUtils.LogInfo;

@Component
public class LoginBusiness {

    @Autowired
    LoginService service;
    @Autowired
    private TokenJWTUtils jwtUtils;

    private LoginResponse mapper = new LoginResponse();

    public ResponseEntity<LoginResponse> autenticar(LoginRequest dados) throws SQLException, ExcecaoGlobal, NoSuchAlgorithmException {
        LoginResponse response = service.autenticarLogin(dados.getUsuario(), dados.getSenha());

        if (!validarAutenticacao(response)) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        LogInfo("Gerando chave KEY JWT");

        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("id_Login", response.getId_Login());
        hashMap.put("ds_NomeLogin", response.getDs_NomeLogin());

        String chaveJWT = jwtUtils.gerarTokenJWT(hashMap);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("x-access-token", chaveJWT);

        LogInfo("Chave KEY JWT gerada com sucesso!");
        return ResponseEntity.ok().headers(responseHeaders).body(response);
    }

    private boolean validarAutenticacao(LoginResponse response) {
        if (response.getId_Login() == 0) {
            response.setMensagemValidacao("Usuario ou senha invalidos.");
            return false;
        }
        if (response.isTp_UsuarioBloqueado()) {
            response.setMensagemValidacao("Usuario bloqueado!");
            return false;
        }

        response.setMensagemValidacao("Autenticado com sucesso!");
        return true;
    }

    public LoginResponse obterDados(String id) throws SQLException, ExcecaoGlobal {
        return service.obterDados(id);
    }

}
