package br.esc.software;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.esc.software.processor.ListaObjetos;
import br.esc.software.processor.MascararDados;

@SpringBootApplication
public class MascararDadosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MascararDadosApplication.class, args);
		
		MascararDados dados = new MascararDados();
		
		ListaObjetos objetos = new ListaObjetos();
		dados.mascararDados(objetos.getPedidosCadastrados());
	}
}
