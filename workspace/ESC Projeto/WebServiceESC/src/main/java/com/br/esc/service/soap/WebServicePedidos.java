package com.br.esc.service.soap;

import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import org.springframework.beans.factory.annotation.Autowired;

import com.br.esc.controller.PedidosBusiness;
import com.br.esc.model.PedidosPorID;

@WebService
public class WebServicePedidos {

	@Autowired
	public PedidosBusiness pedidosBusiness = new PedidosBusiness();

	@WebMethod
	public ArrayList<PedidosPorID> buscarPedidos(@XmlElement(name = "pedidoId", required = true, nillable = false) int pedidoId) {
		return pedidosBusiness.buscarPedidosPorId(pedidoId);
	}

	public void salvarPedidos(String nomeCliente) {
		pedidosBusiness.salvarPedidos(nomeCliente);
	}

	public void atualizarPedidos(int idPedido, String nomeCliente) {
		pedidosBusiness.atualizarPedidos(idPedido, nomeCliente);
	}

	public void excluirPedidos(int pedidoId) {
		pedidosBusiness.excluirPedidos(pedidoId);
	}
}
