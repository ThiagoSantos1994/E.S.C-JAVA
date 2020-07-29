package br.esc.software.commons;

import static br.esc.software.commons.Global.LogErro;
import static br.esc.software.commons.Global.LogInfo;
import static br.esc.software.commons.Global.getProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.esc.software.exceptions.ExcecaoGlobal;

public class ConnectionSQLBackup {
	private static Connection connection;
	private static Statement stmt;

	private static void conectarBaseSQL() throws ExcecaoGlobal {
		LogInfo("Conectando na base de dados SQL Backup...");

		try {
			connection = DriverManager.getConnection(getUriConexao());
			stmt = connection.createStatement();
			LogInfo("Conexao realizada com sucesso!");
		} catch (SQLException e) {
			throw new ExcecaoGlobal("Erro ao conectar no banco de dados -> ", e);
		}
	}

	private static String getUriConexao() {
		/*
		 * Obtem os dados de conexao DAO atravez dos parametros configurados no arquivo
		 * application.properties
		 */
		String servidor = getProperties().getProperty("prop.server");
		String banco = getProperties().getProperty("prop.databaseBackup");
		String usuario = getProperties().getProperty("prop.user");
		String senha = getProperties().getProperty("prop.password");

		return "jdbc:sqlserver://" + servidor + ";databaseName=" + banco + ";user=" + usuario + ";password=" + senha;
	}

	public void abrirConexaoBackup() throws ExcecaoGlobal {
		if (null == stmt) {
			conectarBaseSQL();
		}
	}

	public void fecharConexaoBackup() throws ExcecaoGlobal {
		LogInfo("Fechando conexao...");
		try {
			connection.close();
		} catch (SQLException e) {
			throw new ExcecaoGlobal("Ocorreu uma falha ao fechar a conexao DAO", e);
		}
	}

	public static ResultSet Select_Table(String sSelectTable) throws ExcecaoGlobal {
		ResultSet rs;
		try {
			if (null == stmt) {
				conectarBaseSQL();
			} else {
				rs = stmt.executeQuery(sSelectTable);
				return rs;
			}
		} catch (SQLException e) {
			LogErro("Erro ao executar o metodo SELECT_TABLE:", e);
		} catch (ExcecaoGlobal e) {
			throw new ExcecaoGlobal(e.getMessage(), e.getCause());
		}
		return null;
	}

	public static void Insert_Table(String sInsertTable) throws SQLException {
		ExecuteInstrucoesSQL(sInsertTable);
	}

	public static void Update_Table(String sUpdateTable) throws SQLException {
		ExecuteInstrucoesSQL(sUpdateTable);
	}

	public static void Delete_Table(String sDeleteTable) throws SQLException {
		ExecuteInstrucoesSQL(sDeleteTable);
	}

	private static void ExecuteInstrucoesSQL(String sQueryOperacao) throws SQLException {
		try {
			LogInfo("ExecuteInstrucoesSQLBackup ->>> " + sQueryOperacao);
			stmt.executeUpdate(sQueryOperacao);
		} catch (Exception e) {
			throw new SQLException("Erro ao executar metodo ExecuteInstrucoesSQLBackup", e);
		}

	}
}
