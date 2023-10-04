package br.com.esc.backend.infraestructure.database;

import br.com.esc.backend.domain.DespesasFixasMensaisDAO;
import br.com.esc.backend.domain.DespesasParceladasQuitacaoDAO;
import br.com.esc.backend.domain.DespesasMensaisDAO;
import br.com.esc.backend.mapper.DespesasFixasMensaisRowMapper;
import br.com.esc.backend.mapper.DespesasParceladasQuitacaoRowMapper;
import br.com.esc.backend.mapper.DespesasMensaisRowMapper;
import br.com.esc.backend.repository.AplicacaoRepository;
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

import java.math.BigDecimal;
import java.util.List;

@UseClasspathSqlLocator
public interface JdbiRepository extends AplicacaoRepository {

    @Override
    @SqlQuery
    @UseRowMapper(DespesasFixasMensaisRowMapper.class)
    List<DespesasFixasMensaisDAO> obterDespesasFixasMensais(String dsMes, String dsAno, Integer idFuncionario);

    @Override
    @SqlQuery
    @UseRowMapper(DespesasMensaisRowMapper.class)
    List<DespesasMensaisDAO> obterDespesasMensais(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    @UseRowMapper(DespesasMensaisRowMapper.class)
    List<DespesasMensaisDAO> obterDespesasMensaisPorID(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    BigDecimal obterCalculoTotalDespesa(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    BigDecimal obterCalculoTotalDespesaPendente(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    BigDecimal obterCalculoTotalDespesaPaga(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    BigDecimal obterCalculoDespesaTipoRelatorio(Integer idDespesa, Integer idDespesaLinkRelatorio, Integer idFuncionario);

    @Override
    @SqlQuery
    List<BigDecimal> obterCalculoReceitaPositivaMES(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    BigDecimal obterCalculoReceitaNegativaMES(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    BigDecimal obterCalculoReceitaPendentePgtoMES(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    BigDecimal obterCalculoSaldoInicialMES(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    BigDecimal obterQuantidadeDespesasParceladasMes(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    @UseRowMapper(DespesasParceladasQuitacaoRowMapper.class)
    DespesasParceladasQuitacaoDAO obterQuantidadeDespesasParceladasQuitacaoMes(Integer idDespesa, Integer idFuncionario);
}
