package br.esc.software.rs;

import static br.esc.software.commons.Global.LogInfo;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.esc.software.commons.ConnectionSQL;
import br.esc.software.integration.ExportadorSQL;

@RestController
@RequestMapping("/api")
public class GerarScriptApi {
	
	ExportadorSQL exportador = new ExportadorSQL();
	ConnectionSQL connection = new ConnectionSQL();
	
	private StringBuffer response = null;
	
	@GetMapping(path = "/obter-script-create", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StringBuffer> gerarScriptCreate() throws Exception {

		LogInfo("Inicializando api obter-script-insert");
		
		connection.abrirConexao();
		
		response = exportador.montaScriptImplantacao();
		
		LogInfo("obter-script-insert gerado com sucesso!");
		
		return new ResponseEntity<StringBuffer>(response, HttpStatus.OK);
	}
}
