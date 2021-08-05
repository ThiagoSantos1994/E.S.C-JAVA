package com.br.esc.service.orquestrador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.br.esc.model.Cep;
import com.br.esc.model.Cliente;
import com.br.esc.model.DetalhePedidos;
import com.br.esc.model.Pedidos;
import com.br.esc.service.rest.CepRestClient;
import com.br.esc.service.rest.ClienteRestClient;
import com.br.esc.service.rest.DetalhesPedidosRestPedidos;
import com.br.esc.service.rest.PedidosRestClient;

@RestController
public class InicializarOrquestrador {
	/* Orquestrador baseado em protocolo REST */

	@Autowired
	PedidosRestClient pedidosRestClient = new PedidosRestClient();
	ClienteRestClient clienteRestClient = new ClienteRestClient();
	CepRestClient cepRestClient = new CepRestClient();
	DetalhesPedidosRestPedidos detalhePedidosRestClient = new DetalhesPedidosRestPedidos();

	@GetMapping("/ws/pedidos/{idPedido}")
	public List<Pedidos> InicializarOrquestrador(@PathVariable("idPedido") int idPedido) {
		List<Pedidos> pedidos = pedidosRestClient.getPedidoPorId(idPedido);

		for (Pedidos pedido : pedidos) {
			List<DetalhePedidos> detalhePedidos = detalhePedidosRestClient.getDetalhesPedidoPorId(idPedido);
			pedido.setDetalhePedidos(detalhePedidos);

			Cliente cliente = clienteRestClient.getClientePorId(pedido.getClienteID());
			pedido.setCliente(cliente);

			if (!cliente.getEnderecoComplemento().equals("")) {
				try {
					Cep enderecoCliente = cepRestClient.getCepPorId(cliente.getEnderecoComplemento());
					cliente.setCep(enderecoCliente);
				} catch (BadRequest ex) {
					System.out.println("Ocorreu um erro no Response do Serviço CEP");
				}

			}
		}
		return pedidos;
	}

	@GetMapping("/ws/pedidos/cliente/{nrTelefone}")
	public List<Pedidos> BuscarPedidosPorClienteTelefone(@PathVariable("nrTelefone") String nrTelefone) {
		Cliente cliente = clienteRestClient.getClientePorTelefone(nrTelefone);
		List<Pedidos> pedidos = pedidosRestClient.getPedidoPorClienteID(cliente.getClienteID());

		for (Pedidos pedido : pedidos) {
			List<DetalhePedidos> detalhePedidos = detalhePedidosRestClient.getDetalhesPedidoPorId(Integer.parseInt(pedido.getPedidoID()));
			pedido.setDetalhePedidos(detalhePedidos);
			
			pedido.setCliente(cliente);
			if (!cliente.getEnderecoComplemento().equals("")) {
				Cep enderecoCliente = cepRestClient.getCepPorId(cliente.getEnderecoComplemento());
				cliente.setCep(enderecoCliente);
			}
		}
		return pedidos;
	}

}
