package br.com.esc.backend.mapper;

import br.com.esc.backend.domain.RelatorioDespesasParceladasQuitacaoDAO;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class RelatorioDespesasParceladasQuitacaoRowMapper implements RowMapper<RelatorioDespesasParceladasQuitacaoDAO> {

    @Override
    public RelatorioDespesasParceladasQuitacaoDAO map(ResultSet rs, StatementContext ctx) throws SQLException {
        return RelatorioDespesasParceladasQuitacaoDAO.builder()
                .dsTituloDespesaParcelada(rs.getString("ds_TituloDespesaParcelada"))
                .valorDespesa(rs.getString("VLR_DESPESA"))
                .build();
    }
}
