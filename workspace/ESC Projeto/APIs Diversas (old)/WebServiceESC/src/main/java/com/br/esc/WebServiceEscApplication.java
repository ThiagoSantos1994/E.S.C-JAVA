package com.br.esc;

import javax.xml.ws.Endpoint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.br.esc.service.soap.WebServicePedidos;

@SpringBootApplication
public class WebServiceEscApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebServiceEscApplication.class, args);

		/* Publica o serviço Web */
		/*SOAP*/
		WebServicePedidos webServicePedidos = new WebServicePedidos();
		String URL = "http://localhost:8081/pedidos";
		System.out.println("WebServicePedidos rodando: " + URL);
		Endpoint.publish(URL, webServicePedidos);
	}

}
