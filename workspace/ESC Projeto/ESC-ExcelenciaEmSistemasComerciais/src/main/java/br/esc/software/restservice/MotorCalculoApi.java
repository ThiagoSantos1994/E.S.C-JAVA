package br.esc.software.restservice;

import static br.esc.software.commons.GlobalUtils.LogInfo;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.esc.software.business.MotorCalculoBusiness;
import br.esc.software.commons.ExcecaoGlobal;
import br.esc.software.configuration.ConnectionSQL;
import br.esc.software.domain.MotorCalculoMapper;

@RestController
@RequestMapping("/api")
public class MotorCalculoApi {

	ConnectionSQL connection = new ConnectionSQL();
	MotorCalculoBusiness business = new MotorCalculoBusiness();

	@GetMapping(path = "/motor-calculo/relatorio/ano/{ano}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MotorCalculoMapper> excluirBaseDados(@PathVariable("ano") Integer ano)
			throws ExcecaoGlobal {

		LogInfo("<<INICIO>> Inicializando API motor-calculo/relatorio/ano/" + ano);

		connection.abrirConexao();

		MotorCalculoMapper response = business.realizarCalculo(ano);

		connection.fecharConexao();

		LogInfo("<<FIM>> Calculo realizado com sucesso!");

		return new ResponseEntity<MotorCalculoMapper>(response, HttpStatus.OK);
	}
}
