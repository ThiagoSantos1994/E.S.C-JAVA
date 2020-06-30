package br.esc.software.persistence;

import static br.esc.software.commons.ConnectionSQLBackup.Delete_Table;
import static br.esc.software.commons.ConnectionSQLBackup.Insert_Table;
import static br.esc.software.commons.Global.LogErro;

public class BackupDao {

	public boolean excluirDadosTabelas(String nomeTabela) {
		try {
			Delete_Table("DELETE FROM " + nomeTabela);
		} catch (Exception e) {
			LogErro("Erro ao excluir dados da tabela -> " + nomeTabela);
			return false;
		}
		return true;
	}

	public boolean inserirDadosTabelas(String baseBackup, String basePrincipal, String nomeTabela) {
		try {
			String sQueryInsert = "INSERT INTO " + baseBackup + ".dbo." + nomeTabela + " SELECT * FROM " + basePrincipal + ".dbo." + nomeTabela;
			Insert_Table(sQueryInsert);
		} catch (Exception e) {
			LogErro("Erro ao gravar os dados da tabela -> " + nomeTabela);
			return false;
		}
		return true;
	}
}
