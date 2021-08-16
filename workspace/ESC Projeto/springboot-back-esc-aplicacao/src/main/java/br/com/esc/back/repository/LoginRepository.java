package br.com.esc.back.repository;

import br.com.esc.back.domain.DadosLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class LoginRepository {

    private JdbcTemplate jdbcTemplate;
    private DataSource dataSource;

    @Autowired
    public LoginRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<DadosLogin> obterDadosLogin(Integer id_Login) throws Exception {

        String sQuery = "SELECT id_Login, ds_NomeLogin, tp_UsuarioBloqueado, tp_PermiteExcluirPedidos, tp_UsuarioBloqueado, tp_FuncionarioExcluido, tp_GravaSenha " +
                "FROM tbd_Login " +
                "WHERE id_Login = ?";

        List<DadosLogin> dadosLogin = jdbcTemplate.query(
                sQuery,
                new Object[]{id_Login},
                new RowMapper() {
                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
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
        );

        return dadosLogin;
    }
}
