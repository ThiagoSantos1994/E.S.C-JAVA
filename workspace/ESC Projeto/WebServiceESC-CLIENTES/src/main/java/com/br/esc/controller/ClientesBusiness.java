package com.br.esc.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.br.esc.dao.ClientesDao;
import com.br.esc.model.Clientes;

@Component("ClientesBusiness")
public class ClientesBusiness {

	@Autowired
	ClientesDao clientesDao = new ClientesDao();

	public ArrayList<Clientes> buscarClientesPorId(int idCliente) {
		if (idCliente > 0) {
			return clientesDao.buscarClientesPorID(idCliente);
		} else {
			return clientesDao.buscarClientesTodos();
		}
	}

	public ArrayList<Clientes> buscarClientesPorTelefone(String nrTelefone) {
		if(nrTelefone.equals("")) {
			return null;
		}
		return clientesDao.buscarClientesPorTelefone(nrTelefone);
	}

	public Boolean salvarClientes(String nomeCliente) {
		if (null != nomeCliente) {
			if (clientesDao.salvarClientes(nomeCliente)) {
				System.out.println("Cliente gravado com sucesso!");
				return true;
			} else {
				System.out.println("Ocorreu um erro ao executar o metodo salvarPedidos.");
				return false;
			}
		}
		System.out.println("Necessário inserir o nome do cliente!");
		return false;
	}

	public Boolean atualizaClientes(int idCLiente, String nomeCliente) {
		if (idCLiente > 0 && null != nomeCliente) {
			if (clientesDao.atualizarClientes(idCLiente, nomeCliente)) {
				System.out.println("Cliente Nº " + idCLiente + " atualizado com sucesso!");
				return true;
			} else {
				System.out.println("Ocorreu um erro ao executar o metodo atualizaClientes.");
				return false;
			}
		}
		System.out.println("O numero de identificacao do cliente e nome do cliente não devem ser vazios.");
		return false;
	}

	public Boolean excluirClientes(int idCliente) {
		if (idCliente > 0) {
			if (clientesDao.excluirClientes(idCliente)) {
				System.out.println("Cliente Nº " + idCliente + " excluído com sucesso!");
				return true;
			} else {
				System.out.println("Ocorreu um erro ao executar o metodo excluirPedidos.");
				return false;
			}
		}
		System.out.println("O numero de identificacao do cliente deve ser maior que zero.");
		return false;
	}

}
