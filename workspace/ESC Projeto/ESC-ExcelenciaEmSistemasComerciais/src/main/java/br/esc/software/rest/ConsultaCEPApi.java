package br.esc.software.rest;

import static br.esc.software.commons.GlobalUtils.LogInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.esc.software.business.ConsultaCEPBusiness;
import br.esc.software.commons.ExcecaoGlobal;

@RestController
@RequestMapping("/api")
public class ConsultaCEPApi {
	
	@Autowired
	ConsultaCEPBusiness business;
	
	@GetMapping(path = "/consulta-cep/{cep}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> iniciarBackup(@PathVariable("cep") String cep) throws ExcecaoGlobal {

		LogInfo("<<INICIO>> Iniciando consulta cep API viacep");

		String response = business.obtemDadosCepConsulta(cep);
		
		LogInfo("<<FIM>> " + response);
		
		return new ResponseEntity<String>(response.replace("\\u003d", ""), HttpStatus.OK);
	}
}
