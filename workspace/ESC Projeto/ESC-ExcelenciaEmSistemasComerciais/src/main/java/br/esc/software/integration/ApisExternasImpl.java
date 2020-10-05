package br.esc.software.integration;

import static br.esc.software.commons.GlobalUtils.LogInfo;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import br.esc.software.commons.ExcecaoGlobal;
import br.esc.software.commons.ObjectParser;
import br.esc.software.domain.CepMapper;
import br.esc.software.domain.PrevisaoTempoMapper;

@Component
public class ApisExternasImpl {

	private RestTemplate restTemplate = new RestTemplate();
	private ObjectParser objectParser = new ObjectParser();

	public String consultaCep(String cep) throws ExcecaoGlobal {
		String URL_CEP = "https://viacep.com.br/ws/" + cep + "/json/";

		LogInfo("Realizando chamada API " + URL_CEP);

		String response = objectParser.parser(restTemplate.getForObject(URL_CEP, CepMapper.class));

		LogInfo("Response API: " + response);

		return response;
	}
	
	public PrevisaoTempoMapper consultaPrevisaoTempo() {
		/*Woeid Caieiras*/
		String URL_PREVISAO = "https://api.hgbrasil.com/weather?woeid=426987";
		
		LogInfo("Realizando chamada API " + URL_PREVISAO);

		PrevisaoTempoMapper response = restTemplate.getForObject(URL_PREVISAO, PrevisaoTempoMapper.class);

		LogInfo("Response API: " + response);

		return response;
	}
}
