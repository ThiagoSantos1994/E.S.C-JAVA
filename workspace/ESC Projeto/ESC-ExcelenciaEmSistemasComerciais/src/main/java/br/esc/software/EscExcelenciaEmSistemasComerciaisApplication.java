package br.esc.software;

import static br.esc.software.global.Global.LogErro;
import static br.esc.software.global.Global.LogInfo;

import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.esc.software.global.ConnectionSQL;
import br.esc.software.integration.BackupSQL;
import br.esc.software.integration.ExportadorSQL;

@SpringBootApplication
public class EscExcelenciaEmSistemasComerciaisApplication {
	/**
	 * Orquestrador de processos - SISTEMA ESC
	 * 
	 * @author Thiago Santos
	 * @since 06/2019
	 */

	public static void main(String[] args) {
		SpringApplication.run(EscExcelenciaEmSistemasComerciaisApplication.class, args);
		ConnectionSQL sql = new ConnectionSQL();

		if (sql.abrirConexao()) {
			try {
				String parametroEntrada = args[0];
				LogInfo("Parametro Entrada -> " + parametroEntrada);
			
				// PARAMETROS OBRIGATÓRIOS PARA PROCESSAMENTO BATCH
				String idOrdemExecucao = args[1];
				String idFuncionario = args[2];
				String idMaquina = args[3];
				String linhaComando = linhaComando(args);

				switch (parametroEntrada) {
					case "ExportacaoSQL":
						ExportadorSQL exportador = new ExportadorSQL();
						exportador.IniciarProcessamento(idOrdemExecucao, idFuncionario, idMaquina, linhaComando);
						break;
					case "ImportadorCEP":
//						ImportarCEP cep = new ImportarCEP();
//						cep.ImportarCEP(args);
//						break;
					case "BackupSQL":
						BackupSQL backup = new BackupSQL();
						backup.IniciarBackup(idOrdemExecucao, idFuncionario, idMaquina, linhaComando);
						break;
				}
			} catch (SQLException ex) {
				LogErro("Ocorreu uma exceção SQL durante o processamento ->>", ex);
			}catch (IndexOutOfBoundsException e) {
				LogErro("Necessario informar algum parametro de entrada no orquestrador");
			} catch (Exception e) {
				LogErro("Ocorreu uma exceção genérica durante o processamento ->>", e);
			} finally {
				// Encerra a execução da aplicação JAVA
				sql.fecharConexao();
				System.exit(0);
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
