package br.esc.software.repository.exportador;

import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.domain.exportador.ColunasSQL;
import br.esc.software.domain.exportador.TabelasSQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static br.esc.software.configuration.ConnectionSQL.Select_Table;

public class ExportadorDao {

    private ResultSet RSAdo;

    public ArrayList<TabelasSQL> getListaTabelas() throws SQLException, ExcecaoGlobal {
        ArrayList<TabelasSQL> listaTabelas = new ArrayList<>();

        RSAdo = Select_Table("SELECT TABLE_NAME FROM information_schema.tables ORDER BY TABLE_NAME");
        while (RSAdo.next()) {
            TabelasSQL tabelas = new TabelasSQL();
            tabelas.setNomeTabela(RSAdo.getString("TABLE_NAME"));
            listaTabelas.add(tabelas);
        }
        RSAdo.close();
        return listaTabelas;
    }

    public String getDiretorioDestinoArquivo() throws SQLException, ExcecaoGlobal {
        String sDiretorio = "";

        RSAdo = Select_Table("SELECT tp_DiretorioArquivoJava FROM tbd_ConfiguracaoSistema");
        if (RSAdo.next()) {
            sDiretorio = RSAdo.getString("tp_DiretorioArquivoJava");
        }
        RSAdo.close();

        return sDiretorio;
    }

    public ArrayList<ColunasSQL> getListaColunas(String nomeTabela) throws SQLException, ExcecaoGlobal {
        ArrayList<ColunasSQL> listaColunas = new ArrayList<>();

        RSAdo = Select_Table("SELECT COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH FROM information_schema.COLUMNS WHERE TABLE_NAME = '" + nomeTabela + "' ORDER BY ORDINAL_POSITION");
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

    public ResultSet executarSelect(String tabela, String colunas) throws SQLException, ExcecaoGlobal {
        return RSAdo = Select_Table("SELECT " + colunas + " FROM " + tabela);
    }

    public String montaColunaTabela(String nomeTabela) throws SQLException, ExcecaoGlobal {
        String strColunas = "";
        ArrayList<ColunasSQL> colunasDAO = this.getListaColunas(nomeTabela);
        for (ColunasSQL colunas : colunasDAO) {
            strColunas = strColunas.concat(colunas.getNomeColuna() + ",");
        }
        return strColunas.substring(0, (strColunas.length() - 1));
    }
}
