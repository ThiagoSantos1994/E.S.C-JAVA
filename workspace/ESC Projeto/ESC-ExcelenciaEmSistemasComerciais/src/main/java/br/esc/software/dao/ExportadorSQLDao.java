package br.esc.software.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.esc.software.domain.ColunasSQL;
import br.esc.software.domain.TabelasSQL;
import br.esc.software.persistence.ExportadorSQLPersistence;

public class ExportadorSQLDao extends ExportadorSQLPersistence {
	
	public ArrayList<TabelasSQL> getListaTabelas() throws SQLException {
		return super.getListaTabelas();
	}
	
	public String getDiretorioDestinoArquivo() throws SQLException {
		return super.getDiretorioDestinoArquivo();
	}
	
	public ArrayList<ColunasSQL> getListaColunas(String tabela) throws SQLException {
		return super.getListaColunas(tabela);
	}
	
	public ResultSet executarSelect(String tabela, String colunas) throws SQLException {
		return super.executarSelect(tabela, colunas);
	}
	
	public String montaColunaTabela(String nomeTabela) throws SQLException {
		String strColunas = "";
		ArrayList<ColunasSQL> colunasDAO = this.getListaColunas(nomeTabela);
		for (ColunasSQL colunas : colunasDAO) {
			strColunas = strColunas.concat(colunas.getNomeColuna() + ",");
		}
		return strColunas.substring(0, (strColunas.length() - 1));
	}
}
