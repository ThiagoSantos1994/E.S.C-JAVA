package br.com.esc.backend.mapper;

import br.com.esc.backend.domain.LoginDAO;
import br.com.esc.backend.domain.SessaoDAO;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class SessaoRowMapper implements RowMapper<SessaoDAO> {

    @Override
    public SessaoDAO map(ResultSet rs, StatementContext ctx) throws SQLException {
        return SessaoDAO.builder()
                .dataLogin(rs.getString("data_Login"))
                .horaLogin(rs.getString("hora_Login"))
                .horaAtual(rs.getString("hora_Atual"))
                .tempoLogado(rs.getInt("tempo_Logado"))
                .build();
    }
}
