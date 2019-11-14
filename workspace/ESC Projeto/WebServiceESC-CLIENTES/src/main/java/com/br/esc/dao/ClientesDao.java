package com.br.esc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.br.esc.model.Clientes;

@Component("ClientesDao")
public class ClientesDao {

	static ConexaoSQL SQL = new ConexaoSQL();
	ResultSet RSAdo;

	public ArrayList<Clientes> buscarClientesTodos() {
		return carregarClientes("id_Cliente > 0");
	}

	public ArrayList<Clientes> buscarClientesPorID(int idCliente) {
		return carregarClientes("id_Cliente = " + idCliente);
	}

	public ArrayList<Clientes> buscarClientesPorTelefone(String sTelefone) {
		return carregarClientes("nr_Tel1 = '" + sTelefone + "'");
	}

	private ArrayList<Clientes> carregarClientes(String sWhere) {
		int iContadorRegistros = 0;
		String sWhereParametro = "";
		ArrayList<Clientes> listaClientes = new ArrayList<>();
		
		if (sWhere == "") {
			sWhereParametro = "id_Cliente > 0";
		} else {
			sWhereParametro = sWhere;
		}

		try {
			RSAdo = SQL.Select_Table(
					"SELECT id_Cliente,ds_Nome,ds_Endereco,ds_Bairro,ds_Cidade,nr_Tel1,nr_Tel2,nr_Tel3,nr_Tel4,nr_RG,nr_CPF,nr_CNPJ,ds_Email,ds_Observacoes,dt_Cadastro,ds_UsuarioCadastro,ds_UsuarioAlteracao,nr_Numero,ds_EnderecoComplemento,ds_PontoReferencia,tp_ClienteBloqueado,ds_MotivoBloqueio,tp_SinalIndisponivel,dt_DataNascimento,tp_HabilitaCupom,vl_PorcentagemCupom FROM tbd_CadastroClientes WHERE "
							+ sWhereParametro + " ORDER BY id_Cliente ASC");
			while (RSAdo.next()) {
				Clientes Clientes = new Clientes();
				Clientes.clienteID = RSAdo.getString("id_Cliente");
				Clientes.nome = RSAdo.getString("ds_Nome");
				Clientes.endereco = RSAdo.getString("ds_Endereco");
				Clientes.bairro = RSAdo.getString("ds_Bairro");
				Clientes.cidade = RSAdo.getString("ds_Cidade");
				Clientes.tel1 = RSAdo.getString("nr_Tel1");
				Clientes.tel2 = RSAdo.getString("nr_Tel2");
				Clientes.tel3 = RSAdo.getString("nr_Tel3");
				Clientes.tel4 = RSAdo.getString("nr_Tel4");
				Clientes.rg = RSAdo.getString("nr_RG");
				Clientes.cpf = RSAdo.getString("nr_CPF");
				Clientes.cnpj = RSAdo.getString("nr_CNPJ");
				Clientes.email = RSAdo.getString("ds_Email");
				Clientes.observacoes = RSAdo.getString("ds_Observacoes");
				Clientes.dtCadastro = RSAdo.getString("dt_Cadastro");
				Clientes.usuarioCadastro = RSAdo.getString("ds_UsuarioCadastro");
				Clientes.usuarioAlteracao = RSAdo.getString("ds_UsuarioAlteracao");
				Clientes.numero = RSAdo.getString("nr_Numero");
				Clientes.enderecoComplemento = RSAdo.getString("ds_EnderecoComplemento");
				Clientes.pontoReferencia = RSAdo.getString("ds_PontoReferencia");
				Clientes.tpClienteBloqueado = RSAdo.getString("tp_ClienteBloqueado");
				Clientes.motivoBloqueio = RSAdo.getString("ds_MotivoBloqueio");
				Clientes.tpSinalIndisponivel = RSAdo.getString("tp_SinalIndisponivel");
				Clientes.dataNascimento = RSAdo.getString("dt_DataNascimento");
				Clientes.tpHabilitaCupom = RSAdo.getString("tp_HabilitaCupom");
				Clientes.vlPorcentagemCupom = RSAdo.getString("vl_PorcentagemCupom");
				listaClientes.add(Clientes);
				iContadorRegistros++;
			}
			RSAdo.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Response CLIENTES - " + iContadorRegistros +  " Retorno(s) para a consulta " + sWhereParametro);
		
		if(iContadorRegistros == 0) {
			return null;
		}
		return listaClientes;
	}

	public Boolean salvarClientes(String nomeCliente) {
		try {
			int id_Cliente = getIdClientes("Clientes");
			if (SQL.Insert_Table("INSERT INTO tbd_CadastroClientes(id_Cliente,ds_Nome) VALUES (" + id_Cliente + ",'"
					+ nomeCliente + "')")) {
				System.out.println("Cliente Numero: " + id_Cliente);
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Boolean excluirClientes(int idCliente) {
		try {
			SQL.Insert_Table("DELETE FROM tbd_CadastroClientes WHERE id_Cliente = " + idCliente);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Boolean atualizarClientes(int idCliente, String nomeCliente) {
		try {
			SQL.Insert_Table(
					"UPDATE tbd_CadastroClientes SET ds_Nome = '" + nomeCliente + "' WHERE id_Cliente = " + idCliente);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private int getIdClientes(String tp_RegistroKey) throws SQLException {
		int id_ChaveKey = 1;

		RSAdo = SQL.Select_Table(
				"SELECT id_SequenciaKey FROM tbd_ChavePrimaria WHERE tp_RegistroKey = '" + tp_RegistroKey + "'");
		if (RSAdo.next()) {
			id_ChaveKey += RSAdo.getInt("id_SequenciaKey");
		}
		RSAdo.close();

		SQL.Update_Table("UPDATE tbd_ChavePrimaria SET id_SequenciaKey = " + id_ChaveKey + " WHERE tp_RegistroKey = '"
				+ tp_RegistroKey + "'");
		return id_ChaveKey;
	}

}
