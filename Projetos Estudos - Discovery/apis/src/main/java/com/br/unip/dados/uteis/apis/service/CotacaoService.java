package com.br.unip.dados.uteis.apis.service;

import com.br.unip.dados.uteis.apis.domain.externas.cotacaodolar.Cotacao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CotacaoService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private RestTemplate restTemplate = new RestTemplate();

    public Cotacao obterCotacaoDolar() throws Exception {
        String URL_COTACAO = "https://api.hgbrasil.com/finance?key=b717f4d5&fields=USD";
        logger.info("Realizando chamada API " + URL_COTACAO);

        Cotacao response = restTemplate.getForObject(URL_COTACAO, Cotacao.class);

        logger.info("Response API: " + response.toString());
        return response;
    }
}
