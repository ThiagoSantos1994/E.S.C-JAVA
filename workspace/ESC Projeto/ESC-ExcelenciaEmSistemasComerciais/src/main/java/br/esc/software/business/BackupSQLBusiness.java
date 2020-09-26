package br.esc.software.business;

import static br.esc.software.commons.GlobalUtils.LogDebug;
import static br.esc.software.commons.GlobalUtils.getProperties;

import java.sql.SQLException;

import org.springframework.stereotype.Component;

import br.esc.software.commons.ExcecaoGlobal;
import br.esc.software.commons.ObjectParser;
import br.esc.software.domain.Response;
import br.esc.software.domain.TabelasSQL;
import br.esc.software.repository.BackupDao;
import br.esc.software.repository.ExportadorDao;

@Component
public class BackupSQLBusiness {
	
	ExportadorDao exportadorDao = new ExportadorDao();
	BackupDao backupDao = new BackupDao();
	ObjectParser parser = new ObjectParser();
	
	private String sBaseBackup = getProperties().getProperty("prop.databaseBackup");
	private String sBasePrincipal = getProperties().getProperty("prop.database");
	private boolean bProcessamentoComFalhas = false;
	
	public String iniciarBackup() throws ExcecaoGlobal, SQLException {
		Response response = new Response();
		
		try {
			for (TabelasSQL tabelaSQL : exportadorDao.getListaTabelas()) {
				String tabela = tabelaSQL.getNomeTabela();

				boolean statusDelete = backupDao.excluirDadosTabelas(tabela);
				if (statusDelete == false) {
					bProcessamentoComFalhas = true;
				}

				boolean statusInsert = backupDao.inserirDadosTabelas(sBaseBackup, sBasePrincipal, tabela);
				if (statusInsert == false) {
					bProcessamentoComFalhas = true;
				} else {
					LogDebug("Backup realizado com sucesso -> " + tabela);
				}
			}
		} catch (Exception ex) {
			throw new ExcecaoGlobal("Ocorreu uma excessao durante o backup Java -> ", ex);
		}
		
		if (bProcessamentoComFalhas) {
			response.setResponse("[WARN] Processamento concluido com FALHA! Backup parcial executado com sucesso!");
			return parser.parser(response);
		} else {
			response.setResponse("Processamento concluido! Backup executado com sucesso!");
			return parser.parser(response);
		}
		
	}
}
