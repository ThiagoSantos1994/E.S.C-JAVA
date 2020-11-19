package br.esc.software.repository;

import static br.esc.software.commons.utils.GlobalUtils.LogErro;
import static br.esc.software.configuration.ConnectionSQLBackup.Delete_Table;
import static br.esc.software.configuration.ConnectionSQLBackup.Insert_Table;

public class BackupDao {

    public void excluirDadosTabelas(String nomeTabela) throws Exception {
        try {
            Delete_Table("DELETE FROM " + nomeTabela);
        } catch (Exception e) {
            LogErro("Ocorreu um erro ao excluir os dados da tabela -> " + nomeTabela);
            throw new Exception(e);
        }
    }

    public void inserirDadosTabelas(String baseBackup, String basePrincipal, String nomeTabela) throws Exception {
        try {
            String sQueryInsert = "INSERT INTO " + baseBackup + ".dbo." + nomeTabela + " SELECT * FROM " + basePrincipal + ".dbo." + nomeTabela;
            Insert_Table(sQueryInsert);
        } catch (Exception e) {
            LogErro("Erro ao gravar os dados da tabela -> " + nomeTabela);
            throw new Exception(e);
        }
    }
}
