package br.com.esc.backend.mapper;

import br.com.esc.backend.domain.TituloConsolidacao;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class TituloConsolidacaoRowMapper implements RowMapper<TituloConsolidacao> {

    @Override
    public TituloConsolidacao map(ResultSet rs, StatementContext ctx) throws SQLException {
        return TituloConsolidacao.builder()
                .idConsolidacao(rs.getInt("id_Consolidacao"))
                .tituloConsolidacao(rs.getString("ds_TituloConsolidacao"))
                .build();
    }
}
