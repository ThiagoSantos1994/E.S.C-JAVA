package br.esc.software.restservice.internas;

import br.esc.software.business.BackupSQLBusiness;
import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.configuration.ConnectionSQL;
import br.esc.software.configuration.ConnectionSQLBackup;
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
public class BackupApi {

    @Autowired
    BackupSQLBusiness backup;

    private String response = "";
    private ConnectionSQL connection = new ConnectionSQL();
    private ConnectionSQLBackup connectionBkp = new ConnectionSQLBackup();

    @PostMapping(path = "/backup-sql-principal", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> iniciarBackup() throws ExcecaoGlobal, SQLException {

        LogInfo("<<INICIO>> Iniciando Backup SQL");

        abrirConexao();

        response = backup.iniciarBackup();

        fecharConexao();

        LogInfo("<<FIM>> " + response);
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    private void abrirConexao() throws ExcecaoGlobal {
        connection.abrirConexao();
        connectionBkp.abrirConexao();
    }

    private void fecharConexao() throws ExcecaoGlobal {
        connection.fecharConexao();
        connectionBkp.fecharConexaoBackup();
    }
}
