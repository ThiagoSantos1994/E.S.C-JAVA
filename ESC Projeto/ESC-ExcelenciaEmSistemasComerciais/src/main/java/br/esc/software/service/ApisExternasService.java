package br.esc.software.service;

import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.commons.utils.ObjectParser;
import br.esc.software.domain.Response;
import br.esc.software.domain.apis.Cep;
import br.esc.software.domain.apis.PrevisaoTempo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static br.esc.software.commons.utils.GlobalUtils.LogInfo;

@Service
public class ApisExternasService {

    private RestTemplate restTemplate = new RestTemplate();
    private ObjectParser objectParser = new ObjectParser();

    public String consultaCep(String cep) throws ExcecaoGlobal {
        String URL_CEP = "https://viacep.com.br/ws/" + cep + "/json/";

        LogInfo("Realizando chamada API " + URL_CEP);

        String response = objectParser.parser(restTemplate.getForObject(URL_CEP, Cep.class));

        LogInfo("Response API: " + response.toString());

        return response;
    }

    public String consultaPrevisaoTempo() {
        /* Woeid Caieiras */
        String URL_PREVISAO = "https://api.hgbrasil.com/weather?woeid=426987";

        LogInfo("Realizando chamada API " + URL_PREVISAO);

        PrevisaoTempo response = restTemplate.getForObject(URL_PREVISAO, PrevisaoTempo.class);

        LogInfo("Response API: " + response.toString());

        return this.parserPrevisaoTempo(response);
    }

    private String parserPrevisaoTempo(PrevisaoTempo response) {
        LogInfo("Realizando o parser response PrevisaoTempoAPI");

        String parserResponse = response.getResults().getCity_name() + " - Máx: "
                + response.getResults().getForecast().get(0).getMax() + "º - Min: "
                + response.getResults().getForecast().get(0).getMin() + "º";

        Response resp = new Response();
        resp.setResponse(parserResponse);

        return objectParser.parser(resp);
    }
}
