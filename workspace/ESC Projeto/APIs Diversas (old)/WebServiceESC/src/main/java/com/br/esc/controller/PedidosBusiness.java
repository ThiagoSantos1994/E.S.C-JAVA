package com.br.esc.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.br.esc.dao.PedidoDao;
import com.br.esc.model.PedidosPorID;

@Component("PedidosBusiness")
public class PedidosBusiness {

	@Autowired
	PedidoDao pedidoDao = new PedidoDao();

	public ArrayList<PedidosPorID> buscarPedidosPorId(int id_Pedido) {
		if (id_Pedido > 0) {
			return pedidoDao.buscarPedidosPorId(id_Pedido);
		} else {
			return pedidoDao.buscarPedidosTodos();
		}
	}

	public Boolean salvarPedidos(String nomeCliente) {
		if (null != nomeCliente) {
			if (pedidoDao.salvarPedidos(nomeCliente)) {
				System.out.println("Pedido gravado com sucesso!");
				return true;
			} else {
				System.out.println("Ocorreu um erro ao executar o metodo salvarPedidos.");
				return false;
			}
		}
		System.out.println("Necessário inserir o nome do cliente!");
		return false;
	}

	public Boolean atualizarPedidos(int idPedido, String nomeCliente) {
		if (idPedido > 0 && null != nomeCliente) {
			if (pedidoDao.atualizarPedidos(idPedido, nomeCliente)) {
				System.out.println("Pedido " + idPedido + " atualizado com sucesso!");
				return true;
			} else {
				System.out.println("Ocorreu um erro ao executar o metodo atualizarPedidos.");
				return false;
			}
		}
		System.out.println("O numero do pedido e nome do cliente não devem ser vazios.");
		return false;
	}

	public Boolean excluirPedidos(int idPedido) {
		if (idPedido > 0) {
			if (pedidoDao.excluirPedidos(idPedido)) {
				System.out.println("Pedido " + idPedido + " excluído com sucesso!");
				return true;
			} else {
				System.out.println("Ocorreu um erro ao executar o metodo excluirPedidos.");
				return false;
			}
		}
		System.out.println("O numero do pedido deve ser maior que zero.");
		return false;
	}

}
