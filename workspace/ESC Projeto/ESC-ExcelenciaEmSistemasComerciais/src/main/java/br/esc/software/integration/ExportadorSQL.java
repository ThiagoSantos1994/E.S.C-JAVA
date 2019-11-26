package br.esc.software.integration;
import static br.esc.software.global.Global.AnoAtual;
import static br.esc.software.global.Global.CONCLUIDO;
import static br.esc.software.global.Global.DataAtual;
import static br.esc.software.global.Global.ERRO;
import static br.esc.software.global.Global.EXECUCAO;
import static br.esc.software.global.Global.EscreverArquivoTexto;
import static br.esc.software.global.Global.ExcluirArquivoTexto;
import static br.esc.software.global.Global.LogDebug;
import static br.esc.software.global.Global.LogInfo;
import static br.esc.software.global.Global.MesNomeAtual;
import static br.esc.software.global.Global.PROCESSANDO;

/**
 * Implementação do Exportador SQL do sistema, modulo existente na versão VB6.
 * 
 * @author Thiago Santos
 * @since 06/2019
 */
import java.sql.SQLException;
import java.util.ArrayList;

import br.esc.software.business.ExportadorSQLBusiness;
import br.esc.software.dao.ExportadorSQLDao;
import br.esc.software.dao.GravarStatusProcessamentoDao;
import br.esc.software.domain.TabelasSQL;
import br.esc.software.global.ObjectUtils;

public class ExportadorSQL {
	static final String SIGLA_SISTEMICA = "ExportacaoSQL";
	ExportadorSQLDao dao = new ExportadorSQLDao();
	ExportadorSQLBusiness montaArquivoExportacao = new ExportadorSQLBusiness();
	GravarStatusProcessamentoDao status = new GravarStatusProcessamentoDao();
	
	/**
	 * Gera um arquivo SQL com os dados da base de dados
	 *  
	 * @param idOrdemExecucao
	 * @param idFuncionario
	 * @param idMaquina
	 * @param linhaComando
	 * @throws SQLException
	 * @throws Exception
	 */
	public void IniciarProcessamento(String idOrdemExecucao, String idFuncionario, String idMaquina, String linhaComando) throws SQLException, Exception {
		LogInfo("Inicializando exportação do arquivo SQL");
		
		try {
			status.InicioProcessamento(idOrdemExecucao, idFuncionario, idMaquina, DataAtual(), SIGLA_SISTEMICA, EXECUCAO, linhaComando, PROCESSANDO);
			String sDirArquivo = this.nomeArquivo();	
			
			ExcluirArquivoTexto(sDirArquivo);
			
			this.montaCabecalho();
			
			ArrayList<TabelasSQL> tabelasDAO = dao.getListaTabelas();
			for (TabelasSQL tabelas : tabelasDAO) {
				String tabelaSQL = tabelas.getNomeTabela();
				String colunasSQL = dao.montaColunaTabela(tabelaSQL);
				
				LogDebug("Montando script da tabela -> " + tabelaSQL);
				status.AtualizarProcessamento(idOrdemExecucao, idMaquina, EXECUCAO, "Montando script da tabela -> " + tabelaSQL);	
				montaArquivoExportacao.GerarArquivoExportacao(tabelaSQL, colunasSQL, sDirArquivo);
			}
			
			String sMensagemProcessamento = "Processamento concluído! arquivo disponibilizado em: " + sDirArquivo;
			status.ConcluirProcessamento(idOrdemExecucao, idMaquina, CONCLUIDO, sMensagemProcessamento, DataAtual());
		} catch (Exception ex) {
			String strErro = ("Ocorreu um erro inesperado na classe ExportadorSQL, processamento interrompido -> " + ex);
			status.ErroProcessamento(idOrdemExecucao, idMaquina, ERRO, strErro, DataAtual());
			LogInfo(strErro);
			return;
		} 
	}

	private String nomeArquivo() throws SQLException {
		String nomeArquivo = dao.getDiretorioDestinoArquivo() + "BACKUP_";
		nomeArquivo = nomeArquivo.concat(MesNomeAtual() + "-" + AnoAtual() + ".SQL");
		return nomeArquivo.toUpperCase();
	}
	
	private void montaCabecalho() throws Exception {
		StringBuffer buffer = new StringBuffer();
		buffer.append("--[EXPORTADOR DE CONFIGURAÇÕES E REGISTROS - PROCESSADO EM " + DataAtual() + " - JAVA" + ObjectUtils.pularLinha());
		buffer.append(montaArquivoExportacao.MontaScriptImplantacao());
		EscreverArquivoTexto(buffer, this.nomeArquivo());
	}
}
