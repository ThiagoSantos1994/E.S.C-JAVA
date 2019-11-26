package br.esc.software;

import static br.esc.software.global.Global.DataAtual;
import static br.esc.software.global.Global.LogErro;
import static br.esc.software.global.Global.LogInfo;
import static java.lang.System.exit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.esc.software.service.OrquestradorServicos;

@SpringBootApplication
public class EscExcelenciaEmSistemasComerciaisApplication {
	/**
	 * SISTEMA ESC JAVA - Modais Externos
	 * @author Thiago Santos
	 * @since 06/2019
	 * @Customized 22/11/2019
	 */

	public static void main(String[] args) {
		SpringApplication.run(EscExcelenciaEmSistemasComerciaisApplication.class, args);
		try {
			if (args[0].isEmpty()) {/*lança um IndexOutOfBoundsException*/}

			OrquestradorServicos servicos = new OrquestradorServicos(args);
			servicos.inicializacao();
			
		} catch (IndexOutOfBoundsException e) {
			LogErro("Necessario informar algum parametro de entrada no orquestrador");
		} finally {
			LogInfo("Encerrando aplicacao SPRING :) -> " + DataAtual());
			exit(0);
		}
	}
}
