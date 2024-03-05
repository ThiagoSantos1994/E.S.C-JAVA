package br.com.esc.backend.repository;

import br.com.esc.backend.domain.*;
import br.com.esc.backend.mapper.TituloDespesaRowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AplicacaoRepository {

    List<DespesasFixasMensaisDAO> getDespesasFixasMensais(String dsMes, String dsAno, Integer idFuncionario);

    List<DespesasFixasMensaisDAO> getDespesasFixasMensaisPorID(Integer idDespesa, Integer idFuncionario);

    DespesasFixasMensaisDAO getDespesaFixaMensalPorFiltro(Integer idDespesa, Integer idOrdem, Integer idFuncionario);

    List<LancamentosMensaisDAO> getLancamentosMensais(Integer idDespesa, Integer idFuncionario);

    List<DespesasMensaisDAO> getDespesasMensais(Integer idDespesa, Integer idFuncionario, Integer idDetalheDespesa);

    DespesasMensaisDAO getDespesaMensalPorFiltro(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    BigDecimal getCalculoTotalDespesa(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    BigDecimal getCalculoTotalDespesaPendente(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    BigDecimal getCalculoTotalDespesaPaga(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    BigDecimal getCalculoDespesaTipoRelatorio(Integer idDespesa, Integer idDespesaLinkRelatorio, Integer idFuncionario);

    List<BigDecimal> getCalculoReceitaPositivaMES(Integer idDespesa, Integer idFuncionario);

    BigDecimal getCalculoReceitaNegativaMES(Integer idDespesa, Integer idFuncionario);

    BigDecimal getCalculoReceitaPendentePgtoMES(Integer idDespesa, Integer idFuncionario);

    BigDecimal getCalculoSaldoInicialMES(Integer idDespesa, Integer idFuncionario);

    BigDecimal getQuantidadeDespesasParceladasMes(Integer idDespesa, Integer idFuncionario);

    BigDecimal getQuantidadeDetalheDespesasParceladasMes(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    DespesasParceladasQuitacaoDAO getQuantidadeDespesasParceladasQuitacaoMes(Integer idDespesa, Integer idFuncionario);

    List<DetalheDespesasMensaisDAO> getDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String ordem);

    DetalheDespesasMensaisDAO getDetalheDespesaMensalPorFiltro(DetalheDespesasMensaisDAO mensaisDAO);

    ParcelasDAO getParcelaPorDataVencimento(Integer idDespesaParcelada, String dataVencimento, Integer idFuncionario);

    ParcelasDAO getParcelaDisponivelSemAmortizacao(Integer idDespesaParcelada, Integer idFuncionario);

    ParcelasDAO getUltimaParcelaDespesaParcelada(Integer idDespesaParcelada, Integer idFuncionario);

    List<ParcelasDAO> getParcelasPorFiltro(Integer idDespesaParcelada, Integer idParcela, String tpBaixado, Integer idFuncionario);

    String getValidaDespesaParceladaAmortizacao(Integer idDespesaParcelada, Integer idFuncionario);

    String getValidaParcelaAmortizacao(Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario);

    String getValidaParcelaAdiantamento(Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario);

    String getValidaDetalheDespesaComParcelaAdiada(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    String getValidaDetalheDespesaComParcelaAmortizada(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    String getValidaDetalheDespesaParceladaAdiantada(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idFuncionario);

    String getDespesaParceladaVinculada(Integer idDespesaParcelada, Integer idFuncionario);

    Integer getMaxOrdemDespesasFixasMensais(Integer idDespesa, Integer idFuncionario);

    Integer getMaxOrdemDespesasMensais(Integer idDespesa, Integer idFuncionario);

    Integer getMaxOrdemDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    Integer getMaxOrdemDetalheDespesasTipoRelatorio(Integer idDespesa, Integer idFuncionario);

    DespesasFixasMensaisDAO getDespesasFixasTipoDebitoCartao(Integer idDespesa, Integer idFuncionario);

    BigDecimal getCalculoValorDespesaTipoCartaoDebito(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    Integer getQuantidadeParcelasEmAberto(Integer idDespesaParcelada, Integer idFuncionario);

    Integer getQuantidadeParcelas(Integer idDespesaParcelada, Integer idFuncionario);

    Integer getCodigoEmprestimo(String dsTituloEmprestimo, Integer idFuncionario);

    String getUsuarioLogado(Integer idFuncionario);

    DespesaParceladaDAO getDespesaParcelada(Integer idDespesaParcelada, String dsNomeDespesaParcelada, Integer idFuncionario);

    List<DespesaParceladaDAO> getDespesasParceladas(Integer idFuncionario, String status);

    ChaveKeyDAO getNovaChaveKey(String tpRegistroKey);

    Integer getMaxIdDespesaTemp(Integer idFuncionario);

    Integer getMaxIdDespesa(Integer idFuncionario);

    String getMesAnoPorID(Integer idDespesa, Integer idFuncionario);

    String getMesAnoPorIDTemp(Integer idDespesa, Integer idFuncionario);

    ExtratoDespesasDAO getExtratoDespesasMes(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    ExtratoDespesasDAO getExtratoDespesasParceladasMes(String dsMes, String dsAno, Integer idFuncionario);

    Integer getValidaDespesaExistenteDebitoCartao(Integer idDespesa, Integer idFuncionario);

    Integer getValidaDespesaExistente(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    Integer getIDDetalheDespesaPorTitulo(String dsNomeDespesa, Integer idFuncionario);

    Integer getValidaTituloDespesaDuplicado(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String dsTituloDespesa);

    List<String> getTituloDespesa(Integer idFuncionario);

    List<TituloDespesa> getNomeDespesasParceladas(Integer idFuncionario);

    List<TituloDespesa> getNomeDespesasParceladasParaImportacao(Integer idFuncionario);

    List<String> getTituloDespesaEmprestimoAReceber(Integer idFuncionario);

    String getTituloDespesaEmprestimoPorID(Integer idEmprestimo, Integer idFuncionario);

    String getValorParcelaPorFiltro(String sWhere);

    String getValorTotalDespesaParcelada(Integer idDespesaParcelada, Integer idFuncionario);

    String getValorTotalDespesaParceladaPaga(Integer idDespesaParcelada, Integer idFuncionario);

    String getValorTotalDespesaParceladaPendente(Integer idDespesaParcelada, Integer idFuncionario);

    String getParcelaAtual(Integer idDespesaParcelada, Integer idFuncionario);

    Integer getQuantidadeParcelasPagas(Integer idDespesaParcelada, Integer idFuncionario);

    String getMaxValorParcela(Integer idDespesaParcelada, Integer idFuncionario);

    String getNomeDespesaParceladaPorFiltro(Integer idDespesaParcelada, Integer idFuncionario);

    Integer getValidaTituloDespesaParceladaExistente(String dsTituloDespesaParcelada, Integer idDespesaParcelada, Integer idFuncionario);

    List<RelatorioDespesasParceladasQuitacaoDAO> getRelatorioDespesasParceladasQuitacao(Integer idDespesa, Integer idFuncionario);

    ConfiguracaoLancamentosResponse getConfiguracaoLancamentos(Integer idFuncionario);

    String getValorTotalDespesa(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    Boolean getStatusDetalheDespesaPendentePagamento(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    void insertDespesaFixaTemp(Integer idDespesaTemp, Integer dsMesTemp, Integer dsAnoTemp, Integer idFuncionario);

    void insertDespesasFixasMensais(DespesasFixasMensaisRequest request);

    void insertDespesasMensais(DespesasMensaisDAO despesasMensaisDAO);

    void insertDetalheDespesasMensais(DetalheDespesasMensaisDAO detalheDAO);

    void insertParcela(ParcelasDAO parcelasDAO);

    void insertDespesaParcelada(DespesaParceladaDAO despesaParceladaDAO);

    void updateParcela(ParcelasDAO parcelasDAO);

    void updateParcelasReprocessamento(String vlParcela, Integer idDespesaParcelada, Integer idFuncionario);

    void updateDespesaParcelada(DespesaParceladaDAO despesaParceladaDAO);

    void updateValorDetalheDespesasMensais(DetalheDespesasMensaisDAO detalheDespesasMensais);

    void updateValorTotalDetalheDespesasMensaisParcelas(String vlTotal, Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario, String status);

    void updateDespesasFixasMensais(DespesasFixasMensaisRequest request);

    void updateDespesasMensais(DespesasMensaisDAO despesasMensaisDAO);

    void updateDetalheDespesasMensaisImportacao(DetalheDespesasMensaisDAO detalheDespesasMensais);

    void updateDetalheDespesasMensais(DetalheDespesasMensaisDAO detalheDespesasMensais);

    void updateDetalheDespesasMensaisOrdenacao(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idOrdem, Integer idNovaOrdem, Integer idFuncionario);

    void updateDespesasMensaisOrdenacao(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idNovaOrdem, Integer idFuncionario);

    void updateDespesasFixasMensaisOrdenacao(Integer idDespesa, Integer idOrdem, Integer idNovaOrdem, Integer idFuncionario);

    void updateParcelaStatusPago(String dsObservacoes, Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario);

    void updateParcelaStatusQuitado(String dsObservacoes, Integer idDespesaParcelada, Integer idParcela, String valorQuitacao, Integer idFuncionario);

    void updateParcelaStatusPendenteDespesasExcluidas(Integer idDespesa, Integer idFuncionario);

    void updateParcelaStatusPendente(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario);

    void updateParcelaStatusPendenteParcelaAdiada(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idParcela, String vlParcela, Integer idFuncionario);

    void updateQuantidadeParcelasAdiantadas(Integer idDespesaParcelada, Integer idFuncionario);

    void updateQuantidadeParcelasDesfazerAdiantamento(Integer idDespesaParcelada, Integer idFuncionario);

    void updateDespesasParceladasEmAberto(Integer idFuncionario);

    void updateDespesasParceladasEncerrado(Integer idDespesaParcelada, Integer idFuncionario);

    void updateStatusParcelaSemAmortizacao(Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario);

    void updateParcelaStatusAdiantado(Integer idDespesa, Integer idDetalheDespesa, String dsObservacoes, Integer idParcela, Integer idDespesaParcelada, Integer idFuncionario);

    void updateStatusPagamentoDetalheDespesa(String vlTotal, String vlTotalPago, String tpStatus, String dsObservacoes, String dsObservacoesComplementares, Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idFuncionario);

    void updateStatusBaixaLinhaSeparacao(Integer idDespesa, Integer idFuncionario);

    void updateDetalheDespesasMensaisParcelaAdiada(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, String dsObservacoes, String dsObservacoes2, String vlTotalParcelaAdiantada, Integer idFuncionario);

    void updateDetalheDespesasMensaisDesfazerAdiantamento(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idParcela, String vlTotal, Integer idFuncionario);

    void updateChaveKeyUtilizada(Integer idChaveKey);

    void updateDespesasMensaisTipoTemporario(Integer idDespesa, Integer idFuncionario);

    void updateDetalheDespesasMensaisTipoTemporario(Integer idDespesa, Integer idFuncionario);

    void updateDespesaMensalTituloReuso(Integer idDespesa, Integer idDetalheDespesa, Integer idDetalheDespesaNova, String dsNomeDespesa, Integer idFuncionario);

    void updateDetalheDespesasMensaisID(Integer idDespesa, Integer idDetalheDespesa, Integer idDetalheDespesaNova, Integer idFuncionario);

    void updateTituloDespesasMensais(Integer idDetalheDespesa, Integer idFuncionario, String dsNomeDespesa, String anoReferencia);

    void deleteDespesaParceladaImportada(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idFuncionario);

    void deleteDetalheDespesaParcelada(Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario);

    void deleteTodasParcelas(Integer idDespesaParcelada, Integer idFuncionario);

    void deleteTodosDetalheDespesaParcelada(Integer idDespesaParcelada, Integer idFuncionario);

    void deleteDespesasParceladas(Integer idDespesaParcelada, Integer idFuncionario);

    void deleteDespesasMensaisTemp(Integer idFuncionario);

    void deleteDetalheDespesasMensaisTemp(Integer idFuncionario);

    void deleteTodasDespesasFixasMensais(Integer idDespesa, Integer idFuncionario);

    void deleteTodasDespesasMensais(Integer idDespesa, Integer idFuncionario);

    void deleteTodosDetalhesDespesasMensais(Integer idDespesa, Integer idFuncionario);

    void deleteDespesasFixasMensaisPorFiltro(Integer idDespesa, Integer idOrdem, Integer idFuncionario);

    void deleteDespesasMensaisPorFiltro(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idFuncionario);

    void deleteDetalheDespesasMensaisPorFiltro(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idFuncionario);

    void deleteDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    void deleteParcela(Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario);

    void deleteParcelaDetalheDespesasMensaisAdiantada(Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario);

}
