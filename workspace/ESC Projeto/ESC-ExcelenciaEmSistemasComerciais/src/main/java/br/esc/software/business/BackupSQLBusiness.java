package br.esc.software.business;

import static br.esc.software.commons.Global.LogDebug;
import static br.esc.software.commons.Global.getProperties;

import java.sql.SQLException;

import org.springframework.stereotype.Component;

import br.esc.software.commons.ConnectionSQLBackup;
import br.esc.software.domain.TabelasSQL;
import br.esc.software.exceptions.ExcecaoGlobal;
import br.esc.software.persistence.BackupDao;
import br.esc.software.persistence.ExportadorDao;

@Component
public class BackupSQLBusiness {
	ConnectionSQLBackup sqlBackup = new ConnectionSQLBackup();
	ExportadorDao exportadorDao = new ExportadorDao();
	BackupDao backupDao = new BackupDao();

	private String sBaseBackup = getProperties().getProperty("prop.databaseBackup");
	private String sBasePrincipal = getProperties().getProperty("prop.database");

	boolean bProcessamentoComFalhas = false;

	public String iniciarBackup() throws ExcecaoGlobal, SQLException {
		try {
			sqlBackup.abrirConexaoBackup();
			
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
			
			sqlBackup.fecharConexaoBackup();

		} catch (Exception ex) {
			throw new ExcecaoGlobal("Ocorreu uma excessao durante o backup Java -> ", ex);
		}
		
		if (bProcessamentoComFalhas) {
			String sMensagemProcessamentoFalha = "[WARN] Processamento concluido com FALHA! Backup parcial executado com sucesso!";
			return sMensagemProcessamentoFalha;
		} else {
			String sMensagemProcessamentoOK = "Processamento concluido! Backup executado com sucesso!";
			return sMensagemProcessamentoOK;
		}
	}
}
