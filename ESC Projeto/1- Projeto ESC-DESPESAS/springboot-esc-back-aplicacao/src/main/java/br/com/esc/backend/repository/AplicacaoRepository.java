package br.com.esc.backend.repository;

import br.com.esc.backend.domain.DespesasFixasMensaisDAO;
import br.com.esc.backend.domain.DespesasParceladasQuitacaoDAO;
import br.com.esc.backend.domain.DespesasMensaisDAO;

import java.math.BigDecimal;
import java.util.List;

public interface AplicacaoRepository {

    List<DespesasFixasMensaisDAO> obterDespesasFixasMensais(String dsMes, String dsAno, Integer idFuncionario);

    List<DespesasMensaisDAO> obterDespesasMensais(Integer idDespesa, Integer idFuncionario);

    BigDecimal obterCalculoTotalDespesa(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    BigDecimal obterCalculoTotalDespesaPendente(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    BigDecimal obterCalculoTotalDespesaPaga(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);

    BigDecimal obterCalculoDespesaTipoRelatorio(Integer idDespesa, Integer idDespesaLinkRelatorio, Integer idFuncionario);

    List<BigDecimal> obterCalculoReceitaPositivaMES(Integer idDespesa, Integer idFuncionario);

    BigDecimal obterCalculoReceitaNegativaMES(Integer idDespesa, Integer idFuncionario);

    BigDecimal obterCalculoReceitaPendentePgtoMES(Integer idDespesa, Integer idFuncionario);

    BigDecimal obterCalculoSaldoInicialMES(Integer idDespesa, Integer idFuncionario);

    BigDecimal obterQuantidadeDespesasParceladasMes(Integer idDespesa, Integer idFuncionario);

    DespesasParceladasQuitacaoDAO obterQuantidadeDespesasParceladasQuitacaoMes(Integer idDespesa, Integer idFuncionario);

    //Detalhe Despesas Mensais

    List<DespesasMensaisDAO> obterDespesasMensaisPorID(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario);
}
