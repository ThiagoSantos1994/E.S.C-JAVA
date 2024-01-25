package br.com.esc.backend.config;

import br.com.esc.backend.infraestructure.database.JdbiAutenticacaoRepository;
import br.com.esc.backend.infraestructure.database.JdbiRepository;
import br.com.esc.backend.infraestructure.database.JdbiBackupRepository;
import br.com.esc.backend.repository.AplicacaoRepository;
import br.com.esc.backend.repository.AutenticacaoRepository;
import br.com.esc.backend.repository.BackupRepository;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.spi.JdbiPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class JdbiConfiguration {

    @Bean
    public Jdbi jdbi(DataSource ds, List<JdbiPlugin> jdbiPlugins, List<RowMapper<?>> rowMappers) {
        TransactionAwareDataSourceProxy dataSourceProxy = new TransactionAwareDataSourceProxy(ds);
        Jdbi jdbi = Jdbi.create(dataSourceProxy);

        jdbiPlugins.forEach(jdbi::installPlugin);
        rowMappers.forEach(jdbi::registerRowMapper);

        return jdbi;
    }

    @Bean
    public JdbiPlugin sqlObjectPlugin() {
        return new SqlObjectPlugin();
    }

    @Bean
    public AplicacaoRepository jdbiRepository(Jdbi jdbi) {
        return jdbi.onDemand(JdbiRepository.class);
    }

    @Bean
    public BackupRepository jdbiBackupRepository(Jdbi jdbi) {
        return jdbi.onDemand(JdbiBackupRepository.class);
    }

    @Bean
    public AutenticacaoRepository jdbiAutenticacaoRepository(Jdbi jdbi) {
        return jdbi.onDemand(JdbiAutenticacaoRepository.class);
    }
}
