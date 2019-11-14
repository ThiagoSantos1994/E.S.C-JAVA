package br.esc.software.model;

import java.util.ArrayList;

public class Pedido {
	public String pedidoID;
	public String funcionarioID;
	public String tipoAtendimento;
	public String mesa;
	public String dataPedido;
	public String dataEntrega;
	public String nomeCliente;
	public String ordemCadastro;
	public String quantidadeVendida;
	public String numeroItem;
	public String dsFormaPagamento;
	public String valorTotal;
	public String valorPago;
	public String valorDesconto;
	public String valorTroco;
	public String clienteID;
	public String motoristaID;
	public String tpPedidoRegistrado;
	public String tpPedidoFinalizado;
	public String tpBaixaEntrega;
	public String dataPedidoRegistro;
	public String telaID;
	public String contaID;
	public String pedidoDiarioID;
	public ArrayList<DetalhesPedido> detalhesPedidos;

	public void setPedidoID(String pedidoID) {
		this.pedidoID = pedidoID;
	}

	public String getPedidoID() {
		return pedidoID;
	}

	public void setFuncionarioID(String funcionarioID) {
		this.funcionarioID = funcionarioID;
	}

	public void setTipoAtendimento(String tipoAtendimento) {
		this.tipoAtendimento = tipoAtendimento;
	}

	public void setMesa(String mesa) {
		this.mesa = mesa;
	}

	public void setDataPedido(String dataPedido) {
		this.dataPedido = dataPedido;
	}

	public void setDataEntrega(String dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public void setOrdemCadastro(String ordemCadastro) {
		this.ordemCadastro = ordemCadastro;
	}

	public void setQuantidadeVendida(String quantidadeVendida) {
		this.quantidadeVendida = quantidadeVendida;
	}

	public void setNumeroItem(String numeroItem) {
		this.numeroItem = numeroItem;
	}

	public void setDsFormaPagamento(String dsFormaPagamento) {
		this.dsFormaPagamento = dsFormaPagamento;
	}

	public void setValorTotal(String valorTotal) {
		this.valorTotal = valorTotal;
	}

	public void setValorPago(String valorPago) {
		this.valorPago = valorPago;
	}

	public void setValorDesconto(String valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public void setValorTroco(String valorTroco) {
		this.valorTroco = valorTroco;
	}

	public void setClienteID(String clienteID) {
		this.clienteID = clienteID;
	}

	public void setMotoristaID(String motoristaID) {
		this.motoristaID = motoristaID;
	}

	public void setTpPedidoRegistrado(String tpPedidoRegistrado) {
		this.tpPedidoRegistrado = tpPedidoRegistrado;
	}

	public void setTpPedidoFinalizado(String tpPedidoFinalizado) {
		this.tpPedidoFinalizado = tpPedidoFinalizado;
	}

	public void setTpBaixaEntrega(String tpBaixaEntrega) {
		this.tpBaixaEntrega = tpBaixaEntrega;
	}

	public void setDataPedidoRegistro(String dataPedidoRegistro) {
		this.dataPedidoRegistro = dataPedidoRegistro;
	}

	public void setTelaID(String telaID) {
		this.telaID = telaID;
	}

	public void setContaID(String contaID) {
		this.contaID = contaID;
	}

	public void setPedidoDiarioID(String pedidoDiarioID) {
		this.pedidoDiarioID = pedidoDiarioID;
	}

	public void setDetalhesPedidos(ArrayList<DetalhesPedido> detalhesPedidos) {
		this.detalhesPedidos = detalhesPedidos;
	}

}
