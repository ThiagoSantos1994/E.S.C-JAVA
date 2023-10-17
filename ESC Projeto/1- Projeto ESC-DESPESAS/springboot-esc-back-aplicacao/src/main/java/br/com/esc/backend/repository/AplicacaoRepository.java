package br.com.esc.backend.repository;

import br.com.esc.backend.domain.*;
import org.jdbi.v3.sqlobject.customizer.BindBean;

import java.math.BigDecimal;
import java.util.List;

public interface AplicacaoRepository {

    List<DespesasFixasMensaisDAO> getDespesasFixasMensais(String dsMes, String dsAno, Integer idFuncionario);

    List<LancamentosMensaisDAO> getLancamentosMensais(Integer idDespesa, Integer idFuncionario);

    List<DespesasMensaisDAO> getDespesasMensais(Integer idDespesa, Integer idFuncionario);

    DespesasMensaisDAO getDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

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

    void insertDespesasFixasMensais(DespesasFixasMensaisRequest request);

    void updateDespesasFixasMensais(DespesasFixasMensaisRequest request);

    void deleteDespesasFixasMensais(Integer idDespesa, Integer idOrdem, Integer idFuncionario);

    void insertDespesasMensais(DespesasMensaisDAO despesasMensaisDAO);

    void insertDetalheDespesasMensais(DetalheDespesasMensaisDAO detalheDAO);

    List<DetalheDespesasMensaisDAO> getDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    ParcelasDAO getParcelaPorDataVencimento(Integer idDespesaParcelada, String dataVencimento, Integer idFuncionario);

    String getValidaDespesaParceladaAmortizacao(Integer idDespesaParcelada, Integer idFuncionario);

    void updateDespesasMensais(@BindBean("mensal") DespesasMensaisDAO despesasMensaisDAO);
}
