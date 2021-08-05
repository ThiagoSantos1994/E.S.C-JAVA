package com.br.esc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientWebServiceEscApplication {

	/* Projeto Criado para Estudos - Rest - 15/04/2019 ~29/04/2019 */
	public static void main(String[] args) {
		SpringApplication.run(ClientWebServiceEscApplication.class, args);
		System.out.println("Inicializando Servidor Client na porta -> http://localhost:8099/ws");
		System.out.println("http://localhost:8099/ws/pedidos/{idPedido}");
		System.out.println("http://localhost:8099/ws/pedidos/cliente/{nrTelefone}");
	}

}
