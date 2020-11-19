package br.esc.software.restservice.internas;

import br.esc.software.business.ExportadorBusiness;
import br.esc.software.configuration.ConnectionSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.esc.software.commons.utils.GlobalUtils.LogInfo;

@RestController
@RequestMapping("/api")
public class GerarScriptApi {

    @Autowired
    ConnectionSQL connection;
    @Autowired
    ExportadorBusiness business;

    @GetMapping(path = "/obter-script-create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringBuffer> gerarScriptCreate() throws Exception {

        LogInfo("<<INICIO>> Inicializando API obter-script-insert");

        connection.abrirConexao();

        StringBuffer response = business.gerarScriptImplantacao();

        connection.fecharConexao();

        LogInfo("<<FIM>> obter-script-insert gerado com sucesso!");

        return new ResponseEntity<StringBuffer>(response, HttpStatus.OK);
    }
}
