package com.br.esc.service.rest;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.br.esc.model.DetalhePedidos;

@Component("DetalhesPedidosRestPedidos")
public class DetalhesPedidosRestPedidos {

	private static String URL_Pedidos = "http://localhost:8080/pedidos/detalhes";

	private RestTemplate restTemplate = new RestTemplate();

	public List<DetalhePedidos> getDetalhesPedidoPorId(int idPedido) {
		DetalhePedidos[] arrayDetalhesPedidos = restTemplate.getForObject((URL_Pedidos + "/" + idPedido),DetalhePedidos[].class);
		List<DetalhePedidos> listDetalhePedidosWS = Arrays.asList(arrayDetalhesPedidos);
		return listDetalhePedidosWS;
	}
}
