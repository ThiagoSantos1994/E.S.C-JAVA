package br.esc.software.integration;

import static br.esc.software.commons.GlobalUtils.LogInfo;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import br.esc.software.commons.ObjectParser;
import br.esc.software.domain.CepMapper;

@Component
public class ConsultaCEPImpl {

	private RestTemplate restTemplate = new RestTemplate();
	
	private ObjectParser objectParser = new ObjectParser();
	
	private String URL_CEP = null;

	public String consultaCep(String cep) {
		
		URL_CEP = "https://viacep.com.br/ws/" + cep + "/json/";
		
		LogInfo("Realizando chamada API " + URL_CEP);
		
		String response = objectParser.parser(restTemplate.getForObject(URL_CEP, CepMapper.class));
		
		LogInfo("Response API: " + response);
		
		return response;
	}
	
}
