package br.com.esc.backend.infraestructure.database;

import br.com.esc.backend.domain.*;
import br.com.esc.backend.mapper.*;
import br.com.esc.backend.repository.AplicacaoRepository;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

import java.math.BigDecimal;
import java.util.List;

@UseClasspathSqlLocator
public interface JdbiRepository extends AplicacaoRepository {

    @Override
    @SqlQuery
    @UseRowMapper(DespesasFixasMensaisRowMapper.class)
    List<DespesasFixasMensaisDAO> getDespesasFixasMensais(String dsMes, String dsAno, Integer idFuncionario);

    @Override
    @SqlQuery
    @UseRowMapper(DespesasFixasMensaisRowMapper.class)
    DespesasFixasMensaisDAO getDespesaFixaMensalPorFiltro(Integer idDespesa, Integer idOrdem, Integer idFuncionario);

    @Override
    @SqlQuery
    @UseRowMapper(LancamentosMensaisRowMapper.class)
    List<LancamentosMensaisDAO> getLancamentosMensais(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    @UseRowMapper(DespesasMensaisRowMapper.class)
    List<DespesasMensaisDAO> getDespesasMensais(Integer idDespesa, Integer idFuncionario, Integer idDetalheDespesa);

    @Override
    @SqlQuery
    @UseRowMapper(DespesasMensaisRowMapper.class)
    DespesasMensaisDAO getDespesaMensalPorFiltro(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    BigDecimal getCalculoTotalDespesa(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    BigDecimal getCalculoTotalDespesaPendente(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    BigDecimal getCalculoTotalDespesaPaga(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    BigDecimal getCalculoDespesaTipoRelatorio(Integer idDespesa, Integer idDespesaLinkRelatorio, Integer idFuncionario);

    @Override
    @SqlQuery
    List<BigDecimal> getCalculoReceitaPositivaMES(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    BigDecimal getCalculoReceitaNegativaMES(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    BigDecimal getCalculoReceitaPendentePgtoMES(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    BigDecimal getCalculoSaldoInicialMES(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    BigDecimal getQuantidadeDespesasParceladasMes(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    @UseRowMapper(DespesasParceladasQuitacaoRowMapper.class)
    DespesasParceladasQuitacaoDAO getQuantidadeDespesasParceladasQuitacaoMes(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    @UseRowMapper(DetalheDespesasMensaisRowMapper.class)
    List<DetalheDespesasMensaisDAO> getDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    @UseRowMapper(DetalheDespesasMensaisRowMapper.class)
    DetalheDespesasMensaisDAO getDetalheDespesaMensalPorFiltro(@BindBean("detalhe") DetalheDespesasMensaisDAO mensaisDAO);

    @Override
    @SqlQuery
    @UseRowMapper(ParcelasRowMapper.class)
    ParcelasDAO getParcelaPorDataVencimento(Integer idDespesaParcelada, String dataVencimento, Integer idFuncionario);

    @Override
    @SqlQuery
    @UseRowMapper(ParcelasRowMapper.class)
    ParcelasDAO getParcelaDisponivelSemAmortizacao(Integer idDespesaParcelada, Integer idFuncionario);

    @Override
    @SqlQuery
    @UseRowMapper(ParcelasRowMapper.class)
    ParcelasDAO getUltimaParcelaDespesaParcelada(Integer idDespesaParcelada, Integer idFuncionario);

    @Override
    @SqlQuery
    String getValidaDespesaParceladaAmortizacao(Integer idDespesaParcelada, Integer idFuncionario);

    @Override
    @SqlQuery
    String getValidaDetalheDespesaComParcelaAmortizada(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    String getValidaParcelaAmortizacao(Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario);

    @Override
    @SqlQuery
    String getValidaParcelaAdiantamento(Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario);

    @Override
    @SqlQuery
    String getValidaDetalheDespesaComParcelaAdiada(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    String getValidaDespesaParceladaAdiantada(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idFuncionario);

    @Override
    @SqlQuery
    Integer getMaxOrdemDespesasFixasMensais(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    Integer getMaxOrdemDespesasMensais(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    @UseRowMapper(DespesasFixasMensaisRowMapper.class)
    DespesasFixasMensaisDAO getDespesasFixasTipoDebitoCartao(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    Integer getMaxOrdemDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    Integer getMaxOrdemDetalheDespesasTipoRelatorio(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    BigDecimal getCalculoValorDespesaTipoCartaoDebito(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    Integer getQuantidadeParcelasEmAberto(Integer idDespesaParcelada, Integer idFuncionario);

    @Override
    @SqlQuery
    Integer getCodigoEmprestimo(String dsTituloEmprestimo, Integer idFuncionario);

    @Override
    @SqlQuery
    String getUsuarioLogado(Integer idFuncionario);

    @Override
    @SqlQuery
    @UseRowMapper(DespesaParceladaRowMapper.class)
    DespesaParceladaDAO getDespesaParcelada(Integer idDespesaParcelada, Integer idFuncionario);

    @Override
    @SqlUpdate
    void insertDespesasFixasMensais(@BindBean("fixas") DespesasFixasMensaisRequest request);

    @Override
    @SqlUpdate
    void insertDespesasMensais(@BindBean("despesa") DespesasMensaisDAO despesasMensaisDAO);

    @Override
    @SqlUpdate
    void insertDetalheDespesasMensais(@BindBean("detalhe") DetalheDespesasMensaisDAO detalheDAO);

    @Override
    @SqlUpdate
    void insertNovaParcela(@BindBean("parcela") ParcelasDAO parcelasDAO);

    @Override
    @SqlUpdate
    void updateValorDetalheDespesasMensais(@BindBean("detalhe") DetalheDespesasMensaisDAO detalheDespesasMensais);

    @Override
    @SqlUpdate
    void updateDespesasFixasMensais(@BindBean("fixas") DespesasFixasMensaisRequest request);

    @Override
    @SqlUpdate
    void updateDespesasMensais(@BindBean("mensal") DespesasMensaisDAO despesasMensaisDAO);

    @Override
    @SqlUpdate
    void updateDetalheDespesasMensaisImportacao(@BindBean("detalhe") DetalheDespesasMensaisDAO detalheDespesasMensais);

    @Override
    @SqlUpdate
    void updateParcelaStatusPago(String dsObservacoes, Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario);

    @Override
    @SqlUpdate
    void updateDetalheDespesasMensais(@BindBean("detalhe") DetalheDespesasMensaisDAO detalheDespesasMensais);

    @Override
    @SqlUpdate
    void updateStatusPagamentoDetalheDespesa(String vlTotal, String vlTotalPago, String tpStatus, String dsObservacoes, String dsObservacoesComplementares, Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idFuncionario);

    @Override
    @SqlUpdate
    void updateParcelaStatusPendente(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario);

    @Override
    @SqlUpdate
    void updateParcelaStatusPendenteParcelaAdiada(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idParcela, String vlParcela, Integer idFuncionario);

    @Override
    @SqlUpdate
    void updateDespesasParceladasEmAberto(Integer idFuncionario);

    @Override
    @SqlUpdate
    void updateDespesasParceladasEncerrado(Integer idDespesaParcelada, Integer idFuncionario);

    @Override
    @SqlUpdate
    void updateStatusParcelaSemAmortizacao(Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario);

    @Override
    @SqlUpdate
    void updateParcelaStatusAdiantado(Integer idDespesa, Integer idDetalheDespesa, String dsObservacoes, Integer idParcela, Integer idDespesaParcelada, Integer idFuncionario);

    @Override
    @SqlUpdate
    void updateDetalheDespesasMensaisParcelaAdiada(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, String dsObservacoes, String dsObservacoes2, String vlTotalParcelaAdiantada, Integer idFuncionario);

    @Override
    @SqlUpdate
    void updateStatusBaixaLinhaSeparacao(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlUpdate
    void updateParcelaStatusPendenteDespesasExcluidas(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlUpdate
    void updateQuantidadeParcelasAdiantadas(Integer idDespesaParcelada, Integer idFuncionario);

    @Override
    @SqlUpdate
    void updateQuantidadeParcelasDesfazerAdiantamento(Integer idDespesaParcelada, Integer idFuncionario);

    @Override
    @SqlUpdate
    void updateDetalheDespesasMensaisDesfazerAdiantamento(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idParcela, String vlTotal, Integer idFuncionario);

    @Override
    @SqlUpdate
    void deleteDespesaParceladaImportada(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idFuncionario);

    @Override
    @SqlUpdate
    void deleteDespesasFixasMensaisTemp(Integer idFuncionario);

    @Override
    @SqlUpdate
    void deleteDespesasMensaisTemp(Integer idFuncionario);

    @Override
    @SqlUpdate
    void deleteDetalheDespesasMensaisTemp(Integer idFuncionario);

    @Override
    @SqlUpdate
    void deleteTodasDespesasFixasMensais(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlUpdate
    void deleteTodasDespesasMensais(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlUpdate
    void deleteTodosDetalhesDespesasMensais(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlUpdate
    void deleteDespesasFixasMensaisPorFiltro(Integer idDespesa, Integer idOrdem, Integer idFuncionario);

    @Override
    @SqlUpdate
    void deleteDespesasMensaisPorFiltro(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idFuncionario);

    @Override
    @SqlUpdate
    void deleteDetalheDespesasMensaisPorFiltro(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idFuncionario);

    @Override
    @SqlUpdate
    void deleteDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    @Override
    @SqlUpdate
    void deleteParcela(Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario);

    @Override
    @SqlUpdate
    void deleteParcelaDetalheDespesasMensaisAdiantada(Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario);
}
