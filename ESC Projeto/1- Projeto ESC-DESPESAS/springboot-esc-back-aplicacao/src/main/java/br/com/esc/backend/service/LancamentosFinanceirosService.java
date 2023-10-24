package br.com.esc.backend.service;

import br.com.esc.backend.business.DetalheDespesasBusiness;
import br.com.esc.backend.business.ImportarLancamentosBusiness;
import br.com.esc.backend.business.LancamentosFinanceirosBusiness;
import br.com.esc.backend.domain.DespesasFixasMensaisRequest;
import br.com.esc.backend.domain.DetalheDespesasMensaisDTO;
import br.com.esc.backend.domain.LancamentosFinanceirosDTO;
import br.com.esc.backend.repository.AplicacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import static br.com.esc.backend.utils.ObjectUtils.isNull;

@RequiredArgsConstructor
@Slf4j
public class LancamentosFinanceirosService {

    private final AplicacaoRepository repository;
    private final ImportarLancamentosBusiness importacaoBusiness;
    private final LancamentosFinanceirosBusiness lancamentosBusiness;
    private final DetalheDespesasBusiness detalheDespesasBusiness;

    public LancamentosFinanceirosDTO obterLancamentosFinanceiros(String dsMes, String dsAno, Integer idFuncionario) {
        log.info("Consultando lancamentos financeiros - request: dsMes= {} - dsAno= {} - idFuncionario= {}", dsMes, dsAno, idFuncionario);

        var result = lancamentosBusiness.obterLancamentosFinanceiros(dsMes, dsAno, idFuncionario);

        log.info("Consultando lancamentos financeiros - result: {}", result);

        return result;
    }

    public DetalheDespesasMensaisDTO obterDetalheDespesaMensal(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario) {
        log.info("Consultando detalhes despesa mensal >>>  idDespesa = {} - idDetalheDespesa = {} - idFuncionario = {}", idDespesa, idDetalheDespesa, idFuncionario);

        var result = detalheDespesasBusiness.obterDetalheDespesaMensal(idDespesa, idDetalheDespesa, idFuncionario);

        log.info("Consultando detalhes despesa mensal >>>  result = {}", result);

        return result;
    }

    public void gravarDespesasFixasMensais(DespesasFixasMensaisRequest request) {
        log.info("Inserindo despesas fixas mensais - request: {}", request);
        repository.insertDespesasFixasMensais(request);
    }

    public void updateDespesasFixasMensais(DespesasFixasMensaisRequest request) {
        log.info("Atualizando despesas fixas mensais - request: {}", request);
        repository.updateDespesasFixasMensais(request);
    }

    public void deleteDespesaFixaMensal(Integer idDespesa, Integer idOrdem, Integer idFuncionario) {
        log.info("Excluindo despesa fixa mensal - request: idDespesa= {} - idOrdem= {} - idFuncionario= {}", idDespesa, idOrdem, idFuncionario);
        repository.deleteDespesasFixasMensaisPorFiltro(idDespesa, idOrdem, idFuncionario);
    }

    public void deleteDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idFuncionario) {
        log.info("Excluindo despesa mensal - request: idDespesa= {} - idDetalheDespesa= {} - idOrdem= {} - idFuncionario= {}", idDespesa, idDetalheDespesa, idOrdem, idFuncionario);

        repository.updateParcelaStatusPendente(idDespesa, idDetalheDespesa, null, null, idFuncionario);

        repository.deleteDespesasMensaisPorFiltro(idDespesa, idDetalheDespesa, idOrdem, idFuncionario);

        this.atualizaStatusDespesasParceladasEmAberto(idFuncionario);
    }

    public void deleteDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idFuncionario) {
        log.info("Excluindo detalhe despesa mensal - request: idDespesa= {} - idDetalheDespesa= {} - idOrdem= {} - idFuncionario= {}", idDespesa, idDetalheDespesa, idOrdem, idFuncionario);

        repository.updateParcelaStatusPendente(idDespesa, idDetalheDespesa, null, null, idFuncionario);

        if (isNull(idOrdem) || idOrdem == -1) {
            repository.deleteDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario);
        } else {
            repository.deleteDetalheDespesasMensaisPorFiltro(idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
        }

        this.atualizaStatusDespesasParceladasEmAberto(idFuncionario);
    }

    public void deleteTodosLancamentosMensais(Integer idDespesa, Integer idFuncionario) {
        log.info("Excluindo todas despesas fixas mensais - request: idDespesa= {} - idFuncionario= {}", idDespesa, idFuncionario);
        repository.deleteTodasDespesasFixasMensais(idDespesa, idFuncionario);

        log.info("Excluindo todas despesas mensais - request: idDespesa= {} - idFuncionario= {}", idDespesa, idFuncionario);
        repository.deleteTodasDespesasMensais(idDespesa, idFuncionario);

        log.info("Excluindo todos detalhes despesas mensais - request: idDespesa= {} - idFuncionario= {}", idDespesa, idFuncionario);
        repository.deleteTodosDetalhesDespesasMensais(idDespesa, idFuncionario);

        log.info("Atualizando status das parcelas para Pendente - request: idDespesa= {} - idFuncionario= {}", idDespesa, idFuncionario);
        repository.updateParcelaStatusPendenteDespesasExcluidas(idDespesa, idFuncionario);

        log.info("Atualizando status das despesas parcelas para Em Aberto - request: idFuncionario= {}", idDespesa, idFuncionario);
        this.atualizaStatusDespesasParceladasEmAberto(idFuncionario);
    }

    public void processarImportacaoDespesasMensais(Integer idDespesa, Integer idFuncionario, String dsMes, String dsAno) throws Exception {
        log.info("Processando importacao despesas mensais - idDespesa {} - idFuncionario {} - dsMes {} - dsAno {}", idDespesa, idFuncionario, dsMes, dsAno);

        importacaoBusiness.processarImportacaoDespesasMensais(idDespesa, idFuncionario, dsMes, dsAno);

        this.atualizaStatusDespesasParceladasEmAberto(idFuncionario);

        log.info("Importacao despesas mensais concluida com sucesso!");
    }

    public void processarImportacaoDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String dsMes, String dsAno, Boolean bReprocessarTodosValores) {
        log.info("Iniciando processamento importacao detalhe despesas mensais - Filtros: idDespesa= {} - idDetalheDespesa= {} - idFuncionario= {} - dsMes= {} - dsAno= {} - bReprocessarTodosValores= {}", idDespesa, idDetalheDespesa, idFuncionario, dsMes, dsAno, bReprocessarTodosValores);
        importacaoBusiness.processarImportacaoDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario, dsMes, dsAno, bReprocessarTodosValores);
    }

    private void atualizaStatusDespesasParceladasEmAberto(Integer idFuncionario) {
        repository.updateDespesasParceladasEmAberto(idFuncionario);
    }
}
