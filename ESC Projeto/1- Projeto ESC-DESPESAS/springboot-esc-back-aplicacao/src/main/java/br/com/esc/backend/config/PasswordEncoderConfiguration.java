package br.com.esc.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuração de segurança para criptografia de senhas.
 * Utiliza BCrypt como algoritmo de hash.
 */
@Configuration
public class PasswordEncoderConfiguration {

    /**
     * Bean do PasswordEncoder usando BCrypt.
     * BCrypt é um algoritmo de hash adaptativo que automaticamente
     * inclui salt e é resistente a ataques de força bruta.
     *
     * @return instância do BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

