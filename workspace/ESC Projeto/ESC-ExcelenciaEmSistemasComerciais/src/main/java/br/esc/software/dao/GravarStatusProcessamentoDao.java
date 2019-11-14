package br.esc.software.dao;

import static br.esc.software.global.Global.*;

import br.esc.software.persistence.StatusProcessamentoPersistence;

public class GravarStatusProcessamentoDao extends StatusProcessamentoPersistence {
	
	int idOrdemProcessamento;
	int idFuncionarioSistema;
	
	public void InicioProcessamento(String idOrdemExecucao, String idFuncionario, String idMaquina, String dataHoraInicio, String siglaSistema, String statusProcessamento, String linhaComando, String dsLogExecucao) throws Exception {
		idOrdemProcessamento = Integer.parseInt(idOrdemExecucao);
		idFuncionarioSistema = Integer.parseInt(idFuncionario);
		
		super.GravarStatusProcessamnto(idOrdemProcessamento, idFuncionarioSistema, idMaquina, dataHoraInicio, siglaSistema, statusProcessamento, linhaComando, dsLogExecucao);
	}
	
	public void AtualizarProcessamento(String idOrdemExecucao, String idMaquina, String statusProcessamento, String dsLogExecucao) throws Exception {
		idOrdemProcessamento = Integer.parseInt(idOrdemExecucao);
		super.AtualizarStatusProcessamento(idOrdemProcessamento, idMaquina, statusProcessamento, dsLogExecucao);
	}
	
	public void ConcluirProcessamento(String idOrdemExecucao, String idMaquina, String statusProcessamento, String dsLogExecucao, String dataHoraFimProcessamento) throws Exception {
		idOrdemProcessamento = Integer.parseInt(idOrdemExecucao);
		LogInfo(statusProcessamento);
		super.ConcluirStatusProcessamento(idOrdemProcessamento, idMaquina, statusProcessamento, dsLogExecucao, dataHoraFimProcessamento, false);
	}
	
	public void ErroProcessamento(String idOrdemExecucao, String idMaquina, String statusProcessamento, String dsLogErro, String dataHoraFimProcessamento) throws Exception {
		idOrdemProcessamento = Integer.parseInt(idOrdemExecucao);
		LogErro(dsLogErro);
		super.ConcluirStatusProcessamento(idOrdemProcessamento, idMaquina, statusProcessamento, dsLogErro, dataHoraFimProcessamento, true );
	}
}
