package br.com.esc.back.mappers;

import br.com.esc.back.domain.DadosLogin;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DadosLoginMapper implements RowMapper<DadosLogin> {

    @Override
    public DadosLogin mapRow(ResultSet rs, int rowNum) throws SQLException {
        DadosLogin d = new DadosLogin();

        d.setId_Login(rs.getInt("id_Login"));
        d.setDs_NomeLogin(rs.getString("ds_NomeLogin"));
        d.setTp_UsuarioBloqueado(rs.getString("tp_UsuarioBloqueado"));
        d.setTp_PermiteExcluirPedidos(rs.getString("tp_PermiteExcluirPedidos"));
        d.setTp_UsuarioBloqueado(rs.getString("tp_UsuarioBloqueado"));
        d.setTp_FuncionarioExcluido(rs.getString("tp_FuncionarioExcluido"));
        d.setTp_GravaSenha(rs.getInt("tp_GravaSenha"));

        return d;
    }
}
