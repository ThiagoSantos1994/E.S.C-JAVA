package br.esc.software.persistence;

import java.sql.ResultSet;

import br.esc.software.global.ConnectionSQL;
import br.esc.software.global.Global;

public class StatusProcessamentoPersistence {

	static ConnectionSQL SQL = new ConnectionSQL();
	static int RETORNO_OK = 1;
	static int RETORNO_NOK = -1;
	ResultSet RSAdo;

	private static int idProcessamento;
	
	protected void GravarStatusProcessamnto(int idOrdemExecucao, int idFuncionario, String idMaquina, String dataHoraInicio, String siglaSistema, String statusProcessamento, String linhaComando, String dsLogExecucao) throws Exception {
		try {
			
			Global.LogDebug("Excluindo registro de processamento VB da base de dados");
			this.ExcluirStatusProcessamentoVB(idOrdemExecucao, idFuncionario, idMaquina, siglaSistema);
			
			Global.LogDebug("Gravando registro de processamento JAVA na base de dados");
			String queryInsert = ("" + idOrdemExecucao + "," + idFuncionario + ",'" + idMaquina + "','" + dataHoraInicio + "','" + siglaSistema + "','" + statusProcessamento + "','" + linhaComando + "','" + dsLogExecucao + "', " + RETORNO_OK + "");
			SQL.Insert_Table("INSERT INTO tbd_ProcessamentoJava(id_OrdemExecucao, id_Funcionario, id_Maquina, ds_DataHoraInicio, ds_SiglaSistemica, ds_StatusProcessamento, ds_LinhaComando, ds_LogExecucao, id_Retorno) VALUES (" + queryInsert + ")");
			
			// Busca na base o ID de execução para realizar o update no stts do processamento ao chamar o metodo ATUALIZAR e CONCLUIR (18/09/2019)
			RSAdo = SQL.Select_Table("SELECT id_Processamento FROM tbd_ProcessamentoJava WHERE id_OrdemExecucao = " + idOrdemExecucao + " AND id_Maquina = '" + idMaquina + "' AND id_Funcionario = " + idFuncionario + " and ds_DataHoraInicio = '" + dataHoraInicio + "'");
			if (RSAdo.next()) {
				idProcessamento = RSAdo.getInt("id_Processamento");
			}
			RSAdo.close();
			
		} catch (Exception ex) {
			Global.LogErro("Ocorreu um erro SQL no metodo GravarStatusProcessamnto -> " + ex);
			throw new Exception(ex);
		}
	}
	
	protected boolean ExcluirStatusProcessamentoVB(int idOrdemExecucao, int idFuncionario, String idMaquina, String siglaSistema) throws Exception {
		/*
		 * Essa rotina exclui a linha da chamada VB do historio de processamento.
		 * No VB tem um tratamento que verifica, se existir o registro o sistema trata como uma falha no processamento da BATCH
		 */
		try {
			SQL.Delete_Table("DELETE FROM tbd_ProcessamentoJava WHERE ds_StatusProcessamento = 'VB' AND ds_SiglaSistemica = '" + siglaSistema + "' AND id_OrdemExecucao = " + idOrdemExecucao + " AND id_Funcionario = " + idFuncionario + " AND id_Maquina = '" + idMaquina + "'");
		} catch (Exception e) {
			Global.LogErro("Ocorreu um erro ao excluir da base tbd_ProcessamentoJava o registro de processamento VB -> " + e);
			throw new Exception(e);
		}
		return true;
	}
	
	protected void AtualizarStatusProcessamento(int idOrdemExecucao, String idMaquina, String statusProcessamento, String dsLogExecucao) throws Exception {
		try {
			SQL.Update_Table("UPDATE tbd_ProcessamentoJava SET ds_StatusProcessamento = '" + statusProcessamento + "', ds_LogExecucao = '" + dsLogExecucao + "' WHERE id_Processamento = " + idProcessamento + " AND id_OrdemExecucao = " + idOrdemExecucao + " AND id_Maquina = '" + idMaquina + "'");
		} catch (Exception e) {
			Global.LogErro("Ocorreu um erro ao atualizar o status do processamento -> " + e);
			throw new Exception(e);
		}
	}

	protected void ConcluirStatusProcessamento(int idOrdemExecucao, String idMaquina, String statusProcessamento, String dsLogExecucao, String dataHoraFimProcessamento, boolean bErro) throws Exception {
		try {
			String statusRetorno;
			if(bErro) {
				statusRetorno = "id_Retorno = " + RETORNO_NOK;
			} else {
				statusRetorno = "id_Retorno = " + RETORNO_OK;
			}
			
			SQL.Update_Table("UPDATE tbd_ProcessamentoJava SET ds_StatusProcessamento = '" + statusProcessamento + "', ds_LogExecucao = '" + dsLogExecucao + "', ds_DataHoraFim = '" + dataHoraFimProcessamento + "', " + statusRetorno + " WHERE id_Processamento = " + idProcessamento + " AND id_OrdemExecucao = " + idOrdemExecucao + " AND id_Maquina = '" + idMaquina + "'");
		} catch (Exception e) {
			Global.LogErro("Ocorreu um erro ao atualizar o status do processamento -> " + e);
			throw new Exception(e);
		}
	}
}
