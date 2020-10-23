package br.esc.software.restservice;

import javax.websocket.server.PathParam;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ExcluirBaseDadosApi {

	@PostMapping(path = "/excluir-database/{nome-base}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> excluirBaseDados(@PathParam("nome-base") String nome) {
		
		/*TODO*/
		return null;
	}
}
