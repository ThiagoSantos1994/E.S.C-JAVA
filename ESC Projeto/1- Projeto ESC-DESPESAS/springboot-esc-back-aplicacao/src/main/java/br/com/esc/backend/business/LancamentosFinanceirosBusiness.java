package br.com.esc.backend.business;

import br.com.esc.backend.domain.*;
import br.com.esc.backend.repository.AplicacaoRepository;
import br.com.esc.backend.service.DetalheDespesasServices;
import br.com.esc.backend.service.LancamentosFinanceirosServices;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class LancamentosFinanceirosBusiness {

    private final AplicacaoRepository repository;
    private final LancamentosFinanceirosServices lancamentosServices;
    private final DetalheDespesasServices detalheDespesasServices;

    @SneakyThrows
    public LancamentosFinanceirosDTO obterLancamentosFinanceiros(String dsMes, String dsAno, Integer idFuncionario) {
        log.info("Consultando lancamentos financeiros - request: dsMes= {} - dsAno= {} - idFuncionario= {}", dsMes, dsAno, idFuncionario);

        var result = lancamentosServices.obterLancamentosFinanceiros(dsMes, dsAno, idFuncionario);
        result.setLabelQuitacaoParcelasMes(detalheDespesasServices.obterExtratoDespesasMes(result.getIdDespesa(), null, idFuncionario, "lancamentosMensais").getMensagem());

        return result;
    }

    @SneakyThrows
    public java.util.List<LancamentosMensaisDAO> obterDespesasMensaisConsolidadas(Integer idDespesa, Integer idConsolidacao, Integer idFuncionario) {
        log.info("Consultando despesas mensais consolidadas >>>  idDespesa = {} - idConsolidacao = {} - idFuncionario = {}", idDespesa, idConsolidacao, idFuncionario);
        return lancamentosServices.obterLancamentosMensaisConsolidados(idDespesa, idConsolidacao, idFuncionario);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void gravarDespesasFixasMensais(DespesasFixasMensaisRequest request) {
        lancamentosServices.gravarDespesasFixasMensais(request);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void ordenarListaDespesasMensais(Integer idDespesa, Integer idFuncionario) {
        lancamentosServices.ordenarListaDespesasMensais(idDespesa, idFuncionario);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void deleteDespesaFixaMensal(Integer idDespesa, Integer idOrdem, Integer idFuncionario) {
        log.info("Excluindo despesa fixa mensal - request: idDespesa= {} - idOrdem= {} - idFuncionario= {}", idDespesa, idOrdem, idFuncionario);
        repository.deleteDespesasFixasMensaisPorFiltro(idDespesa, idOrdem, idFuncionario);
        this.ordenarListaDespesasMensais(idDespesa, idFuncionario);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void deleteDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idFuncionario) {
        log.info("Excluindo despesa mensal - request: idDespesa= {} - idDetalheDespesa= {} - idOrdem= {} - idFuncionario= {}", idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
        repository.desassociarConsolidacaoDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario);
        repository.deleteDespesasMensaisPorFiltro(idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void alterarOrdemRegistroDespesas(Integer idDespesa, Integer iOrdemAtual, Integer iOrdemNova, Integer idFuncionario) {
        log.info("Alterando ordem registros DespesasMensais - Filtros: idDespesa = {}, iOrdemAtual = {}, iOrdemNova = {}, idFuncionario = {}", idDespesa, iOrdemAtual, iOrdemNova, idFuncionario);
        lancamentosServices.alterarOrdemRegistroDespesas(idDespesa, iOrdemAtual, iOrdemNova, idFuncionario);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void alterarOrdemRegistroDespesasFixas(Integer idDespesa, Integer iOrdemAtual, Integer iOrdemNova, Integer idFuncionario) {
        log.info("Alterando ordem registros DespesasFixasMensais - Filtros: idDespesa = {}, iOrdemAtual = {}, iOrdemNova = {}, idFuncionario = {}", idDespesa, iOrdemAtual, iOrdemNova, idFuncionario);
        lancamentosServices.alterarOrdemRegistroDespesasFixas(idDespesa, iOrdemAtual, iOrdemNova, idFuncionario);
    }

    @SneakyThrows
    public StringResponse obterMesAnoPorID(Integer idDespesa, Integer idFuncionario) {
        return lancamentosServices.obterMesAnoPorID(idDespesa, idFuncionario);
    }

    @SneakyThrows
    public StringResponse validaDespesaExistenteDebitoCartao(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario) {
        var response = lancamentosServices.validaDespesaExistenteDebitoCartao(idDespesa, idDetalheDespesa, idFuncionario);

        log.info("Validando despesa existente tipo Debito Cartao >> response: {}", response);
        return StringResponse.builder()
                .mensagem(response)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public StringResponse alterarTituloDespesaReuso(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String novoNomeDespesa) {
        var response = lancamentosServices.validarAlteracaoTituloDespesa(idDespesa, idDetalheDespesa, idFuncionario, novoNomeDespesa);

        return StringResponse.builder()
                .mensagem(response)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void alterarTituloDespesa(Integer idDetalheDespesa, Integer idFuncionario, String dsNomeDespesa, String anoReferencia) {
        log.info("Alterando titulo despesas >> idDetalheDespesa: {} para: {}", idDetalheDespesa, dsNomeDespesa);
        lancamentosServices.alterarTituloDespesa(idDetalheDespesa, idFuncionario, dsNomeDespesa, anoReferencia);
    }

    @SneakyThrows
    public StringResponse validaTituloDespesaDuplicado(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String dsTituloDespesa, String anoReferencia) {
        var response = lancamentosServices.validaTituloDespesaDuplicado(idDespesa, idDetalheDespesa, idFuncionario, dsTituloDespesa, anoReferencia);

        log.info("Validando Titulo Duplicado >> Response: {}", response);
        return StringResponse.builder()
                .mensagem(response)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void alterarDespesaMensalReferencia(Integer idDespesa, Integer idDetalheDespesa, Integer idDetalheDespesaNova, Integer idFuncionario) {
        lancamentosServices.alterarDespesaMensalReferencia(idDespesa, idDetalheDespesa, idDetalheDespesaNova, idFuncionario);
    }

    public TituloDespesaResponse obterTitulosDespesas(Integer idFuncionario) {
        log.info("Consultando titulos das despesas cadastradas");
        return lancamentosServices.getTituloDespesa(idFuncionario);
    }

    public TituloDespesaResponse obterTitulosEmprestimos(Integer idFuncionario) {
        log.info("Consultando titulos dos emprestimos cadastrados");
        return lancamentosServices.getTituloEmprestimo(idFuncionario);
    }

    public TituloDespesaResponse obterTitulosDespesasRelatorio(Integer idDespesa, Integer idFuncionario) {
        log.info("Consultando titulos das despesas cadastradas como relatorio");
        return lancamentosServices.getTituloDespesaRelatorio(idDespesa, idFuncionario);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void limparDadosTemporarios(Integer idFuncionario) {
        log.info("Limpando dados temporarios de lançamentos mensais");
        repository.deleteDespesasFixasMensaisTemp(idFuncionario);
        repository.deleteDespesasMensaisTemp(idFuncionario);
        repository.deleteDetalheDespesasMensaisTemp(idFuncionario);
    }
}
