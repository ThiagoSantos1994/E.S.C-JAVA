package com.br.esc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.br.esc.model.PedidosPorID;

@Component("pedidoDao")
public class PedidoDao {

	static ConexaoSQL SQL = new ConexaoSQL();
	ResultSet RSAdo;

	public ArrayList<PedidosPorID> buscarPedidosPorId(int id_Pedido) {
		String sWhere = "";
		ArrayList<PedidosPorID> listaPedidos = new ArrayList<>();

		if (id_Pedido == 0) {
			sWhere = "id_Pedido > 0";
		} else {
			sWhere = "id_Pedido = " + id_Pedido;
		}

		try {
			RSAdo = SQL.Select_Table(
					"SELECT id_Pedido,id_Funcionario,ds_TipoAtendimento,nr_Mesa,dt_DataPedido,dt_DataEntrega,ds_NomeCliente,id_OrdemCadastro,nr_QuantidadeVendida,nr_Item,ds_FormaPagamento,vl_Total,vl_Pago,vl_Desconto,vl_Troco,id_Cliente,id_Motorista,tp_PedidoRegistrado,tp_PedidoFinalizado,tp_BaixaEntrega,DataPedidoRegistro,tp_Tela,id_Conta,id_PedidoDiario FROM tbd_Vendas WHERE "
							+ sWhere + " ORDER BY id_Pedido ASC");
			while (RSAdo.next()) {
				PedidosPorID pedidosPorID = new PedidosPorID();
				pedidosPorID.pedidoID = RSAdo.getString("id_Pedido");
				pedidosPorID.funcionarioID = RSAdo.getString("id_Funcionario");
				pedidosPorID.tipoAtendimento = RSAdo.getString("ds_TipoAtendimento");
				pedidosPorID.mesa = RSAdo.getString("nr_Mesa");
				pedidosPorID.dataPedido = RSAdo.getString("dt_DataPedido");
				pedidosPorID.dataEntrega = RSAdo.getString("dt_DataEntrega");
				pedidosPorID.nomeCliente = RSAdo.getString("ds_NomeCliente");
				pedidosPorID.ordemCadastro = RSAdo.getString("id_OrdemCadastro");
				pedidosPorID.quantidadeVendida = RSAdo.getString("nr_QuantidadeVendida");
				pedidosPorID.numero_Item = RSAdo.getString("nr_Item");
				pedidosPorID.dsFormaPagamento = RSAdo.getString("ds_FormaPagamento");
				pedidosPorID.valorTotal = RSAdo.getString("vl_Total");
				pedidosPorID.valorPago = RSAdo.getString("vl_Pago");
				pedidosPorID.valorDesconto = RSAdo.getString("vl_Desconto");
				pedidosPorID.valorTroco = RSAdo.getString("vl_Troco");
				pedidosPorID.clienteID = RSAdo.getString("id_Cliente");
				pedidosPorID.motoristaID = RSAdo.getString("id_Motorista");
				pedidosPorID.tpPedidoRegistrado = RSAdo.getString("tp_PedidoRegistrado");
				pedidosPorID.tpPedidoFinalizado = RSAdo.getString("tp_PedidoFinalizado");
				pedidosPorID.tpBaixaEntrega = RSAdo.getString("tp_BaixaEntrega");
				pedidosPorID.dataPedidoRegistro = RSAdo.getString("DataPedidoRegistro");
				pedidosPorID.telaID = RSAdo.getString("tp_Tela");
				pedidosPorID.contaID = RSAdo.getString("id_Conta");
				pedidosPorID.pedidoDiarioID = RSAdo.getString("id_PedidoDiario");
				listaPedidos.add(pedidosPorID);
			}
			RSAdo.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listaPedidos;
	}

	public ArrayList<PedidosPorID> buscarPedidosTodos() {
//		Busca todos os pedidos existentes na base 
		return buscarPedidosPorId(0);
	}

	public Boolean salvarPedidos(String nomeCliente) {
		try {
			int id_Pedido = getIdPedido();
			if (SQL.Insert_Table("INSERT INTO tbd_Vendas(id_Pedido,ds_NomeCliente) VALUES (" + getIdPedido() + ",'"
					+ nomeCliente + "')")) {
				System.out.println("Pedido Numero: " + id_Pedido);
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Boolean excluirPedidos(int idPedido) {
		try {
			SQL.Insert_Table("DELETE FROM tbd_Vendas WHERE id_Pedido = " + idPedido);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Boolean atualizarPedidos(int idPedido, String nomeCliente) {
		try {
			SQL.Insert_Table(
					"UPDATE tbd_Vendas SET ds_NomeCliente = '" + nomeCliente + "' WHERE id_Pedido = " + idPedido);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private int getIdPedido() throws SQLException {
		int id_PedidoKey = 1;

		RSAdo = SQL.Select_Table("SELECT id_SequenciaKey FROM tbd_ChavePrimaria WHERE tp_RegistroKey = 'VENDAS'");
		if (RSAdo.next()) {
			id_PedidoKey += RSAdo.getInt("id_SequenciaKey");
		}
		RSAdo.close();

		SQL.Update_Table(
				"UPDATE tbd_ChavePrimaria SET id_SequenciaKey = " + id_PedidoKey + " WHERE tp_RegistroKey = 'VENDAS'");
		return id_PedidoKey;
	}

}
