package br.com.esc.backend.rest;

import br.com.esc.backend.domain.DadosLogin;
import br.com.esc.backend.service.AplicacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AplicacaoController {

    private final AplicacaoService service;

    @GetMapping(path = "/login/obterDados/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DadosLogin>> obterDadosUsuario(@PathVariable("id") Integer id_Login) throws Exception {
        var response = service.obterDadosLogin(id_Login);
        return ResponseEntity.ok(response);
    }
}
