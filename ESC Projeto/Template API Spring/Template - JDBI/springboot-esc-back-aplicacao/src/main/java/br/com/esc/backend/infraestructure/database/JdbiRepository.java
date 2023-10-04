package br.com.esc.backend.infraestructure.database;

import br.com.esc.backend.domain.DadosLogin;
import br.com.esc.backend.mapper.DadosLoginRowMapper;
import br.com.esc.backend.repository.AplicacaoRepository;
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

import java.util.List;

@UseClasspathSqlLocator
public interface JdbiRepository extends AplicacaoRepository {

    @Override
    @SqlQuery
    @UseRowMapper(DadosLoginRowMapper.class)
    List<DadosLogin> obterDadosLogin(Integer idLogin);
}
