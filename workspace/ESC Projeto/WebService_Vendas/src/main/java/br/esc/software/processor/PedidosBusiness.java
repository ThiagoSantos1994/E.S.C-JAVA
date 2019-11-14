package br.esc.software.processor;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.esc.software.dao.PedidosDao;
import br.esc.software.exception.InsertException;
import br.esc.software.model.DetalhesPedido;
import br.esc.software.model.Pedido;

@Component("PedidosBusiness")
public class PedidosBusiness {

	@Autowired
	PedidosDao pedidoDao = new PedidosDao();
	static final Logger logger = LoggerFactory.getLogger(PedidosBusiness.class);

	public ArrayList<Pedido> getPedido(int idPedido) {
		if (!validaParametroEntrada(idPedido)) {
			return null;
		}
		return pedidoDao.getPedidoPorID(idPedido);
	}

	public ArrayList<Pedido> getPedidoCliente(int idCliente) {
		if (validaParametroEntrada(idCliente)) {
			return null;
		}
		return pedidoDao.getPedidoPorClienteID(idCliente);
	}

	public ArrayList<DetalhesPedido> getDetalhesPedido(int idPedido) {
		if (validaParametroEntrada(idPedido)) {
			return null;
		}
		return pedidoDao.getDetalhesPedidoID(idPedido);
	}

	public Boolean salvarPedidos(String nomeCliente) throws InsertException {
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

	private boolean validaParametroEntrada(int parametro) {
		if (parametro <= 0) {
			logger.error("Parametro de entrada é inválido! -> Parametro: " + parametro);
			return false;
		}
		return true;
	}
}
