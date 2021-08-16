package br.com.esc.back.repository.connectionJDBC;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

import static br.com.esc.back.commons.GlobalUtils.getProperties;


@Configuration
@ComponentScan("com..jdbc")
public class SpringJdbcConfig {

    private String servidor = "";
    private String banco = "";
    private String usuario = "";
    private String senha = "";

    @Bean
    public DataSource mysqlDataSource() throws Exception {
        obterParametrosConexao();

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl("jdbc:sqlserver://" + servidor + ";databaseName=" + banco);
        dataSource.setUsername(usuario);
        dataSource.setPassword(senha);

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) throws Exception {
        return new JdbcTemplate(mysqlDataSource());
    }

    private void obterParametrosConexao() throws Exception {
        servidor = getProperties().getProperty("prop.server");
        banco = getProperties().getProperty("prop.database");
        usuario = getProperties().getProperty("prop.user");
        senha = getProperties().getProperty("prop.password");
    }
}
