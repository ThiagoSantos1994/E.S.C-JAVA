package br.esc.software.api.global;

import static br.esc.software.api.global.Global.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ConnectionSQL {
	private static Connection connection;
	private static Statement stmt;

	private boolean conexaoDAO() {
		Properties prop = getProperties();
		String sServidor = prop.getProperty("prop.server");
		String sBanco = prop.getProperty("prop.database");
		String sUsuario = prop.getProperty("prop.user");
		String sSenha = prop.getProperty("prop.password");
		String URL = "jdbc:sqlserver://" + sServidor + ";databaseName=" + sBanco + ";user=" + sUsuario + ";password=" + sSenha;

		try {
			connection = DriverManager.getConnection(URL);
			stmt = connection.createStatement();
			LogInfo("Conexao JDBC REALIZADA COM SUCESSO: ");
			return true;
		} catch (SQLException e) {
			LogErro("Erro ao conectar no banco de dados -> ", e);
			return false;
		}
	}
	
	public boolean abrirConexao() {
		boolean bConexaoAtiva = this.conexaoDAO();
		return bConexaoAtiva;
	}
	
	public void fecharConexao() {
		try {
			connection.close();
			LogInfo("Conexao JDBC FECHADA COM SUCESSO!");
		} catch (SQLException e) {
			LogErro("Ocorreu um erro ao fechar a conexao com SQL - ", e);
		}
	}
	
	public ResultSet Select_Table(String sSelectTable) {
		ResultSet rs;
		try {
			rs = stmt.executeQuery(sSelectTable);
			return rs;
		} catch (SQLException e) {
			LogErro("Erro ao executar o metodo SELECT_TABLE:", e);
		}
		return null;
	}

	public boolean Insert_Table(String sInsertTable) {
		return ExecuteInstrucoesSQL(sInsertTable);
	}

	public boolean Update_Table(String sUpdateTable) {
		return ExecuteInstrucoesSQL(sUpdateTable);
	}

	public boolean Delete_Table(String sDeleteTable) {
		return ExecuteInstrucoesSQL(sDeleteTable);
	}

	private boolean ExecuteInstrucoesSQL(String sQueryOperacao) {
		if (null == stmt) {
			this.abrirConexao();
		}
		
		try {
			stmt.executeUpdate(sQueryOperacao);
			return true;
		} catch (SQLException e) {
			LogErro("Erro ao executar o metodo ExecuteInstrucoesSQL:", e);
			LogErro("QUERY: " + sQueryOperacao);
			return false;
		}
	}
}
