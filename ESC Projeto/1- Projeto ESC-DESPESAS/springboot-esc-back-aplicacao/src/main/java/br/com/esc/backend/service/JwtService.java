package br.com.esc.backend.service;

import br.com.esc.backend.domain.LoginDAO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final SecretKey secretKey = generateSecretKey();

    public String generateToken(LoginDAO usuario) {
        return Jwts.builder()
                .subject(usuario.getIdLogin().toString())
                .claim("role", "ADMIN")
                .claim("username", usuario.getDsLogin())
                .claim("permissions", List.of("READ", "WRITE", "DELETE"))
                .issuedAt(new java.util.Date())
                .expiration(new java.util.Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(20))) // 20 minutos de validade
                .signWith(secretKey)
                .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public long getTokenExpirationTime(String token) {
        Claims claims = validateToken(token);
        java.util.Date expiration = claims.getExpiration();
        long expirationTimeMillis = expiration.getTime();
        long currentTimeMillis = System.currentTimeMillis();
        long remainingMillis = expirationTimeMillis - currentTimeMillis;
        
        return Math.max(remainingMillis, 0);
    }

    public long getTokenExpirationTimeInSeconds(String token) {
        return getTokenExpirationTime(token) / 1000;
    }

    public TokenExpirationInfo getTokenExpirationInfo(String token) {
        Claims claims = validateToken(token);
        long remainingSeconds = getTokenExpirationTimeInSeconds(token);
        java.util.Date expiration = claims.getExpiration();
        java.util.Date issuedAt = claims.getIssuedAt();

        return TokenExpirationInfo.builder()
                .expiresAt(expiration)
                .issuedAt(issuedAt)
                .remainingSeconds(remainingSeconds)
                .remainingMinutes(remainingSeconds / 60)
                .remainingHours(remainingSeconds / 3600)
                .isExpired(remainingSeconds <= 0)
                .username((String) claims.get("username"))
                .userId(claims.getSubject())
                .role((String) claims.get("role"))
                .build();
    }

    private SecretKey generateSecretKey() {
        // Geração de uma chave aleatória de 256 bits (32 bytes) para HS256
        byte[] keyBytes = new byte[32]; // 256 bits
        new SecureRandom().nextBytes(keyBytes);

        SecretKey secretKey = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());

        log.info("==== SecretKey gerada com sucesso ====");
        return secretKey;
    }
}
