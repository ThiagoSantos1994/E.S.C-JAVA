package br.com.esc.backend.mapper;

import br.com.esc.backend.domain.DespesasParceladasQuitacaoDAO;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class DespesasParceladasQuitacaoRowMapper implements RowMapper<DespesasParceladasQuitacaoDAO> {

    @Override
    public DespesasParceladasQuitacaoDAO map(ResultSet rs, StatementContext ctx) throws SQLException {
        DespesasParceladasQuitacaoDAO dao = new DespesasParceladasQuitacaoDAO();

        dao.setQtde_Parcelas(rs.getInt("qtde_Parcelas"));
        dao.setVl_Parcelas(rs.getBigDecimal("vl_Parcelas"));

        log.info("DespesasParceladasQuitacaoRowMapper >> {}", dao);
        return dao;
    }
}
