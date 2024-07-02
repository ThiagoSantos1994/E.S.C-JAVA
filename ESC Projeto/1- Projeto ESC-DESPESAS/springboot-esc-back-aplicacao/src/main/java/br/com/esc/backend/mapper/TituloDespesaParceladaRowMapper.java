package br.com.esc.backend.mapper;

import br.com.esc.backend.domain.TituloDespesa;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

@Slf4j
public class TituloDespesaParceladaRowMapper implements RowMapper<TituloDespesa> {

    @Override
    public TituloDespesa map(ResultSet rs, StatementContext ctx) throws SQLException {
        return TituloDespesa.builder()
                .idDespesa(rs.getInt("id_DespesaParcelada"))
                .tituloDespesa(rs.getString("ds_TituloDespesaParcelada").toUpperCase(Locale.ROOT))
                .build();
    }
}
