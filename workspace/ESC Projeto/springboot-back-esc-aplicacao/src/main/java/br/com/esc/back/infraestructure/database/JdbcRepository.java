package br.com.esc.back.infraestructure.database;

import br.com.esc.back.domain.DadosLogin;
import br.com.esc.back.mapper.DadosLoginRowMapper;
import br.com.esc.back.repository.EscAplicacaoRepository;
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

import java.util.List;

@UseClasspathSqlLocator
public interface JdbcRepository extends EscAplicacaoRepository {

    @Override
    @SqlQuery
    @UseRowMapper(DadosLoginRowMapper.class)
    List<DadosLogin> obterDadosLogin(Integer idLogin);
}
