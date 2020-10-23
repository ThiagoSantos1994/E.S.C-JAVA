package br.esc.software.restservice.internas;

import br.esc.software.business.ExportadorSQLBusiness;
import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.configuration.ConnectionSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

import static br.esc.software.commons.utils.GlobalUtils.LogInfo;

@RestController
@RequestMapping("/api")
public class ExportadorApi {

    @Autowired
    ExportadorSQLBusiness exportador;
    ConnectionSQL connection = new ConnectionSQL();

    @PostMapping(path = "/exportar-arquivos-sql", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> iniciarExportacao() throws SQLException, ExcecaoGlobal {

        LogInfo("<<INICIO>> Iniciando exportação script SQL");

        connection.abrirConexao();

        String response = exportador.iniciarExportacao();

        connection.fecharConexao();

        LogInfo("<<FIM>> Script exportado com sucesso!");
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }
}
