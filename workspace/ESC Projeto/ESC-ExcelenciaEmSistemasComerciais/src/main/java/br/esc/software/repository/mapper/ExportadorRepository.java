package br.esc.software.repository.mapper;

import br.esc.software.domain.exportador.ColunasSQL;
import br.esc.software.domain.exportador.TabelasSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static br.esc.software.commons.utils.GlobalUtils.LogErro;

//@Repository
public class ExportadorRepository {

    @Autowired
    protected JdbcTemplate jdbcTemplate;
    private DataSource dataSource;

    private TabelaMapper tabelaMapper;
    private ColunaMapper colunaMapper;
    private DiretorioMapper diretorioMapper;

    @Autowired
    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
        this.jdbcTemplate = new JdbcTemplate(dataSource);

        tabelaMapper = new TabelaMapper();
        colunaMapper = new ColunaMapper();
        diretorioMapper = new DiretorioMapper();
    }

    public ArrayList<TabelasSQL> getListaTabelas() throws Exception {
        try {
            ArrayList<TabelasSQL> listaTabelas = (ArrayList<TabelasSQL>) jdbcTemplate.query("SELECT TABLE_NAME FROM information_schema.tables ORDER BY TABLE_NAME", tabelaMapper);
            return listaTabelas;
        } catch (Exception e) {
            LogErro("Ocorreu um erro ao executar o metodo getListaTabelas ->> " + e);
            throw new Exception(e);
        }
    }

    public ArrayList<ColunasSQL> getListaColunas(String nomeTabela) throws Exception {
        try {
            ArrayList<ColunasSQL> listaColunas = (ArrayList<ColunasSQL>) jdbcTemplate.query("SELECT COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH FROM information_schema.COLUMNS WHERE TABLE_NAME = '" + nomeTabela + "' ORDER BY ORDINAL_POSITION", colunaMapper);
            return listaColunas;
        } catch (Exception e) {
            LogErro("Ocorreu um erro ao executar o metodo getListaColunas ->> " + e);
            throw new Exception(e);
        }
    }

    public ResultSet executarSelect(String tabela, String colunas) {
        ResultSet resultSet = jdbcTemplate.queryForObject("SELECT " + colunas + " FROM " + tabela, new Object[]{}, new RowMapper<ResultSet>() {
            @Override
            public ResultSet mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                return resultSet;
            }
        });

        return resultSet;
    }

    public String getDiretorioDestinoArquivo() throws Exception {
        List<String> sDiretorio = jdbcTemplate.query("SELECT tp_DiretorioArquivoJava FROM tbd_ConfiguracaoSistema", diretorioMapper);
        return sDiretorio.get(0);
    }

    public String montaColunaTabela(String nomeTabela) throws Exception {
        String strColunas = "";

        try {
            for (ColunasSQL colunas : this.getListaColunas(nomeTabela)) {
                strColunas = strColunas.concat(colunas.getNomeColuna() + ",");
            }
            return strColunas.substring(0, (strColunas.length() - 1));
        } catch (Exception e) {
            LogErro("Ocorreu um erro ao executar o metodo montaColunaTabela ->> " + e);
            throw new Exception(e);
        }
    }
}
