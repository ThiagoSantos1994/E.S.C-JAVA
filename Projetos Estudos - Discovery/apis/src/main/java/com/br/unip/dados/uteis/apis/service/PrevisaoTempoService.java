package com.br.unip.dados.uteis.apis.service;

import com.br.unip.dados.uteis.apis.domain.externas.previsaotempo.PrevisaoTempo;
import com.br.unip.dados.uteis.apis.domain.externas.previsaotempo.Woeid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PrevisaoTempoService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private RestTemplate restTemplate = new RestTemplate();

    public PrevisaoTempo consultaPrevisaoTempo(String cidade) throws Exception {
        String URL_PREVISAO = "https://api.hgbrasil.com/weather?key=b717f4d5&woeid=" + this.obterWoeid(cidade);
        logger.info("Realizando chamada API " + URL_PREVISAO);

        PrevisaoTempo response = restTemplate.getForObject(URL_PREVISAO, PrevisaoTempo.class);

        logger.info("Response API: " + response.toString());
        return response;
    }

    private Integer obterWoeid(String cidade) {
        String URL_WOEID = "https://api.hgbrasil.com/stats/find_woeid?key=17284dd0&format=json-cors&sdk_version=console&city_name=" + cidade;

        try {
            logger.info("Realizando chamada API " + URL_WOEID);
            Woeid response = restTemplate.getForObject(URL_WOEID, Woeid.class);

            logger.info("Response API: " + response.toString());
            return response.getWoeid();
        } catch (Exception ex) {
            logger.error("Ocorreu um erro ao obter o woeid >>> " + ex.getCause());
            return 455827; //Woeid São Paulo
        }
    }
}
