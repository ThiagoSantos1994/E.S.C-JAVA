package br.com.esc.backend.mapper;

import br.com.esc.backend.domain.CategoriaDespesasDAO;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

import static br.com.esc.backend.utils.MotorCalculoUtils.convertStringToDecimal;

@Slf4j
public class CategoriaDespesaRowMapper implements RowMapper<CategoriaDespesasDAO> {

    @Override
    public CategoriaDespesasDAO map(ResultSet rs, StatementContext ctx) throws SQLException {
        return CategoriaDespesasDAO.builder()
                .nomeCategoria(rs.getString("tp_CategoriaDespesa"))
                .vlDespesa(rs.getString("VLR_DESPESAS"))
                .build();
    }
}
