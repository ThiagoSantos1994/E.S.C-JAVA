package br.esc.software.integration;

import br.esc.software.commons.utils.ObjectParser;
import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.domain.Response;
import br.esc.software.domain.apis.CepMapper;
import br.esc.software.domain.apis.PrevisaoTempoMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static br.esc.software.commons.utils.GlobalUtils.LogInfo;

@Component
public class ApisExternasImpl {

    private RestTemplate restTemplate = new RestTemplate();
    private ObjectParser objectParser = new ObjectParser();

    public String consultaCep(String cep) throws ExcecaoGlobal {
        String URL_CEP = "https://viacep.com.br/ws/" + cep + "/json/";

        LogInfo("Realizando chamada API " + URL_CEP);

        String response = objectParser.parser(restTemplate.getForObject(URL_CEP, CepMapper.class));

        LogInfo("Response API: " + response.toString());

        return response;
    }

    public String consultaPrevisaoTempo() {
        /* Woeid Caieiras */
        String URL_PREVISAO = "https://api.hgbrasil.com/weather?woeid=426987";

        LogInfo("Realizando chamada API " + URL_PREVISAO);

        PrevisaoTempoMapper response = restTemplate.getForObject(URL_PREVISAO, PrevisaoTempoMapper.class);

        LogInfo("Response API: " + response.toString());

        return this.parserPrevisaoTempo(response);
    }

    private String parserPrevisaoTempo(PrevisaoTempoMapper response) {
        LogInfo("Realizando o parser response PrevisaoTempoAPI");

        String parserResponse = response.getResults().getCity_name() + " - Máx: "
                + response.getResults().getForecast().get(0).getMax() + "º - Min: "
                + response.getResults().getForecast().get(0).getMin() + "º";

        Response resp = new Response();
        resp.setResponse(parserResponse);

        return objectParser.parser(resp);
    }
}
