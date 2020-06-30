package br.esc.software.rs;

import static br.esc.software.commons.Global.LogInfo;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.esc.software.business.BackupSQLBusiness;
import br.esc.software.commons.ConnectionSQL;
import br.esc.software.commons.ConnectionSQLBackup;
import br.esc.software.exceptions.ExcecaoGlobal;

@RestController
@RequestMapping("/api")
public class BackupApi {

	@Autowired
	BackupSQLBusiness backup;
	ConnectionSQL connection = new ConnectionSQL();
	
	@PostMapping("/backup-sql-principal")
	public ResponseEntity<String> iniciarBackup() throws ExcecaoGlobal, SQLException {

		LogInfo("Iniciando o Backup da base Principal SQL");

		try {
			ConnectionSQLBackup backup = new ConnectionSQLBackup();
			backup.abrirConexaoBackup();
			connection.abrirConexao();
		} catch (Exception e) {
			throw new ExcecaoGlobal("Erro ao conectar na base SQL Backup");
		}

		String response = backup.iniciarBackup();
		
		LogInfo("Backup da base Principal SQL realizado com sucesso!");
		
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

}
