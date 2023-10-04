package br.esc.software.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.esc.software.exception.InsertException;
import br.esc.software.global.ConnectionSQL;
import br.esc.software.model.DetalhesPedido;
import br.esc.software.model.Pedido;

@Component("pedidoDao")
public class PedidosDao {

	static final Logger logger = LoggerFactory.getLogger(PedidosDao.class);
	static ConnectionSQL SQL = new ConnectionSQL();
	ResultSet RSAdo;

	public ArrayList<Pedido> getPedidoPorID(int idPedido) {
		logger.debug("getPedidoPorID -> " + idPedido);
		return carregarPedidos("id_Pedido = " + idPedido);
	}

	public ArrayList<Pedido> getPedidoPorClienteID(int idCliente) {
		logger.debug("getPedidoPorClienteID -> " + idCliente);
		return carregarPedidos("id_Cliente = " + idCliente);
	}

	public ArrayList<DetalhesPedido> getDetalhesPedidoID(int idPedido) {
		logger.debug("getDetalhesPedidoID -> " + idPedido);
		return carregarDetalhesPedido("id_Pedido = " + idPedido);
	}

	private ArrayList<Pedido> carregarPedidos(String sWhere) {
		ArrayList<Pedido> listaPedidos = new ArrayList<>();

		try {
			RSAdo = SQL.Select_Table(
					"SELECT id_Pedido,id_Funcionario,ds_TipoAtendimento,nr_Mesa,dt_DataPedido,dt_DataEntrega,ds_NomeCliente,id_OrdemCadastro,nr_QuantidadeVendida,nr_Item,ds_FormaPagamento,vl_Total,vl_Pago,vl_Desconto,vl_Troco,id_Cliente,id_Motorista,tp_PedidoRegistrado,tp_PedidoFinalizado,tp_BaixaEntrega,DataPedidoRegistro,tp_Tela,id_Conta,id_PedidoDiario FROM tbd_Vendas WHERE "
							+ sWhere);
			while (RSAdo.next()) {
				Pedido pedidos = new Pedido();
				pedidos.setPedidoID(RSAdo.getString("id_Pedido"));
				pedidos.setFuncionarioID(RSAdo.getString("id_Funcionario"));
				pedidos.setTipoAtendimento(RSAdo.getString("ds_TipoAtendimento"));
				pedidos.setMesa(RSAdo.getString("nr_Mesa"));
				pedidos.setDataPedido(RSAdo.getString("dt_DataPedido"));
				pedidos.setDataEntrega(RSAdo.getString("dt_DataEntrega"));
				pedidos.setNomeCliente(RSAdo.getString("ds_NomeCliente"));
				pedidos.setOrdemCadastro(RSAdo.getString("id_OrdemCadastro"));
				pedidos.setQuantidadeVendida(RSAdo.getString("nr_QuantidadeVendida"));
				pedidos.setNumeroItem(RSAdo.getString("nr_Item"));
				pedidos.setDsFormaPagamento(RSAdo.getString("ds_FormaPagamento"));
				pedidos.setValorTotal(RSAdo.getString("vl_Total"));
				pedidos.setValorPago(RSAdo.getString("vl_Pago"));
				pedidos.setValorDesconto(RSAdo.getString("vl_Desconto"));
				pedidos.setValorTroco(RSAdo.getString("vl_Troco"));
				pedidos.setClienteID(RSAdo.getString("id_Cliente"));
				pedidos.setMotoristaID(RSAdo.getString("id_Motorista"));
				pedidos.setTpPedidoRegistrado(RSAdo.getString("tp_PedidoRegistrado"));
				pedidos.setTpPedidoFinalizado(RSAdo.getString("tp_PedidoFinalizado"));
				pedidos.setTpBaixaEntrega(RSAdo.getString("tp_BaixaEntrega"));
				pedidos.setDataPedidoRegistro(RSAdo.getString("DataPedidoRegistro"));
				pedidos.setTelaID(RSAdo.getString("tp_Tela"));
				pedidos.setContaID(RSAdo.getString("id_Conta"));
				pedidos.setPedidoDiarioID(RSAdo.getString("id_PedidoDiario"));

				// Injeta na lista principal a lista de detalhes do pedido

				pedidos.detalhesPedidos = this.carregarDetalhesPedido(sWhere);
				listaPedidos.add(pedidos);
				logger.debug("Carregando Pedido Nº: " + pedidos.getPedidoID());
			}
			RSAdo.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listaPedidos;
	}

	private ArrayList<DetalhesPedido> carregarDetalhesPedido(String sWhere) {
		int iContadorRegistros = 0;
		ArrayList<DetalhesPedido> listaDetalhesPedidos = new ArrayList<>();

		try {
			RSAdo = SQL.Select_Table(
					"SELECT id_Funcionario ,nr_QuantidadeVendida ,nr_Item,ds_NomeMercadoria,vl_PrecoVenda,id_CodigoBarras,id_Pedido,vl_Unitario,tp_PedidoRegistrado,tp_PedidoFinalizado,DataPedidoRegistro,tp_Tela,id_OrdemCadastrada,nr_Mesa,id_PedidoDiario,id_Maquina,nr_QuantidadeQuilo,vl_TaxaDesconto FROM tbd_RegistroVendas WHERE "
							+ sWhere + " ORDER BY nr_Item ASC");
			while (RSAdo.next()) {
				DetalhesPedido detalhesPedidos = new DetalhesPedido();
				detalhesPedidos.setId_Funcionario(RSAdo.getString("id_Funcionario"));
				detalhesPedidos.setNr_Item(RSAdo.getString("nr_Item"));
				detalhesPedidos.setDs_NomeMercadoria(RSAdo.getString("ds_NomeMercadoria"));
				detalhesPedidos.setVl_PrecoVenda(RSAdo.getString("vl_PrecoVenda"));
				detalhesPedidos.setId_CodigoBarras(RSAdo.getString("id_CodigoBarras"));
				detalhesPedidos.setId_Pedido(RSAdo.getString("id_Pedido"));
				detalhesPedidos.setVl_Unitario(RSAdo.getString("vl_Unitario"));
				detalhesPedidos.setTp_PedidoRegistrado(RSAdo.getString("tp_PedidoRegistrado"));
				detalhesPedidos.setTp_PedidoFinalizado(RSAdo.getString("tp_PedidoFinalizado"));
				detalhesPedidos.setDataPedidoRegistro(RSAdo.getString("DataPedidoRegistro"));
				detalhesPedidos.setTp_Tela(RSAdo.getString("tp_Tela"));
				detalhesPedidos.setId_OrdemCadastrada(RSAdo.getString("id_OrdemCadastrada"));
				detalhesPedidos.setNr_Mesa(RSAdo.getString("nr_Mesa"));
				detalhesPedidos.setId_PedidoDiario(RSAdo.getString("id_PedidoDiario"));
				detalhesPedidos.setId_Maquina(RSAdo.getString("id_Maquina"));
				detalhesPedidos.setNr_QuantidadeQuilo(RSAdo.getString("nr_QuantidadeQuilo"));
				detalhesPedidos.setVl_TaxaDesconto(RSAdo.getString("vl_TaxaDesconto"));
				listaDetalhesPedidos.add(detalhesPedidos);
				iContadorRegistros++;
			}
//			RSAdo.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		logger.debug(
				"Carregando Detalhes do Pedido com parametros: " + sWhere + " Total de itens-> " + iContadorRegistros);
		return listaDetalhesPedidos;
	}

	public Boolean salvarPedidos(String nomeCliente) throws InsertException {
		try {
			long id_Pedido = this.getPedidoID();
			SQL.Insert_Table("INSERT INTO tbd_Vendas(id_Pedido,ds_NomeCliente) VALUES (" + id_Pedido + ",'" + nomeCliente + "')");
			logger.info("Pedido gravado com sucesso, Nº " + id_Pedido);
			return true;
		} catch (Exception e) {
			throw new InsertException("Ocorreu um erro durante a gravação do pedido", e);
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

	private int getPedidoID() throws SQLException {
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
