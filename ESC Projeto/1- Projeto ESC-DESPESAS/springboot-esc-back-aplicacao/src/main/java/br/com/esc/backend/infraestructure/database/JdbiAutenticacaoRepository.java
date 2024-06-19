package br.com.esc.backend.infraestructure.database;

import br.com.esc.backend.domain.LoginDAO;
import br.com.esc.backend.domain.SessaoDAO;
import br.com.esc.backend.mapper.SessaoRowMapper;
import br.com.esc.backend.mapper.UsuarioLoginRowMapper;
import br.com.esc.backend.repository.AutenticacaoRepository;
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

import java.util.List;

@UseClasspathSqlLocator
public interface JdbiAutenticacaoRepository extends AutenticacaoRepository {

    @Override
    @SqlQuery
    @UseRowMapper(UsuarioLoginRowMapper.class)
    List<LoginDAO> getLoginUsuario();

    @Override
    @SqlQuery
    @UseRowMapper(SessaoRowMapper.class)
    SessaoDAO getHorarioLoginAuditoriaAcesso(Integer idFuncionario);

    @Override
    @SqlUpdate
    void insertAuditoriaAcesso(Integer idFuncionario, String dataHoraLogin, String idMaquina);
}
