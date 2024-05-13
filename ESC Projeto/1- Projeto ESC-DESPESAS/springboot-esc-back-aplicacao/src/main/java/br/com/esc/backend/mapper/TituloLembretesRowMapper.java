package br.com.esc.backend.mapper;

import br.com.esc.backend.domain.TituloLembretesDAO;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class TituloLembretesRowMapper implements RowMapper<TituloLembretesDAO> {

    @Override
    public TituloLembretesDAO map(ResultSet rs, StatementContext ctx) throws SQLException {
        return TituloLembretesDAO.builder()
                .idLembrete(rs.getInt("id_Lembrete"))
                .idFuncionario(rs.getInt("id_Funcionario"))
                .dsTituloLembrete(rs.getString("ds_TituloLembrete"))
                .build();
    }
}
