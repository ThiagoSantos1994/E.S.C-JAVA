package br.com.esc.backend.controller;

import br.com.esc.backend.domain.BooleanResponse;
import br.com.esc.backend.facade.AutenticacaoFacade;
import br.com.esc.backend.domain.StringResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sessao")
@RequiredArgsConstructor
@Slf4j
public class SessaoController {

    private final AutenticacaoFacade autenticacaoFacade;

    @GetMapping(path = "/validar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BooleanResponse> validaSessaoUsuario(
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = autenticacaoFacade.validaSessaoUsuario(idFuncionario);
        return ResponseEntity.ok(response);
    }
}

