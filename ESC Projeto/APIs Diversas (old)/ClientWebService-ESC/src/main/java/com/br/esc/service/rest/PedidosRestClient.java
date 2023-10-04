package com.br.esc.service.rest;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.br.esc.model.Pedidos;

@Component("PedidosRestClient")
public class PedidosRestClient {

	private static String URL_Pedidos = "http://localhost:8080/pedidos/";

	private RestTemplate restTemplate = new RestTemplate();

	public List<Pedidos> getPedidoPorId(int idPedido) {
		Pedidos[] arrayPedidos = restTemplate.getForObject((URL_Pedidos + idPedido), Pedidos[].class);
		List<Pedidos> listPedidosWS = Arrays.asList(arrayPedidos);
		return listPedidosWS;
	}
	
	public List<Pedidos> getPedidoPorClienteID(String idCliente) {
		Pedidos[] arrayPedidos = restTemplate.getForObject((URL_Pedidos + "/clientes/" + idCliente), Pedidos[].class);
		List<Pedidos> listPedidosWS = Arrays.asList(arrayPedidos);
		return listPedidosWS;
	}
}
