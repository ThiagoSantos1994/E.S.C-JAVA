package com.br.esc.service.soap;

import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;

import com.br.esc.controller.ClientesBusiness;
import com.br.esc.model.Clientes;

@WebService
public class WebServiceClientes {

	@Autowired
	public ClientesBusiness clientesBusiness = new ClientesBusiness();

	@WebMethod
	public ArrayList<Clientes> buscarClientesPorId(int idCliente) {
		return clientesBusiness.buscarClientesPorId(idCliente);
	}

	@WebMethod
	public Clientes buscarClientesPorId(String nr_Telefone) {
		ArrayList<Clientes> Clientes = clientesBusiness.buscarClientesPorTelefone(nr_Telefone);
		return Clientes.get(0);
	}

	@WebMethod
	public Boolean salvarClientes(String nomeCliente) {
		return clientesBusiness.salvarClientes(nomeCliente);
	}

	@WebMethod
	public Boolean atualizaClientes(int idCLiente, String nomeCliente) {
		return clientesBusiness.atualizaClientes(idCLiente, nomeCliente);
	}

	@WebMethod
	public Boolean excluirClientes(int idCliente) {
		return clientesBusiness.excluirClientes(idCliente);
	}
}
