package br.esc.software.integration;
import static br.esc.software.global.Global.*;

import java.util.Properties;
import br.esc.software.dao.BackupSQLDao;
import br.esc.software.dao.ExportadorSQLDao;
import br.esc.software.dao.GravarStatusProcessamentoDao;
import br.esc.software.domain.TabelasSQL;
import br.esc.software.global.ConnectionSQLBackup;

public class BackupSQL {
	static final String SIGLA_SISTEMICA = "BackupSQL";
	ConnectionSQLBackup sqlBackup = new ConnectionSQLBackup();
	ExportadorSQLDao exportadorSQLDao = new ExportadorSQLDao();
	GravarStatusProcessamentoDao status = new GravarStatusProcessamentoDao();
	BackupSQLDao dao = new BackupSQLDao();
	Properties prop = getProperties();

	String sBaseBackup = prop.getProperty("prop.databaseBackup");
	String sBasePrincipal = prop.getProperty("prop.database");
	boolean bProcessamentoComFalhas = false;
	
	public void IniciarBackup(String idOrdemExecucao, String idFuncionario, String idMaquina, String linhaComando) throws Exception {
		
		if (!sqlBackup.abrirConexao()) {
			String strErro = ("Erro ao conectar no banco de dados -> " + sBaseBackup);
			status.ErroProcessamento(idOrdemExecucao, idMaquina, ERRO, strErro, DataAtual());
			LogErro(strErro);
			return;
		}
		
		try {
			status.InicioProcessamento(idOrdemExecucao, idFuncionario, idMaquina, DataAtual(), SIGLA_SISTEMICA, EXECUCAO, linhaComando, PROCESSANDO);
			
			for (TabelasSQL tabelaSQL : exportadorSQLDao.getListaTabelas()) {
				String tabela = tabelaSQL.getNomeTabela();
				String sLog = "Processando backup da tabela ->> " + tabela;
				
				LogInfo(sLog);
				status.AtualizarProcessamento(idOrdemExecucao, idMaquina, EXECUCAO, sLog);
				
				boolean statusDelete = dao.excluirDadosTabelas(tabela);
				if (statusDelete == false) {
					bProcessamentoComFalhas = true;
					LogErro("Ocorreu um erro ao excluir os dados da tabela -> " + tabela);
				}
				
				boolean statusInsert = dao.inserirDadosTabelas(sBaseBackup, sBasePrincipal, tabela);
				if (statusInsert == false) {
					bProcessamentoComFalhas = true;
					LogErro("Erro ao realizar o backup da tabela -> " + tabela);
				} else {
					LogDebug("Backup realizado com sucesso -> " + tabela);
				}
				
			}
		} catch (Exception ex) {
			String strErro = ("Ocorreu uma excessao durante o backup Java -> " + ex);
			status.ErroProcessamento(idOrdemExecucao, idMaquina, ERRO, strErro, DataAtual());
			LogErro(strErro);
			this.fecharConexao();
			return;
		} 

		this.fecharConexao();
		
		if (bProcessamentoComFalhas) {
			String sMensagemProcessamentoFalha = "[WARN] Processamento concluído com FALHA! Backup parcial executado com sucesso!";
			status.ConcluirProcessamento(idOrdemExecucao, idMaquina, CONCLUIDO, sMensagemProcessamentoFalha, DataAtual());
		} else {
			String sMensagemProcessamento = "Processamento concluído! Backup executado com sucesso!";
			status.ConcluirProcessamento(idOrdemExecucao, idMaquina, CONCLUIDO, sMensagemProcessamento, DataAtual());
		}
	}
	
	private void fecharConexao() {
		sqlBackup.fecharConexao();
	}
}
