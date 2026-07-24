package br.com.esc.backend.security;

import br.com.esc.backend.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static br.com.esc.backend.utils.DataUtils.converterMilissegundosEmHoras;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = extractTokenFromRequest(request);

            if (token != null && !token.isEmpty()) {
                Claims claims = jwtService.validateToken(token);
                setupSecurityContext(claims);
                log.info("TOKEN >>> Token validado com sucesso -- Tempo restante de sessão: {}", converterMilissegundosEmHoras(jwtService.getTokenExpirationTime(token)));
            } else {
                log.info("TOKEN >>> Nenhum token encontrado no header Authorization");
            }

        } catch (ExpiredJwtException ex) {
            log.warn("Token JWT expirado: {}", ex.getMessage());
            sendUnauthorizedError(response, "Token expirado");
            return;
        } catch (JwtException ex) {
            log.warn("Token JWT inválido: {}", ex.getMessage());
            sendUnauthorizedError(response, "Token inválido");
            return;
        } catch (Exception ex) {
            log.error("Erro ao processar token JWT", ex);
            sendUnauthorizedError(response, "Erro ao processar autenticação");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length());
        }

        return null;
    }

    private void setupSecurityContext(Claims claims) {
        Integer userId = Integer.valueOf(claims.getSubject());
        String username = (String) claims.get("username");
        String role = (String) claims.get("role");
        List<String> permissions = (List<String>) claims.get("permissions");

        Collection<GrantedAuthority> authorities = buildAuthorities(role, permissions);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userId, null, authorities);

        authentication.setDetails(claims);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Collection<GrantedAuthority> buildAuthorities(String role, List<String> permissions) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        if (role != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }

        if (permissions != null) {
            for (String permission : permissions) {
                authorities.add(new SimpleGrantedAuthority(permission));
            }
        }

        return authorities;
    }

    private void sendUnauthorizedError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                String.format("{\"error\": \"Unauthorized\", \"message\": \"%s\"}", message)
        );
    }
}
