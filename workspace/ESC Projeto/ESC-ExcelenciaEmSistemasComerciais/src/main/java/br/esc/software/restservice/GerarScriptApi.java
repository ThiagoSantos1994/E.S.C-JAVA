package br.esc.software.restservice;

import static br.esc.software.commons.GlobalUtils.LogInfo;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.esc.software.configuration.ConnectionSQL;
import br.esc.software.integration.ExportadorSQLImpl;

@RestController
@RequestMapping("/api")
public class GerarScriptApi {
	
	ExportadorSQLImpl exportador = new ExportadorSQLImpl();
	ConnectionSQL connection = new ConnectionSQL();
	
	private StringBuffer response = null;
	
	@GetMapping(path = "/obter-script-create", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StringBuffer> gerarScriptCreate() throws Exception {

		LogInfo("<<INICIO>> Inicializando API obter-script-insert");
		
		connection.abrirConexao();
		
		response = exportador.montaScriptImplantacao();
		
		connection.fecharConexao();
		
		LogInfo("<<FIM>> obter-script-insert gerado com sucesso!");
		
		return new ResponseEntity<StringBuffer>(response, HttpStatus.OK);
	}
}
