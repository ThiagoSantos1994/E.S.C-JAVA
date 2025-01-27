package br.com.esc.backend.mapper;

import br.com.esc.backend.domain.ChaveKeyDAO;
import br.com.esc.backend.domain.ChaveKeySemUsoDAO;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class ChaveKeySemUsoMapper implements RowMapper<ChaveKeySemUsoDAO> {

    @Override
    public ChaveKeySemUsoDAO map(ResultSet rs, StatementContext ctx) throws SQLException {
        return ChaveKeySemUsoDAO.builder()
                .chave(rs.getInt("rangestart"))
                .build();
    }
}
