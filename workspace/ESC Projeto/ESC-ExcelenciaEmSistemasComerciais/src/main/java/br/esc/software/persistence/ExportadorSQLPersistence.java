package br.esc.software.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.esc.software.domain.ColunasSQL;
import br.esc.software.domain.TabelasSQL;
import br.esc.software.global.ConnectionSQL;

public class ExportadorSQLPersistence {

	static ConnectionSQL SQL = new ConnectionSQL();
	ResultSet RSAdo;

	protected ArrayList<TabelasSQL> getListaTabelas() throws SQLException {
		ArrayList<TabelasSQL> listaTabelas = new ArrayList<>();

		RSAdo = SQL.Select_Table("SELECT TABLE_NAME FROM information_schema.tables ORDER BY TABLE_NAME");
		while (RSAdo.next()) {
			TabelasSQL tabelas = new TabelasSQL();
			tabelas.setNomeTabela(RSAdo.getString("TABLE_NAME"));
			listaTabelas.add(tabelas);
		}
		RSAdo.close();
		return listaTabelas;
	}
	
	protected String getDiretorioDestinoArquivo() throws SQLException {
		String sDiretorio = "";
		
		RSAdo = SQL.Select_Table("SELECT tp_DiretorioArquivoJava FROM tbd_ConfiguracaoSistema");
		if (RSAdo.next()) {
			sDiretorio = RSAdo.getString("tp_DiretorioArquivoJava");
		}
		RSAdo.close();
		
		return sDiretorio;
	}
	
	protected ArrayList<ColunasSQL> getListaColunas(String nomeTabela) throws SQLException {
		ArrayList<ColunasSQL> listaColunas = new ArrayList<>();

		RSAdo = SQL.Select_Table("SELECT COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH FROM information_schema.COLUMNS WHERE TABLE_NAME = '" + nomeTabela + "' ORDER BY ORDINAL_POSITION");
		while (RSAdo.next()) {
			ColunasSQL colunas = new ColunasSQL();
			colunas.setNomeColuna(RSAdo.getString("COLUMN_NAME"));
			colunas.setTipoColuna(RSAdo.getString("DATA_TYPE"));
			colunas.setTamanhoColuna(RSAdo.getString("CHARACTER_MAXIMUM_LENGTH"));
			listaColunas.add(colunas);
		}
		RSAdo.close();
		return listaColunas;
	}

	protected ResultSet executarSelect(String tabela, String colunas) throws SQLException {
		return RSAdo = SQL.Select_Table("SELECT " + colunas + " FROM " + tabela);
	}
	
}
