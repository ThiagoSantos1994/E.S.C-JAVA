package br.com.esc.backend.mapper;

import br.com.esc.backend.domain.TituloDespesa;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class TituloDespesaMensalRowMapper implements RowMapper<TituloDespesa> {

    @Override
    public TituloDespesa map(ResultSet rs, StatementContext ctx) throws SQLException {
        return TituloDespesa.builder()
                .idDetalheDespesa(rs.getInt("id_DetalheDespesa"))
                .tituloDespesa(rs.getString("ds_NomeDespesa"))
                .build();
    }
}
