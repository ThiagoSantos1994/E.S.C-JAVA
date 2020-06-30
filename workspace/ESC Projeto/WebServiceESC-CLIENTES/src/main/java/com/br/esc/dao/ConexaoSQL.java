package com.br.esc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoSQL {

	public boolean conectaSQL() {
		if (null != this.connection()) {
			return true;
		} else {
			return false;
		}
	}

	private Statement connection() {
		Connection con;
		try {
			con = DriverManager.getConnection(
					"jdbc:sqlserver://localhost:1433;databaseName=bkp_BancoScmThiago;user=sa;password=13509");
//					"jdbc:sqlserver://localhost:1433;databaseName=BancoScmPizzaria;user=sa;password=13509");
			Statement stmt = con.createStatement();
			return stmt;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ResultSet Select_Table(String sSelectTable) {
		ResultSet rs;
		try {
			rs = this.connection().executeQuery(sSelectTable);
			return rs;
		} catch (SQLException e) {
			System.out.println("Erro: " + e);
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
		Statement stmt = this.connection();
		try {
			stmt.executeUpdate(sQueryOperacao);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
}
