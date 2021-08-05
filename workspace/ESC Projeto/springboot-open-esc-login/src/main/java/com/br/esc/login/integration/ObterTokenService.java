package com.br.esc.login.integration;

import com.br.esc.login.business.LoginBusiness;
import com.br.esc.login.domain.TokenOAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static com.br.esc.login.commons.GlobalUtils.getProperties;

@Component
public class ObterTokenService {

    private RestTemplate restTemplate = new RestTemplate();
    private static final Logger log = LoggerFactory.getLogger(LoginBusiness.class);

    public ResponseEntity<TokenOAuth> obterTokenAPI() throws Exception {
        String URI_TOKEN = getProperties().getProperty("prop.token.uri");

        log.info("Obtendo TOKEN Bearer --> " + URI_TOKEN);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(getProperties().getProperty("prop.token.basicAuth"));

        return restTemplate.postForEntity(URI_TOKEN, new HttpEntity(headers), TokenOAuth.class);
    }
}
