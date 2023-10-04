
package com.br.esc.model;

import java.util.List;

public class Pedidos {

	private String pedidoID;
	private String funcionarioID;
	private String tipoAtendimento;
	private String mesa;
	private String dataPedido;
	private String dataEntrega;
	private String nomeCliente;
	private String ordemCadastro;
	private String quantidadeVendida;
	private String numero_Item;
	private String dsFormaPagamento;
	private String valorTotal;
	private String valorPago;
	private String valorDesconto;
	private String valorTroco;
	private String clienteID;
	private String motoristaID;
	private String tpPedidoRegistrado;
	private String tpPedidoFinalizado;
	private String tpBaixaEntrega;
	private String dataPedidoRegistro;
	private String telaID;
	private String contaID;
	private String pedidoDiarioID;

	private Cliente cliente;
	private List<DetalhePedidos> detalhePedidos;

	public List<DetalhePedidos> getDetalhePedidos() {
		return detalhePedidos;
	}

	public void setDetalhePedidos(List<DetalhePedidos> detalhePedidos) {
		this.detalhePedidos = detalhePedidos;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getPedidoID() {
		return pedidoID;
	}

	public void setPedidoID(String pedidoID) {
		this.pedidoID = pedidoID;
	}

	public String getFuncionarioID() {
		return funcionarioID;
	}

	public void setFuncionarioID(String funcionarioID) {
		this.funcionarioID = funcionarioID;
	}

	public String getTipoAtendimento() {
		return tipoAtendimento;
	}

	public void setTipoAtendimento(String tipoAtendimento) {
		this.tipoAtendimento = tipoAtendimento;
	}

	public String getMesa() {
		return mesa;
	}

	public void setMesa(String mesa) {
		this.mesa = mesa;
	}

	public String getDataPedido() {
		return dataPedido;
	}

	public void setDataPedido(String dataPedido) {
		this.dataPedido = dataPedido;
	}

	public String getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(String dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getOrdemCadastro() {
		return ordemCadastro;
	}

	public void setOrdemCadastro(String ordemCadastro) {
		this.ordemCadastro = ordemCadastro;
	}

	public String getQuantidadeVendida() {
		return quantidadeVendida;
	}

	public void setQuantidadeVendida(String quantidadeVendida) {
		this.quantidadeVendida = quantidadeVendida;
	}

	public String getNumero_Item() {
		return numero_Item;
	}

	public void setNumero_Item(String numero_Item) {
		this.numero_Item = numero_Item;
	}

	public String getDsFormaPagamento() {
		return dsFormaPagamento;
	}

	public void setDsFormaPagamento(String dsFormaPagamento) {
		this.dsFormaPagamento = dsFormaPagamento;
	}

	public String getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(String valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getValorPago() {
		return valorPago;
	}

	public void setValorPago(String valorPago) {
		this.valorPago = valorPago;
	}

	public String getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(String valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public String getValorTroco() {
		return valorTroco;
	}

	public void setValorTroco(String valorTroco) {
		this.valorTroco = valorTroco;
	}

	public String getClienteID() {
		return clienteID;
	}

	public void setClienteID(String clienteID) {
		this.clienteID = clienteID;
	}

	public String getMotoristaID() {
		return motoristaID;
	}

	public void setMotoristaID(String motoristaID) {
		this.motoristaID = motoristaID;
	}

	public String getTpPedidoRegistrado() {
		return tpPedidoRegistrado;
	}

	public void setTpPedidoRegistrado(String tpPedidoRegistrado) {
		this.tpPedidoRegistrado = tpPedidoRegistrado;
	}

	public String getTpPedidoFinalizado() {
		return tpPedidoFinalizado;
	}

	public void setTpPedidoFinalizado(String tpPedidoFinalizado) {
		this.tpPedidoFinalizado = tpPedidoFinalizado;
	}

	public String getTpBaixaEntrega() {
		return tpBaixaEntrega;
	}

	public void setTpBaixaEntrega(String tpBaixaEntrega) {
		this.tpBaixaEntrega = tpBaixaEntrega;
	}

	public String getDataPedidoRegistro() {
		return dataPedidoRegistro;
	}

	public void setDataPedidoRegistro(String dataPedidoRegistro) {
		this.dataPedidoRegistro = dataPedidoRegistro;
	}

	public String getTelaID() {
		return telaID;
	}

	public void setTelaID(String telaID) {
		this.telaID = telaID;
	}

	public String getContaID() {
		return contaID;
	}

	public void setContaID(String contaID) {
		this.contaID = contaID;
	}

	public String getPedidoDiarioID() {
		return pedidoDiarioID;
	}

	public void setPedidoDiarioID(String pedidoDiarioID) {
		this.pedidoDiarioID = pedidoDiarioID;
	}

}
