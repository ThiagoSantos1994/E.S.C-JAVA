package br.com.esc.backend.infraestructure.database;

import br.com.esc.backend.domain.*;
import br.com.esc.backend.mapper.*;
import br.com.esc.backend.repository.AplicacaoRepository;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.Define;
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
    List<DespesasFixasMensaisDAO> getDespesasFixasMensaisPorID(Integer idDespesa, Integer idFuncionario);

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
    BigDecimal getQuantidadeDetalheDespesasParceladasMes(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    @UseRowMapper(DespesasParceladasQuitacaoRowMapper.class)
    DespesasParceladasQuitacaoDAO getQuantidadeDespesasParceladasQuitacaoMes(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    @UseRowMapper(DetalheDespesasMensaisRowMapper.class)
    List<DetalheDespesasMensaisDAO> getDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, @Define("ordem") String ordem);

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
    List<ParcelasDAO> getParcelasPorFiltro(Integer idDespesaParcelada, Integer idParcela, String tpBaixado, Integer idFuncionario);

    @Override
    @SqlQuery
    @UseRowMapper(ParcelasRowMapper.class)
    List<ParcelasDAO> getParcelasParaAmortizacao(Integer idDespesaParcelada, Integer idFuncionario);

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
    String getValorTotalDespesaParcelada(Integer idDespesaParcelada, Integer idFuncionario);

    @Override
    @SqlQuery
    String getValorTotalDespesaParceladaPaga(Integer idDespesaParcelada, Integer idFuncionario);

    @Override
    @SqlQuery
    String getParcelaAtual(Integer idDespesaParcelada, Integer idFuncionario);

    @Override
    @SqlQuery
    String getDespesaParceladaVinculada(Integer idDespesaParcelada, Integer idFuncionario);

    @Override
    @SqlQuery
    String getValorTotalDespesaParceladaPendente(Integer idDespesaParcelada, Integer idFuncionario);

    @Override
    @SqlQuery
    Integer getQuantidadeParcelasPagas(Integer idDespesaParcelada, Integer idFuncionario);

    @Override
    @SqlQuery
    Integer getQuantidadeParcelas(Integer idDespesaParcelada, Integer idFuncionario);

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
    String getValidaDetalheDespesaParceladaAdiantada(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idFuncionario);

    @Override
    @SqlQuery
    Integer getMaxOrdemDespesasFixasMensais(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    Integer getMaxOrdemDespesasMensais(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    Integer getValidaDespesaExistenteDebitoCartao(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    Integer getValidaDespesaExistente(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    Integer getIDDetalheDespesaPorTitulo(String dsNomeDespesa, Integer idFuncionario);

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
    DespesaParceladaDAO getDespesaParcelada(Integer idDespesaParcelada, String dsNomeDespesaParcelada, Integer idFuncionario);

    @Override
    @SqlQuery
    @UseRowMapper(DespesaParceladaRowMapper.class)
    List<DespesaParceladaDAO> getDespesasParceladas(Integer idFuncionario, @Define("status") String status);

    @Override
    @SqlQuery
    @UseRowMapper(ChaveKeyMapper.class)
    ChaveKeyDAO getNovaChaveKey(String tpRegistroKey);

    @Override
    @SqlQuery
    Integer getMaxIdDespesaTemp(Integer idFuncionario);

    @Override
    @SqlQuery
    Integer getMaxIdDespesa(Integer idFuncionario);

    @Override
    @SqlQuery
    String getMesAnoPorID(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    String getMesAnoPorIDTemp(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    String getObservacoesDetalheDespesa(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idFuncionario);

    @Override
    @SqlQuery
    @UseRowMapper(ExtratoDespesasRowMapper.class)
    ExtratoDespesasDAO getExtratoDespesasMes(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    @UseRowMapper(ExtratoDespesasRowMapper.class)
    ExtratoDespesasDAO getExtratoDespesasParceladasMes(String dsMes, String dsAno, Integer idFuncionario);

    @Override
    @SqlQuery
    Integer getValidaTituloDespesaDuplicado(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String dsTituloDespesa);

    @Override
    @SqlQuery
    List<String> getTituloDespesa(Integer idFuncionario);

    @Override
    @SqlQuery
    @UseRowMapper(TituloDespesaParceladaRowMapper.class)
    List<TituloDespesa> getNomeDespesasParceladas(Integer idFuncionario);

    @Override
    @SqlQuery
    @UseRowMapper(TituloDespesaParceladaRowMapper.class)
    List<TituloDespesa> getNomeDespesasParceladasParaImportacao(Integer idFuncionario);

    @Override
    @SqlQuery
    @UseRowMapper(TituloDespesaRowMapper.class)
    List<TituloDespesa> getNomeDespesaRelatorio(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    List<String> getTituloDespesaEmprestimoAReceber(Integer idFuncionario);

    @Override
    @SqlQuery
    String getTituloDespesaEmprestimoPorID(Integer idEmprestimo, Integer idFuncionario);

    @Override
    @SqlQuery
    String getValorParcelaPorFiltro(@Define("sWhere") String sWhere);

    @Override
    @SqlQuery
    String getMaxValorParcela(Integer idDespesaParcelada, Integer idFuncionario);

    @Override
    @SqlQuery
    String getNomeDespesaParceladaPorFiltro(Integer idDespesaParcelada, Integer idFuncionario);

    @Override
    @SqlQuery
    Integer getValidaTituloDespesaParceladaExistente(String dsTituloDespesaParcelada, Integer idDespesaParcelada, Integer idFuncionario);

    @Override
    @SqlQuery
    @UseRowMapper(RelatorioDespesasParceladasQuitacaoRowMapper.class)
    List<RelatorioDespesasParceladasQuitacaoDAO> getRelatorioDespesasParceladasQuitacao(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    @UseRowMapper(ConfiguracaoLancamentosRowMapper.class)
    ConfiguracaoLancamentosResponse getConfiguracaoLancamentos(Integer idFuncionario);

    @Override
    @SqlQuery
    String getValorTotalDespesa(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    Boolean getStatusDetalheDespesaPendentePagamento(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    @Override
    @SqlQuery
    @UseRowMapper(LembretesRowMapper.class)
    List<LembretesDAO> getMonitorLembretes(Integer idFuncionario, String tpBaixado, @Define("whereSemanal") String sWhereSemanal);

    @Override
    @SqlQuery
    @UseRowMapper(TituloLembretesRowMapper.class)
    List<TituloLembretesDAO> getTituloLembretes(Integer idFuncionario, String tpBaixado);

    @Override
    @SqlQuery
    @UseRowMapper(LembretesRowMapper.class)
    LembretesDAO getLembreteDetalhe(Integer idLembrete, Integer idFuncionario);

    @Override
    @SqlQuery
    String getDiaSemana();

    @Override
    @SqlQuery
    List<String> getListaAnoReferencia();

    @Override
    @SqlUpdate
    void insertDespesaFixaTemp(Integer idDespesaTemp, Integer dsMesTemp, Integer dsAnoTemp, Integer idFuncionario);

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
    void insertObservacaoDetalheDespesaMensal(@BindBean("detalhe") ObservacoesDetalheDespesaRequest detalheDAO);

    @Override
    @SqlUpdate
    void insertParcela(@BindBean("parcela") ParcelasDAO parcelasDAO);

    @Override
    @SqlUpdate
    void insertDespesaParcelada(@BindBean("despesa") DespesaParceladaDAO despesaParceladaDAO);

    @Override
    @SqlUpdate
    void insertLembrete(@BindBean("lembrete") LembretesDAO lembretesDAO);

    @Override
    @SqlUpdate
    void insertDataConfiguracoesLancamentosNovo(Integer idFuncionario);

    @Override
    @SqlUpdate
    void updateLembrete(@BindBean("lembrete") LembretesDAO lembretesDAO);

    @Override
    @SqlUpdate
    void updateDataBaixaLembrete(Integer idLembrete, String data, Integer idFuncionario);

    @Override
    @SqlUpdate
    void updateBaixarLembrete(Integer idLembrete, Integer idFuncionario);

    @Override
    @SqlUpdate
    void updateObservacaoDetalheDespesaMensal(@BindBean("detalhe") ObservacoesDetalheDespesaRequest request);

    @Override
    @SqlUpdate
    void updateDespesaParcelada(@BindBean("despesa") DespesaParceladaDAO despesaParceladaDAO);

    @Override
    @SqlUpdate
    void updateConfiguracoesLancamentos(@BindBean("parametro") ConfiguracaoLancamentosRequest request);

    @Override
    @SqlUpdate
    void updateDataConfiguracoesLancamentos(Integer idFuncionario, Integer mesReferencia);

    @Override
    @SqlUpdate
    void updateParcela(@BindBean("parcela") ParcelasDAO parcelasDAO);

    @Override
    @SqlUpdate
    void updateParcelasReprocessamento(String vlParcela, Integer idDespesaParcelada, Integer idFuncionario);

    @Override
    @SqlUpdate
    void updateValorDetalheDespesasMensais(@BindBean("detalhe") DetalheDespesasMensaisDAO detalheDespesasMensais);

    @Override
    @SqlUpdate
    void updateValorTotalDetalheDespesasMensaisParcelas(String vlTotal, Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario, String status);

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
    void updateDetalheDespesasMensaisOrdenacao(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idOrdem, Integer idNovaOrdem, Integer idFuncionario);

    @Override
    @SqlUpdate
    void updateObservacaoDetalheDespesasMensaisOrdenacao(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idNovaOrdem, Integer idFuncionario);

    @Override
    @SqlUpdate
    void updateDespesasMensaisOrdenacao(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idNovaOrdem, Integer idFuncionario);

    @Override
    @SqlUpdate
    void updateDespesasFixasMensaisOrdenacao(Integer idDespesa, Integer idOrdem, Integer idNovaOrdem, Integer idFuncionario);

    @Override
    @SqlUpdate
    void updateParcelaStatusPago(String dsObservacoes, Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario);

    @Override
    @SqlUpdate
    void updateParcelaStatusQuitado(String dsObservacoes, Integer idDespesaParcelada, Integer idParcela, String valorQuitacao, Integer idFuncionario);

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
    void updateParcelaStatusAmortizado(Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario);

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
    void updateDetalheDespesasMensaisSemRelatorio(Integer idDespesa, Integer idDespesaLinkRelatorio, Integer idFuncionario);

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
    void updateChaveKeyUtilizada(Integer idChaveKey);

    @Override
    @SqlUpdate
    void updateDespesasMensaisTipoTemporario(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlUpdate
    void updateDetalheDespesasMensaisTipoTemporario(Integer idDespesa, Integer idFuncionario);

    @Override
    @SqlUpdate
    void updateDespesaMensalTituloReuso(Integer idDespesa, Integer idDetalheDespesa, Integer idDetalheDespesaNova, String dsNomeDespesa, Integer idFuncionario);

    @Override
    @SqlUpdate
    void updateDetalheDespesasMensaisID(Integer idDespesa, Integer idDetalheDespesa, Integer idDetalheDespesaNova, Integer idFuncionario);

    @Override
    @SqlUpdate
    void updateTituloDespesasMensais(Integer idDetalheDespesa, Integer idFuncionario, String dsNomeDespesa, String anoReferencia);

    @Override
    @SqlUpdate
    void updateDataRenovacaoAUTOLembrete(Integer idLembrete, Integer idFuncionario, String dataInicial);

    @Override
    @SqlUpdate
    void deleteDespesaParceladaImportada(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idFuncionario);

    @Override
    @SqlUpdate
    void deleteDetalheDespesaParcelada(Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario);

    @Override
    @SqlUpdate
    void deleteTodasParcelas(Integer idDespesaParcelada, Integer idFuncionario);

    @Override
    @SqlUpdate
    void deleteTodosDetalheDespesaParcelada(Integer idDespesaParcelada, Integer idFuncionario);

    @Override
    @SqlUpdate
    void deleteDespesasParceladas(Integer idDespesaParcelada, Integer idFuncionario);

    @Override
    @SqlUpdate
    void deleteDespesasMensaisTemp(Integer idFuncionario);

    @Override
    @SqlUpdate
    void deleteDetalheDespesasMensaisTemp(Integer idFuncionario);

    @Override
    @SqlUpdate
    void deleteDespesasFixasMensaisTemp(Integer idFuncionario);

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
    void deleteObservacaoDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idFuncionario);

    @Override
    @SqlUpdate
    void deleteTodasObservacaoDetalheDespesaMensal(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    @Override
    @SqlUpdate
    void deleteParcela(Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario);

    @Override
    @SqlUpdate
    void deleteParcelaDetalheDespesasMensaisAdiantada(Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario);

    @Override
    @SqlUpdate
    void deleteLembrete(Integer idLembrete, Integer idFuncionario);
}
