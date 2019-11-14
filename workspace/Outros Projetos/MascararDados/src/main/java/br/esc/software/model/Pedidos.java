package br.esc.software.model;

public class Pedidos {

	public int idPedido;
	public String nomeProduto;
	public String nomeCliente;
	public String enderecoCliente;
	public int qtProdutosVendidos;

	public int getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(int idPedido) {
		this.idPedido = idPedido;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getEnderecoCliente() {
		return enderecoCliente;
	}

	public void setEnderecoCliente(String enderecoCliente) {
		this.enderecoCliente = enderecoCliente;
	}

	public int getQtProdutosVendidos() {
		return qtProdutosVendidos;
	}

	public void setQtProdutosVendidos(int qtProdutosVendidos) {
		this.qtProdutosVendidos = qtProdutosVendidos;
	}

}
