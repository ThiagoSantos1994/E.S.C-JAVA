package com.br.esc.service.rest;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.br.esc.model.Cliente;

@Component("ClienteRestClient")
public class ClienteRestClient {

	private static String URL_ClientesID = "http://localhost:8081/clientes/";

	private RestTemplate restTemplate = new RestTemplate();

	public Cliente getClientePorId(String clienteId) {
		Cliente cliente = restTemplate.getForObject((URL_ClientesID + clienteId), Cliente.class);
		return cliente;
	}
	
	public Cliente getClientePorTelefone(String telefoneId) {
		Cliente cliente = restTemplate.getForObject(URL_ClientesID + "telefone/"+ telefoneId, Cliente.class);
		return cliente;
	}
}
