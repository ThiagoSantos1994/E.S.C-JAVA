package br.com.esc.backend.mapper;

import br.com.esc.backend.domain.ExtratoDespesasDAO;
import br.com.esc.backend.domain.ParcelasDAO;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class ExtratoDespesasRowMapper implements RowMapper<ExtratoDespesasDAO> {

    @Override
    public ExtratoDespesasDAO map(ResultSet rs, StatementContext ctx) throws SQLException {
        return ExtratoDespesasDAO.builder()
                .qtDespesas(rs.getInt("QTDE_DESPESAS"))
                .vlDespesas(rs.getString("VLR_DESPESAS"))
                .build();
    }
}
