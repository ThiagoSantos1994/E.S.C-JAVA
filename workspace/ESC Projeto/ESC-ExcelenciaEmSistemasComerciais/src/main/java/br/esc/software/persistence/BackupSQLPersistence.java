package br.esc.software.persistence;

import br.esc.software.global.ConnectionSQLBackup;
import br.esc.software.global.Global;

public class BackupSQLPersistence {
	ConnectionSQLBackup sqlBackup = new ConnectionSQLBackup();

	protected boolean excluirDadosTabelas(String nomeTabela) {
		if (!sqlBackup.Delete_Table("DELETE FROM " + nomeTabela)) {
			Global.LogErro("Erro ao excluir dados da tabela -> " + nomeTabela);
			return false;
		}
		return true;
	}

	protected boolean inserirDadosTabelas(String baseBackup, String basePrincipal, String nomeTabela) {
		String sQueryInsert = "INSERT INTO " + baseBackup + ".dbo." + nomeTabela + " SELECT * FROM " + basePrincipal + ".dbo." + nomeTabela;
		if (!sqlBackup.Insert_Table(sQueryInsert)) {
			Global.LogErro("Erro ao gravar os dados da tabela -> " + nomeTabela);
			return false;
		}
		return true;
	}
}
