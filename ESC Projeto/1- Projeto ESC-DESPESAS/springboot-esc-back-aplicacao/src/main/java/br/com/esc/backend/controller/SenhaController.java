package br.com.esc.backend.controller;

import br.com.esc.backend.domain.BooleanResponse;
import br.com.esc.backend.domain.StringResponse;
import br.com.esc.backend.service.PasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para operações administrativas de senhas.
 * Use com cuidado - apenas para administradores do sistema.
 */
@RestController
@RequestMapping("/api/admin/senha")
@RequiredArgsConstructor
@Slf4j
public class SenhaController {

    private final PasswordService passwordService;

    /**
     * Gera um hash BCrypt para uma senha fornecida.
     * Use este endpoint para gerar hashes de senhas para inserir no banco.
     *
     * ATENÇÃO: Este endpoint deve ser protegido em produção!
     *
     * @param senha senha em texto plano
     * @return hash BCrypt
     */
    @PostMapping(path = "/gerarHash", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> gerarHashBCrypt(@RequestParam("senha") String senha) {
        log.info("Gerando hash BCrypt para senha");

        String hash = passwordService.gerarHashSenha(senha);

        return ResponseEntity.ok(StringResponse.builder()
                .data(hash)
                .build());
    }

    /**
     * Valida se uma senha corresponde a um hash BCrypt.
     *
     * @param senha senha em texto plano
     * @param hash hash BCrypt para validar
     * @return true se a senha corresponde ao hash
     */
    @PostMapping(path = "/validarHash", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> validarHashBCrypt(
            @RequestParam("senha") String senha,
            @RequestParam("hash") String hash) {
        log.info("Validando senha contra hash BCrypt");

        boolean valido = passwordService.validarSenha(senha, hash);

        return ResponseEntity.ok(StringResponse.builder()
                .data(valido ? "Senha válida" : "Senha inválida")
                .build());
    }

    /**
     * Verifica se uma string é um hash BCrypt válido.
     *
     * @param valor valor a ser verificado
     * @return true se for um hash BCrypt
     */
    @GetMapping(path = "/verificarFormato", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> verificarFormatoBCrypt(@RequestParam("valor") String valor) {
        boolean isBCrypt = passwordService.isBCryptHash(valor);

        return ResponseEntity.ok(StringResponse.builder()
                .data(isBCrypt ? "Formato BCrypt válido" : "Não é formato BCrypt (texto plano ou outro)")
                .build());
    }
}

