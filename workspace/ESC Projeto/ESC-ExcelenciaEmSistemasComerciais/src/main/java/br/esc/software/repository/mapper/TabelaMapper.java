package br.esc.software.repository.mapper;

import br.esc.software.domain.exportador.TabelasSQL;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TabelaMapper implements RowMapper<TabelasSQL> {

    @Override
    public TabelasSQL mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        TabelasSQL tabelasSQL = new TabelasSQL();
        tabelasSQL.setNomeTabela(resultSet.getString("TABLE_NAME"));
        return tabelasSQL;
    }
}
