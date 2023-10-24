package br.com.esc.backend.repository;

import br.com.esc.backend.domain.*;

import java.math.BigDecimal;
import java.util.List;

public interface AplicacaoRepository {

    List<DespesasFixasMensaisDAO> getDespesasFixasMensais(String dsMes, String dsAno, Integer idFuncionario);

    List<LancamentosMensaisDAO> getLancamentosMensais(Integer idDespesa, Integer idFuncionario);

    List<DespesasMensaisDAO> getDespesasMensais(Integer idDespesa, Integer idFuncionario, Integer idDetalheDespesa);

    BigDecimal getCalculoTotalDespesa(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    BigDecimal getCalculoTotalDespesaPendente(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    BigDecimal getCalculoTotalDespesaPaga(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    BigDecimal getCalculoDespesaTipoRelatorio(Integer idDespesa, Integer idDespesaLinkRelatorio, Integer idFuncionario);

    List<BigDecimal> getCalculoReceitaPositivaMES(Integer idDespesa, Integer idFuncionario);

    BigDecimal getCalculoReceitaNegativaMES(Integer idDespesa, Integer idFuncionario);

    BigDecimal getCalculoReceitaPendentePgtoMES(Integer idDespesa, Integer idFuncionario);

    BigDecimal getCalculoSaldoInicialMES(Integer idDespesa, Integer idFuncionario);

    BigDecimal getQuantidadeDespesasParceladasMes(Integer idDespesa, Integer idFuncionario);

    DespesasParceladasQuitacaoDAO getQuantidadeDespesasParceladasQuitacaoMes(Integer idDespesa, Integer idFuncionario);

    List<DetalheDespesasMensaisDAO> getDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    DetalheDespesasMensaisDAO getDetalheDespesaMensalPorFiltro(DetalheDespesasMensaisDAO mensaisDAO);

    ParcelasDAO getParcelaPorDataVencimento(Integer idDespesaParcelada, String dataVencimento, Integer idFuncionario);

    String getValidaDespesaParceladaAmortizacao(Integer idDespesaParcelada, Integer idFuncionario);

    void insertDespesasFixasMensais(DespesasFixasMensaisRequest request);

    void insertDespesasMensais(DespesasMensaisDAO despesasMensaisDAO);

    void insertDetalheDespesasMensais(DetalheDespesasMensaisDAO detalheDAO);

    void updateValorDetalheDespesasMensais(DetalheDespesasMensaisDAO detalheDespesasMensais);

    void updateDespesasFixasMensais(DespesasFixasMensaisRequest request);

    void updateDespesasMensais(DespesasMensaisDAO despesasMensaisDAO);

    void updateDetalheDespesasMensais(DetalheDespesasMensaisDAO detalheDespesasMensais);

    void deleteDespesaParceladaImportada(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idFuncionario);

    void deleteDespesasFixasMensaisTemp(Integer idFuncionario);

    void deleteDespesasMensaisTemp(Integer idFuncionario);

    void deleteDetalheDespesasMensaisTemp(Integer idFuncionario);

    void deleteTodasDespesasFixasMensais(Integer idDespesa, Integer idFuncionario);

    void deleteTodasDespesasMensais(Integer idDespesa, Integer idFuncionario);

    void deleteTodosDetalhesDespesasMensais(Integer idDespesa, Integer idFuncionario);

    void deleteDespesasFixasMensaisPorFiltro(Integer idDespesa, Integer idOrdem, Integer idFuncionario);

    void deleteDespesasMensaisPorFiltro(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idFuncionario);

    void deleteDetalheDespesasMensaisPorFiltro(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idFuncionario);

    void deleteDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    void updateParcelaStatusPendenteDespesasExcluidas(Integer idDespesa, Integer idFuncionario);

    void updateParcelaStatusPendente(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario);

    void updateDespesasParceladasEmAberto(Integer idFuncionario);
}
