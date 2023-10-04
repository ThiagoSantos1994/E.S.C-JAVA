package com.br.esc.service.rest;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.esc.controller.PedidosBusiness;
import com.br.esc.model.PedidosPorID;

@RestController
public class WebServicePedidosRest {

	@Autowired
	public PedidosBusiness pedidosBusiness;

	@GetMapping("/pedidos")
	public ArrayList<PedidosPorID> pedidos() {
		return pedidosBusiness.buscarPedidosPorId(0);
	}

	@GetMapping("/pedidos/{pedidoid}")
	public @ResponseBody ArrayList<PedidosPorID> pedidosPorPedidoId(@PathVariable("pedidoid") int pedidoId) {
		return pedidosBusiness.buscarPedidosPorId(pedidoId);
	}

	@PostMapping("/pedidos/{nomeDoCliente}")
	public Boolean save(@PathVariable("nomeDoCliente") String nomeDoCliente) {
		return pedidosBusiness.salvarPedidos(nomeDoCliente);
	}

	@DeleteMapping("/pedidos/{pedidoid}")
	public Boolean delete(@PathVariable("pedidoid") int pedidoId) {
		return pedidosBusiness.excluirPedidos(pedidoId);
	}

	@PutMapping("/pedidos/{pedidoid}/{nomeDoCliente}")
	public Boolean update(@PathVariable("pedidoid") int pedidoId, @PathVariable("nomeDoCliente") String nomeDoCliente) {
		return pedidosBusiness.atualizarPedidos(pedidoId, nomeDoCliente);
	}
}
