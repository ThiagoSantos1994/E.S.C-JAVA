package br.esc.software;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestesJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestesJavaApplication.class, args);
		
		TESTES testes = new TESTES();
		testes.TestesGerais();
	}

}
