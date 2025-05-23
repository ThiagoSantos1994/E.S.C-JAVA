package br.com.esc.back.controller;

import br.com.esc.back.service.LoginBusiness;
import br.com.esc.back.domain.DadosLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginApi {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    LoginBusiness business;

    @GetMapping(path = "/login/obterDados/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DadosLogin> obterDadosUsuario(@PathVariable("id") String id_Login) throws Exception {
        logger.info("Executando operacao obterDadosUsuario");
        return new ResponseEntity<DadosLogin>(business.obterDados(id_Login), HttpStatus.OK);
    }
}
