package br.com.esc.login.integration;

import br.com.esc.login.business.LoginBusiness;
import br.com.esc.login.domain.TokenOAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static br.com.esc.login.commons.GlobalUtils.getProperties;

@Service
public class ObterTokenService {

    private RestTemplate restTemplate = new RestTemplate();
    private static final Logger log = LoggerFactory.getLogger(LoginBusiness.class);

    public TokenOAuth obterTokenAPI() throws Exception {
        String url = getProperties().getProperty("prop.token.uri");

        log.info("obterTokenAPI >> Realizando chamada API para obter o TOKEN -->> URL: {{}}", url);

        ResponseEntity<TokenOAuth> response = restTemplate.exchange(url,
                HttpMethod.POST,
                new HttpEntity(getAuthorization()), TokenOAuth.class);

        log.info("Response: {{}}", response.toString());
        return response.getBody();
    }

    private HttpHeaders getAuthorization() throws Exception {
        String basicAuth = getProperties().getProperty("prop.token.basicAuth");

        log.info("Montando header para obter token bearer");
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(basicAuth);
        return headers;
    }
}
