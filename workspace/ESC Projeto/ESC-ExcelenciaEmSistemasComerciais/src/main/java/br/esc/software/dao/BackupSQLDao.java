package br.esc.software.dao;

import br.esc.software.persistence.BackupSQLPersistence;

public class BackupSQLDao extends BackupSQLPersistence {
	
	public boolean excluirDadosTabelas(String nomeTabela) {
		return super.excluirDadosTabelas(nomeTabela);
	}
	
	public boolean inserirDadosTabelas(String baseBackup, String basePrincipal, String nomeTabela) {
		return super.inserirDadosTabelas(baseBackup, basePrincipal, nomeTabela);
	}
}
