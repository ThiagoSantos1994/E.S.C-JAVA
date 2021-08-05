package br.esc.software;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.esc.software.global.ConnectionSQL;

@SpringBootApplication
public class WebServiceVendasApplication {

	static final Logger logger = LoggerFactory.getLogger(WebServiceVendasApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(WebServiceVendasApplication.class, args);
		
		ConnectionSQL connectionSQL = new ConnectionSQL();
		if (connectionSQL.abrirConexao()) {
			logger.info("Conexao SQL realizada com sucesso!");
		};
	}

}
