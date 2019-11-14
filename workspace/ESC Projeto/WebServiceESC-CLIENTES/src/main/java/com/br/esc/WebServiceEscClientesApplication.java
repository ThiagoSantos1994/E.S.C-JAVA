package com.br.esc;

import javax.xml.ws.Endpoint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.br.esc.service.soap.WebServiceClientes;

@SpringBootApplication
public class WebServiceEscClientesApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(WebServiceEscClientesApplication.class, args);
		
		System.out.println("Publicando WS REST -> http://localhost:8080/clientes?wsdl");
		
		/*
		 * Este WS sobe os serviços tanto em SOAP quanto em REST, para subir em SOAP é
		 * necessário descomentar a publicação, a aplicação em REST sobe na porta padrão
		 * configurada (8080) e não necessita de EndPoint
		 */	

		/* Publica o serviço Web SOAP */
		WebServiceClientes webServiceClientes = new WebServiceClientes();
		String URL = "http://localhost:8081/clientes";
		System.out.println("webServicePedidosSoap rodando: " + URL);
		Endpoint.publish(URL, webServiceClientes);	
		
	}

}
