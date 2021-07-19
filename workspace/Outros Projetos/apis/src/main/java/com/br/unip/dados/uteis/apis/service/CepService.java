package com.br.unip.dados.uteis.apis.service;

import com.br.unip.dados.uteis.apis.domain.externas.consultacep.ConsultaCEP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CepService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private RestTemplate restTemplate = new RestTemplate();

    public ConsultaCEP consultaCep(String cep) throws Exception {
        String URL_CEP = "https://viacep.com.br/ws/" + cep + "/json/";
        logger.info("Realizando chamada API " + URL_CEP);

        ConsultaCEP response = restTemplate.getForObject(URL_CEP, ConsultaCEP.class);

        logger.info("Response API: " + response.toString());
        return response;
    }
}
