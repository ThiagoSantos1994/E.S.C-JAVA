package br.esc.software.restservice;

import br.esc.software.business.ApisExternasBusiness;
import br.esc.software.commons.ExcecaoGlobal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.esc.software.commons.GlobalUtils.LogInfo;

@RestController
@RequestMapping("/api")
public class ApisExternasApi {

    @Autowired
    ApisExternasBusiness business;

    @GetMapping(path = "/consulta-cep/{cep}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> consultarCep(@PathVariable("cep") String cep) throws ExcecaoGlobal {

        LogInfo("<<INICIO>> Iniciando consulta cep API viacep");

        String response = business.obtemDadosCepConsulta(cep);

        LogInfo("<<FIM>> " + response);

        return new ResponseEntity<String>(response.replace("\\u003d", ""), HttpStatus.OK);
    }

    @GetMapping(path = "/consulta-previsao-tempo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> consultarPrevisao() throws ExcecaoGlobal {

        LogInfo("<<INICIO>> Iniciando consulta previsão tempo API via hgbrasil");

        String response = business.obtemPrevisaoTempo();

        LogInfo("<<FIM>> " + response);

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }
}
