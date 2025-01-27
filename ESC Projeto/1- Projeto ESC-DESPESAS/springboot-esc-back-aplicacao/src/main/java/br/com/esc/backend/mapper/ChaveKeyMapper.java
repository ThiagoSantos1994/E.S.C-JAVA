package br.com.esc.backend.mapper;

import br.com.esc.backend.domain.ChaveKeyDAO;
import br.com.esc.backend.domain.ParcelasDAO;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class ChaveKeyMapper implements RowMapper<ChaveKeyDAO> {

    @Override
    public ChaveKeyDAO map(ResultSet rs, StatementContext ctx) throws SQLException {
        return ChaveKeyDAO.builder()
                .idChaveKey(rs.getInt("id_ChaveKey"))
                .novaChave(rs.getInt("novaChave"))
                .dsNomeTabela(rs.getString("ds_NomeTabela"))
                .dsNomeColuna(rs.getString("ds_NomeColuna"))
                .build();
    }
}
