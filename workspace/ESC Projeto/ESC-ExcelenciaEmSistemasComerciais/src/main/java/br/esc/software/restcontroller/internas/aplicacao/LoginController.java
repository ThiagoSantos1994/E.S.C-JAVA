package br.esc.software.restcontroller.internas.aplicacao;

import br.esc.software.business.aplicacao.LoginBusiness;
import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.domain.aplicacao.LoginRequest;
import br.esc.software.domain.aplicacao.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static br.esc.software.commons.utils.GlobalUtils.LogInfo;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private LoginBusiness business;

    @PostMapping(path = "/login/autenticar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity autenticarLogin(@RequestBody LoginRequest request) throws SQLException, ExcecaoGlobal, NoSuchAlgorithmException {

        LogInfo("<<INICIO>> Autenticando LOGIN >>> [ " + request.getUsuario() + " ] Senha: [ " + request.getSenha() + " ]");

        ResponseEntity response = business.autenticar(request);

        LogInfo("<<FIM>> status autenticacao = " + response.toString());

        return response;
    }

    @GetMapping(path = "/login/obterDados/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> obterDados(@PathVariable("id") String id) throws SQLException, ExcecaoGlobal {
        return new ResponseEntity<LoginResponse>(business.obterDados(id), HttpStatus.OK);
    }
}
