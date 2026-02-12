package br.com.esc.backend.controller;

import br.com.esc.backend.business.ParametrosBusiness;
import br.com.esc.backend.domain.ConfiguracaoLancamentosRequest;
import br.com.esc.backend.domain.ConfiguracaoLancamentosResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parametros")
@RequiredArgsConstructor
@Slf4j
public class ParametrosController {

    private final ParametrosBusiness parametrosBusiness;

    @GetMapping(path = "/obterConfiguracaoLancamentos/usuario", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConfiguracaoLancamentosResponse> obterConfiguracaoLancamentos(
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = parametrosBusiness.obterConfiguracaoLancamentos(idFuncionario);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/gravar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> gravarParametrosSistema(@RequestBody ConfiguracaoLancamentosRequest request) {
        parametrosBusiness.gravarConfiguracoesLancamentos(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

