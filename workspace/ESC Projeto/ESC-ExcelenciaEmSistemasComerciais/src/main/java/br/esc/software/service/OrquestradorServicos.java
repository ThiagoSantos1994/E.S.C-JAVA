package br.esc.software.service;

import static br.esc.software.global.Global.LogErro;
import static br.esc.software.global.Global.LogInfo;

import java.sql.SQLException;

import br.esc.software.global.ConnectionSQL;
import br.esc.software.integration.BackupSQL;
import br.esc.software.integration.ExportadorSQL;

public class OrquestradorServicos {
	/**
	 * Orquestrador de serviços do sistema ESC VB -> JAVA
	 * Processo de migração de funcionalidades para o JAVA
	 */
	/* PARAMETROS OBRIGATÓRIOS PARA PROCESSAMENTO BATCH */
	private String parametroEntrada;
	private String idOrdemExecucao;
	private String idFuncionario;
	private String idMaquina;
	private String linhaComando;

	public OrquestradorServicos(String[] args) {
		parametroEntrada = args[0];
		LogInfo("Parametro Entrada -> " + parametroEntrada);

		idOrdemExecucao = args[1];
		idFuncionario = args[2];
		idMaquina = args[3];
		linhaComando = linhaComando(args);
	}

	public void inicializacao() {
		ConnectionSQL sql = new ConnectionSQL();

		if (sql.abrirConexao()) {
			try {
				switch (parametroEntrada) {
				case "ExportacaoSQL":
					ExportadorSQL exportador = new ExportadorSQL();
					exportador.IniciarProcessamento(idOrdemExecucao, idFuncionario, idMaquina, linhaComando);
					break;
				case "ImportadorCEP":
					//	ImportarCEP cep = new ImportarCEP();
					//	cep.ImportarCEP(args);
					//	break;
				case "BackupSQL":
					BackupSQL backup = new BackupSQL();
					backup.IniciarBackup(idOrdemExecucao, idFuncionario, idMaquina, linhaComando);
					break;
				}
			} catch (SQLException ex) {
				LogErro("Ocorreu uma exceção SQL durante o processamento ->>", ex);
			} catch (Exception e) {
				LogErro("Ocorreu uma exceção genérica durante o processamento ->>", e);
			} finally {
				sql.fecharConexao();
			}
		}
	}

	private static String linhaComando(String[] args) {
		String linhaComando = args[0];
		for (int i = 1; i < args.length; i++) {
			linhaComando = linhaComando.concat(" " + args[i]);
		}
		LogInfo("Linha de Comando: " + linhaComando);
		return linhaComando;
	}
}
