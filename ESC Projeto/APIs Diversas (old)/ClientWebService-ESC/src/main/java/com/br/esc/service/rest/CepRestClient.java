package com.br.esc.service.rest;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.br.esc.model.Cep;

@Component("CepRestClient")
public class CepRestClient {

	private static String URL_CEP = "https://viacep.com.br/ws/";

	private RestTemplate restTemplate = new RestTemplate();

	public Cep getCepPorId(String cepId) {
		Cep cep = restTemplate.getForObject((URL_CEP + cepId + "/json/"), Cep.class);
		return cep;
	}
}
