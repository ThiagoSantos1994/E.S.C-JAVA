package br.esc.software.processor;

import java.util.ArrayList;

import br.esc.software.model.Pedidos;

public class ListaObjetos {
	
	public ArrayList<Pedidos> getPedidosCadastrados(){
		Pedidos pedidos1 = new Pedidos();
		pedidos1.setIdPedido(1);
		pedidos1.setNomeProduto("Arroz Camil TP1");
		pedidos1.setQtProdutosVendidos(2);
		pedidos1.setNomeCliente("Thiago do Nascimento Santos");
		pedidos1.setEnderecoCliente("Rua das Petúnias");
		
		ArrayList<Pedidos> listPedidos = new ArrayList<Pedidos>();
		listPedidos.add(pedidos1);
		return listPedidos;
	}
}
