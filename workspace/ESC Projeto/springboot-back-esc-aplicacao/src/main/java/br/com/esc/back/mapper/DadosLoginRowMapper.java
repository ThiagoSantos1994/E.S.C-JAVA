package br.com.esc.back.mapper;

import br.com.esc.back.domain.DadosLogin;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class DadosLoginRowMapper implements RowMapper<DadosLogin> {

    @Override
    public DadosLogin map(ResultSet rs, StatementContext ctx) throws SQLException {
        DadosLogin login = new DadosLogin();

        login.setId_Login(rs.getInt("id_Login"));
        login.setDs_NomeLogin(rs.getString("ds_NomeLogin"));
        login.setTp_UsuarioBloqueado(rs.getString("tp_UsuarioBloqueado"));
        login.setTp_PermiteExcluirPedidos(rs.getString("tp_PermiteExcluirPedidos"));
        login.setTp_UsuarioBloqueado(rs.getString("tp_UsuarioBloqueado"));
        login.setTp_FuncionarioExcluido(rs.getString("tp_FuncionarioExcluido"));
        login.setTp_GravaSenha(rs.getInt("tp_GravaSenha"));

        return login;
    }
}
