package br.esc.software.repository.mapper;

import br.esc.software.domain.exportador.ColunasSQL;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ColunaMapper implements RowMapper<ColunasSQL> {

    @Override
    public ColunasSQL mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        ColunasSQL colunasSQL = new ColunasSQL();
        colunasSQL.setNomeColuna(resultSet.getString("COLUMN_NAME"));
        colunasSQL.setTipoColuna(resultSet.getString("DATA_TYPE"));
        colunasSQL.setTamanhoColuna(resultSet.getString("CHARACTER_MAXIMUM_LENGTH"));
        return colunasSQL;
    }
}
