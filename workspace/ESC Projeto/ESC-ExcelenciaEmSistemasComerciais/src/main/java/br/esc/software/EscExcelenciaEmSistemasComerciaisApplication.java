package br.esc.software;

import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.esc.software.commons.ConnectionSQL;
import br.esc.software.commons.Global;
import br.esc.software.exceptions.ExcecaoGlobal;

@SpringBootApplication
public class EscExcelenciaEmSistemasComerciaisApplication {
	
	/*Alterado para API em 29/06/2020*/
	
	public static void main(String[] args) throws ExcecaoGlobal, SQLException {
		SpringApplication.run(EscExcelenciaEmSistemasComerciaisApplication.class, args);
		
//		try {
//			ConnectionSQL connectionSQL = new ConnectionSQL();
//			connectionSQL.abrirConexao();
//		} catch (ExcecaoGlobal e) {
//			Global.LogErro("Ocorreu um erro durante a inicializacao da API -> " + e);
//			System.exit(0);
//		}
	}
}
