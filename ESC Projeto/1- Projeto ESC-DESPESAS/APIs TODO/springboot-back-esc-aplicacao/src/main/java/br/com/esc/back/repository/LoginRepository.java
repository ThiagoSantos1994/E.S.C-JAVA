package br.com.esc.back.repository;

import br.com.esc.back.domain.DadosLogin;
import br.com.esc.back.mappers.DadosLoginMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class LoginRepository {
    private final Logger logger = LoggerFactory.getLogger(getClass());

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

        logger.info("Consulta: " + sQuery);

        List<DadosLogin> dadosLogin = jdbcTemplate.query(
                sQuery,
                new Object[]{id_Login},
                new DadosLoginMapper());

        return dadosLogin;
    }
}
