package br.com.esc.backend.controller;

import br.com.esc.backend.facade.AutenticacaoFacade;
import br.com.esc.backend.business.LancamentosFinanceirosBusiness;
import br.com.esc.backend.domain.AutenticacaoResponse;
import br.com.esc.backend.domain.LoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final AutenticacaoFacade autenticacaoFacade;
    private final LancamentosFinanceirosBusiness lancamentosFinanceirosBusiness;

    @PostMapping(path = "/autenticar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AutenticacaoResponse> autenticarLogin(@RequestBody LoginRequest request) {
        var response = autenticacaoFacade.autenticarUsuario(request);
        if (response.getIdLogin().equals(-1)) {
            return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/limparDadosTemporarios", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> limparDadosTemporarios(
            @RequestParam("idFuncionario") Integer idFuncionario) {

        lancamentosFinanceirosBusiness.limparDadosTemporarios(idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

