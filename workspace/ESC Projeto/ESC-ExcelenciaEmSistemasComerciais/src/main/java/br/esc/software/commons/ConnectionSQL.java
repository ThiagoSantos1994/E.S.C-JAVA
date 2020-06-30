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

public class ConnectionSQL {
	private static Connection connection;
	private static Statement stmt;

	private static String servidor = getProperties().getProperty("prop.server");
	private static String banco = getProperties().getProperty("prop.database");
	private static String usuario = getProperties().getProperty("prop.user");
	private static String senha = getProperties().getProperty("prop.password");

	private void conectarBaseDAO() throws ExcecaoGlobal {
		try {
			LogInfo("Conectando na base de dados SQL...");
			
			connection = DriverManager.getConnection("jdbc:sqlserver://" + servidor + ";databaseName=" + banco
					+ ";user=" + usuario + ";password=" + senha);
			
			stmt = connection.createStatement();
			LogInfo("Conexao realizada com sucesso!");
		} catch (SQLException e) {
			throw new ExcecaoGlobal("Erro ao conectar no banco de dados -> ", e);
		}
	}

	public void abrirConexao() throws ExcecaoGlobal, SQLException {
		if (null == stmt) {
			this.conectarBaseDAO();
		}
	}

	public void fecharConexao() throws SQLException {
		LogInfo("Fechando conexao...");
		connection.close();
	}

	public static ResultSet Select_Table(String sSelectTable) {
		ResultSet rs;
		try {
			rs = stmt.executeQuery(sSelectTable);
			return rs;
		} catch (SQLException e) {
			LogErro("Erro ao executar o metodo SELECT_TABLE:", e);
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
		stmt.executeUpdate(sQueryOperacao);
	}
}
