package br.com.esc.backend.controller;

import br.com.esc.backend.business.LancamentosFinanceirosBusiness;
import br.com.esc.backend.domain.AutenticacaoResponse;
import br.com.esc.backend.domain.LoginRequest;
import br.com.esc.backend.exception.CredenciaisInvalidasException;
import br.com.esc.backend.exception.UsuarioBloqueadoException;
import br.com.esc.backend.facade.AutenticacaoFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        try {
            AutenticacaoResponse response = autenticacaoFacade.autenticarUsuario(request);
            return ResponseEntity.ok(response);
        } catch (CredenciaisInvalidasException e) {
            log.warn("Falha na autenticacao: credenciais invalidas >> usuario: {}", request.getUsuario());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AutenticacaoResponse.builder()
                            .idLogin(-1)
                            .mensagem(e.getMessage())
                            .autorizado(false)
                            .build());
        } catch (UsuarioBloqueadoException e) {
            log.warn("Falha na autenticacao: usuario bloqueado >> usuario: {}", request.getUsuario());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(AutenticacaoResponse.builder()
                            .idLogin(-1)
                            .mensagem(e.getMessage())
                            .autorizado(false)
                            .build());
        }
    }

    @DeleteMapping(path = "/limparDadosTemporarios", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> limparDadosTemporarios(
            @RequestParam("idFuncionario") Integer idFuncionario) {

        lancamentosFinanceirosBusiness.limparDadosTemporarios(idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

