package com.br.esc.service.rest;

import java.util.ArrayList;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.esc.controller.ClientesBusiness;
import com.br.esc.model.Clientes;
import com.br.esc.model.Erro;

@RestController
public class WebServiceClientesRest {

	@Autowired
	public ClientesBusiness clientesBusiness;
	Erro tratarErros = new Erro();

	@GetMapping("/clientes")
	public ArrayList<Clientes> clientes() {
		return clientesBusiness.buscarClientesPorId(0);
	}

	@GetMapping("/clientes/{idCliente}")
	public Clientes buscarClientesPorId(@PathVariable("idCliente") int idCliente) {
		ArrayList<Clientes> clientes = clientesBusiness.buscarClientesPorId(idCliente);
		return clientes.get(0);
	}

	@GetMapping("/clientes/telefone/{nr_Telefone}")
	public Clientes buscarClientesPorTelefone(@PathVariable("nr_Telefone") String nr_Telefone) {
		ArrayList<Clientes> clientes = clientesBusiness.buscarClientesPorTelefone(nr_Telefone);
		return clientes.get(0);
//		if (clientes != null && !clientes.isEmpty()) {
//			return Response.status(Response.Status.OK).entity(clientes).build().getEntity();
//		} else {
//			tratarErros.setErro("Nenhum registro encontrado");
//			tratarErros.setCodigo("404");
//			return Response.status(Response.Status.NOT_FOUND).entity(tratarErros).build().getEntity();
//		}
	}

	@PostMapping("/clientes/{nomeCliente}")
	public Boolean salvarClientes(@PathVariable("nomeCliente") String nomeCliente) {
		return clientesBusiness.salvarClientes(nomeCliente);
	}

	@PutMapping("/clientes/{idCLiente}/{nomeCliente}")
	public Boolean atualizaClientes(@PathVariable("idCLiente") int idCLiente,
			@PathVariable("nomeCliente") String nomeCliente) {
		return clientesBusiness.atualizaClientes(idCLiente, nomeCliente);
	}

	@DeleteMapping("/clientes/{idCLiente}")
	@NotEmpty(message = "Necessário Preencher o ID do Cliente")
	public Boolean excluirClientes(@PathVariable("idCLiente") int idCliente) {
		return clientesBusiness.excluirClientes(idCliente);
	}
}
