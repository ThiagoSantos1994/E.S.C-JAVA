package br.com.esc.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Serviço utilitário para gerenciamento de senhas.
 * Fornece métodos para gerar hash BCrypt e validar senhas.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordService {

    private final PasswordEncoder passwordEncoder;

    /**
     * Gera um hash BCrypt para a senha fornecida.
     * Use este método ao cadastrar ou alterar senhas de usuários.
     *
     * @param senhaTextoPlano senha em texto plano
     * @return hash BCrypt da senha
     */
    public String gerarHashSenha(String senhaTextoPlano) {
        if (senhaTextoPlano == null || senhaTextoPlano.isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser nula ou vazia");
        }

        String hash = passwordEncoder.encode(senhaTextoPlano);
        log.info("Hash BCrypt gerado com sucesso");
        return hash;
    }

    /**
     * Valida se a senha fornecida corresponde ao hash armazenado.
     *
     * @param senhaTextoPlano senha em texto plano fornecida pelo usuário
     * @param hashArmazenado hash BCrypt armazenado no banco
     * @return true se a senha corresponde ao hash
     */
    public boolean validarSenha(String senhaTextoPlano, String hashArmazenado) {
        if (senhaTextoPlano == null || hashArmazenado == null) {
            return false;
        }
        return passwordEncoder.matches(senhaTextoPlano, hashArmazenado);
    }

    /**
     * Verifica se a senha está em formato BCrypt.
     *
     * @param senha string a ser verificada
     * @return true se for um hash BCrypt
     */
    public boolean isBCryptHash(String senha) {
        if (senha == null || senha.length() < 60) {
            return false;
        }
        return senha.startsWith("$2a$") || senha.startsWith("$2b$") || senha.startsWith("$2y$");
    }

    /**
     * Verifica se a senha precisa ser migrada para BCrypt.
     *
     * @param senhaArmazenada senha atual armazenada no banco
     * @return true se a senha precisa ser convertida para BCrypt
     */
    public boolean precisaMigrarParaBCrypt(String senhaArmazenada) {
        return !isBCryptHash(senhaArmazenada);
    }
}

