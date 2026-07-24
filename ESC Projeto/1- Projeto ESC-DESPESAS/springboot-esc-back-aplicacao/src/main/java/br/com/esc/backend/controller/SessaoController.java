package br.com.esc.backend.controller;

import br.com.esc.backend.domain.BooleanResponse;
import br.com.esc.backend.facade.AutenticacaoFacade;
import br.com.esc.backend.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sessao")
@RequiredArgsConstructor
@Slf4j
public class SessaoController {

    private final AutenticacaoFacade autenticacaoFacade;

    @GetMapping(path = "/validar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BooleanResponse> validaSessaoUsuario() {
        var response = autenticacaoFacade.validaSessaoUsuario(AuthUtils.getUserId());
        return ResponseEntity.ok(response);
    }
}

