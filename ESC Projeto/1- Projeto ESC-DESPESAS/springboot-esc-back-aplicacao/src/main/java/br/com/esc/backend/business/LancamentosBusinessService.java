package br.com.esc.backend.business;

import br.com.esc.backend.domain.*;
import br.com.esc.backend.repository.AplicacaoRepository;
import br.com.esc.backend.service.DespesasParceladasServices;
import br.com.esc.backend.service.DetalheDespesasServices;
import br.com.esc.backend.service.ImportarLancamentosServices;
import br.com.esc.backend.service.LancamentosFinanceirosServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

@RequiredArgsConstructor
@Slf4j
public class LancamentosBusinessService {

    private final AplicacaoRepository repository;
    private final ImportarLancamentosServices importacaoServices;
    private final LancamentosFinanceirosServices lancamentosServices;
    private final DetalheDespesasServices detalheDespesasServices;
    private final DespesasParceladasServices despesasParceladasServices;

    public LancamentosFinanceirosDTO obterLancamentosFinanceiros(String dsMes, String dsAno, Integer idFuncionario) {
        log.info("Consultando lancamentos financeiros - request: dsMes= {} - dsAno= {} - idFuncionario= {}", dsMes, dsAno, idFuncionario);

        var result = lancamentosServices.obterLancamentosFinanceiros(dsMes, dsAno, idFuncionario);

        log.info("Consultando lancamentos financeiros - result: {}", result);

        return result;
    }

    public DetalheDespesasMensaisDTO obterDetalheDespesaMensal(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario) {
        log.info("Consultando detalhes despesa mensal >>>  idDespesa = {} - idDetalheDespesa = {} - idFuncionario = {}", idDespesa, idDetalheDespesa, idFuncionario);

        var result = detalheDespesasServices.obterDetalheDespesaMensal(idDespesa, idDetalheDespesa, idFuncionario);

        log.info("Consultando detalhes despesa mensal >>>  result = {}", result);

        return result;
    }

    public void gravarDespesasFixasMensais(DespesasFixasMensaisRequest request) {
        lancamentosServices.gravarDespesasFixasMensais(request);
    }

    public void deleteDespesaFixaMensal(Integer idDespesa, Integer idOrdem, Integer idFuncionario) {
        log.info("Excluindo despesa fixa mensal - request: idDespesa= {} - idOrdem= {} - idFuncionario= {}", idDespesa, idOrdem, idFuncionario);
        repository.deleteDespesasFixasMensaisPorFiltro(idDespesa, idOrdem, idFuncionario);
    }

    public void deleteDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idFuncionario) {
        log.info("Excluindo despesa mensal - request: idDespesa= {} - idDetalheDespesa= {} - idOrdem= {} - idFuncionario= {}", idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
        repository.deleteDespesasMensaisPorFiltro(idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
    }

    public void deleteDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idFuncionario) throws Exception {
        log.info("Excluindo detalhe despesa mensal - request: idDespesa= {} - idDetalheDespesa= {} - idOrdem= {} - idFuncionario= {}", idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
        detalheDespesasServices.deleteDetalheDespesasMensais(idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
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
        log.info("Processando importacao lancamentos e despesas mensais - idDespesa {} - idFuncionario {} - dsMes {} - dsAno {}", idDespesa, idFuncionario, dsMes, dsAno);
        importacaoServices.processarImportacaoDespesasMensais(idDespesa, idFuncionario, dsMes, dsAno);
        this.atualizaStatusDespesasParceladasEmAberto(idFuncionario);
    }

    public void processarImportacaoDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String dsMes, String dsAno, Boolean bReprocessarTodosValores) {
        log.info("Iniciando processamento importacao detalhe despesas mensais - Filtros: idDespesa= {} - idDetalheDespesa= {} - idFuncionario= {} - dsMes= {} - dsAno= {} - bReprocessarTodosValores= {}", idDespesa, idDetalheDespesa, idFuncionario, dsMes, dsAno, bReprocessarTodosValores);
        importacaoServices.processarImportacaoDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario, dsMes, dsAno, bReprocessarTodosValores);
        this.atualizaStatusDespesasParceladasEmAberto(idFuncionario);
    }

    public void gravarDespesaMensal(DespesasMensaisRequest request) throws InvocationTargetException, IllegalAccessException {
        DespesasMensaisDAO mensaisDAO = new DespesasMensaisDAO();
        BeanUtils.copyProperties(mensaisDAO, request);
        detalheDespesasServices.gravarDespesasMensais(mensaisDAO);
    }

    public void gravarDetalheDespesasMensais(DetalheDespesasMensaisRequest request) throws Exception {
        DetalheDespesasMensaisDAO detalheDAO = new DetalheDespesasMensaisDAO();
        BeanUtils.copyProperties(detalheDAO, request);
        detalheDespesasServices.gravarDetalheDespesasMensais(detalheDAO);
    }

    public void processarPagamentoDetalheDespesas(PagamentoDespesasRequest request) throws Exception {
        log.info("Processando pagamento despesas mensais - Filtros: {}", request.toString());
        detalheDespesasServices.baixarPagamentoDespesas(request);
    }

    public void adiantarFluxoParcelas(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario) throws Exception {
        log.info("Processando adiantamento fluxo de parcelas - Filtros: idDespesa = {}, idDetalheDespesa = {}, idDespesaParcelada = {}, idParcela = {}, idFuncionario = {}", idDespesa, idDetalheDespesa, idDespesaParcelada, idParcela, idFuncionario);
        despesasParceladasServices.adiantarFluxoParcelas(idDespesa, idDetalheDespesa, idDespesaParcelada, idParcela, idFuncionario);
    }

    public void desfazerAdiantamentoFluxoParcelas(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario) throws Exception {
        log.info("Processando fluxo para desfazer o adiantamento de parcelas - Filtros: idDespesa = {}, idDetalheDespesa = {}, idDespesaParcelada = {}, idParcela = {}, idFuncionario = {}", idDespesa, idDetalheDespesa, idDespesaParcelada, idParcela, idFuncionario);
        despesasParceladasServices.desfazerAdiantamentoFluxoParcelas(idDespesa, idDetalheDespesa, idDespesaParcelada, idParcela, idFuncionario);
    }

    private void atualizaStatusDespesasParceladasEmAberto(Integer idFuncionario) {
        repository.updateDespesasParceladasEmAberto(idFuncionario);
    }

}
