package br.esc.software.rs;

import static br.esc.software.commons.Global.LogInfo;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.esc.software.business.ExportadorSQLBusiness;
import br.esc.software.commons.ConnectionSQL;
import br.esc.software.exceptions.ExcecaoGlobal;

@RestController
@RequestMapping("/api")
public class ExportadorApi {

	@Autowired
	ExportadorSQLBusiness exportador;
	ConnectionSQL connection = new ConnectionSQL();

	@PostMapping(path = "/exportar-arquivos-sql", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> iniciarExportacao() throws SQLException, ExcecaoGlobal {

		LogInfo("Inicializando exportação do arquivo SQL");

		connection.abrirConexao();

		String response = exportador.iniciarExportacao();

		LogInfo("Exportação do arquivo SQL realizada com sucesso!");
		
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}
}
