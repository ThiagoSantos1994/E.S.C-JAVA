package br.esc.software.processor;

import java.util.ArrayList;

import br.esc.software.model.Pedidos;

public class MascararDados {

	public ArrayList<Pedidos> mascararDados(ArrayList<Pedidos> listaPedidos) {
		ArrayList<Pedidos> pedidosMascarado = new ArrayList<Pedidos>();
		
		for (Pedidos pedidos : listaPedidos) {
			Pedidos pedido = new Pedidos();
			pedido.setNomeCliente(aplicarMascara(pedidos.getNomeCliente()));
			pedido.setEnderecoCliente(aplicarMascara(pedidos.getEnderecoCliente()));
			pedido.setNomeProduto(aplicarMascara(pedidos.getNomeProduto()));
			pedidosMascarado.add(pedido);
		}
		return pedidosMascarado;
	}

	private String aplicarMascara(String valor) {
		String stringOriginal = valor;
		String stringMascarada = "";
		
		try {
			if (null != stringOriginal) {
				char[] stringChar = stringOriginal.toCharArray();
				int iTamanhoString = valor.length() / 2;

				for (int i = 0; i < stringChar.length; i++) {
					if (i >= iTamanhoString) {
						if (stringChar[i] != ' ') {
							stringChar[i] = '*';
						}
					}
					stringMascarada = stringMascarada + stringChar[i];
				}
			}
		} catch (Exception e) {
			return stringOriginal;
		}
		return stringMascarada;
	}
}
