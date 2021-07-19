package br.esc.software.commons.utils;

import br.esc.software.domain.aplicacao.LoginResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.security.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Component
public class TokenJWTUtils {

    private KeyPairGenerator keyGenerator = null;

    public String gerarTokenJWT(Map<String, Object> objectMap) throws NoSuchAlgorithmException {
        String jwtToken = Jwts.builder()
                .setClaims(objectMap)
                .setIssuedAt(new Date())
                .setExpiration(
                        Date.from(
                                LocalDateTime.now().plusMinutes(60L)
                                        .atZone(ZoneId.of("America/Sao_Paulo"))
                                        .toInstant()))
                .signWith(this.obterKeyPair(), SignatureAlgorithm.RS512)
                .compact();

        return jwtToken;
    }

    private PrivateKey obterKeyPair() throws NoSuchAlgorithmException {
        keyGenerator = KeyPairGenerator.getInstance("RSA");
        keyGenerator.initialize(2048);

        KeyPair kp = keyGenerator.genKeyPair();
        //PublicKey publicKey = (PublicKey) kp.getPublic();
        PrivateKey privateKey = (PrivateKey) kp.getPrivate();

        return privateKey;
    }

}
