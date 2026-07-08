package br.com.esc.backend.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class AuthUtils {

    public static Integer getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return (Integer) authentication.getPrincipal();
        }

        log.warn("Nenhum usuário autenticado encontrado no contexto de segurança.");
        return null;
    }
}
