package br.esc.software.configuration;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

import static br.esc.software.commons.utils.GlobalUtils.getProperties;

@Configuration
@ComponentScan("com..jdbc")
public class SpringJdbcConfig {

    private static final String servidor = getProperties().getProperty("prop.server");
    private static final String banco = getProperties().getProperty("prop.database");
    private static final String usuario = getProperties().getProperty("prop.user");
    private static final String senha = getProperties().getProperty("prop.password");

    @Bean
    public DataSource mysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl("jdbc:sqlserver://" + servidor + ";databaseName=" + banco);
        dataSource.setUsername(usuario);
        dataSource.setPassword(senha);

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(mysqlDataSource());
    }

}
