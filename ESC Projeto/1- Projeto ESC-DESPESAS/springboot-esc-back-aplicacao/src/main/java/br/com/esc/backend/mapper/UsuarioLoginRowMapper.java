package br.com.esc.backend.mapper;

import br.com.esc.backend.domain.LoginDAO;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class UsuarioLoginRowMapper implements RowMapper<LoginDAO> {

    @Override
    public LoginDAO map(ResultSet rs, StatementContext ctx) throws SQLException {
        return LoginDAO.builder()
                .idLogin(rs.getInt("id_Login"))
                .dsLogin(rs.getString("ds_NomeLogin"))
                .dsSenha(rs.getString("ds_SenhaLogin"))
                .isUsuarioBloqueado(rs.getString("tp_UsuarioBloqueado"))
                .isUsuarioExcluido(rs.getString("tp_FuncionarioExcluido"))
                .isGravaSenha(rs.getString("tp_GravaSenha"))
                .build();
    }
}
