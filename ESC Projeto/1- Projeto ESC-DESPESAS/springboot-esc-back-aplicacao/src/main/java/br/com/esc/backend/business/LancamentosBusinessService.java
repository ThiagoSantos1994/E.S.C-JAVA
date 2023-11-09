package br.com.esc.backend.business;

import br.com.esc.backend.domain.*;
import br.com.esc.backend.exception.CamposObrigatoriosException;
import br.com.esc.backend.repository.AplicacaoRepository;
import br.com.esc.backend.service.DespesasParceladasServices;
import br.com.esc.backend.service.DetalheDespesasServices;
import br.com.esc.backend.service.ImportarLancamentosServices;
import br.com.esc.backend.service.LancamentosFinanceirosServices;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;

import static br.com.esc.backend.utils.ObjectUtils.isNull;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
@Slf4j
public class LancamentosBusinessService {

    private final AplicacaoRepository repository;
    private final ImportarLancamentosServices importacaoServices;
    private final LancamentosFinanceirosServices lancamentosServices;
    private final DetalheDespesasServices detalheDespesasServices;
    private final DespesasParceladasServices despesasParceladasServices;

    @SneakyThrows
    public LancamentosFinanceirosDTO obterLancamentosFinanceiros(String dsMes, String dsAno, Integer idFuncionario) {
        log.info("Consultando lancamentos financeiros - request: dsMes= {} - dsAno= {} - idFuncionario= {}", dsMes, dsAno, idFuncionario);
        return lancamentosServices.obterLancamentosFinanceiros(dsMes, dsAno, idFuncionario);
    }

    @SneakyThrows
    public DetalheDespesasMensaisDTO obterDetalheDespesaMensal(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String ordem) {
        log.info("Consultando detalhes despesa mensal >>>  idDespesa = {} - idDetalheDespesa = {} - idFuncionario = {}", idDespesa, idDetalheDespesa, idFuncionario);
        return detalheDespesasServices.obterDetalheDespesaMensal(idDespesa, idDetalheDespesa, idFuncionario, ordem);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void gravarDespesasFixasMensais(DespesasFixasMensaisRequest request) {
        lancamentosServices.gravarDespesasFixasMensais(request);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void ordenarListaDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String ordem) {
        detalheDespesasServices.ordenarListaDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario, ordem);
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
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void deleteDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idFuncionario) {
        log.info("Excluindo despesa mensal - request: idDespesa= {} - idDetalheDespesa= {} - idOrdem= {} - idFuncionario= {}", idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
        repository.deleteDespesasMensaisPorFiltro(idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void deleteDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idFuncionario) throws Exception {
        log.info("Excluindo detalhe despesa mensal - request: idDespesa= {} - idDetalheDespesa= {} - idOrdem= {} - idFuncionario= {}", idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
        detalheDespesasServices.deleteDetalheDespesasMensais(idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
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

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void processarImportacaoDespesasMensais(Integer idDespesa, Integer idFuncionario, String dsMes, String dsAno) throws Exception {
        log.info("Processando importacao lancamentos e despesas mensais - idDespesa {} - idFuncionario {} - dsMes {} - dsAno {}", idDespesa, idFuncionario, dsMes, dsAno);
        importacaoServices.processarImportacaoDespesasMensais(idDespesa, idFuncionario, dsMes, dsAno);
        this.atualizaStatusDespesasParceladasEmAberto(idFuncionario);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void processarImportacaoDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String dsMes, String dsAno, Boolean bReprocessarTodosValores) {
        log.info("Iniciando processamento importacao detalhe despesas mensais - Filtros: idDespesa= {} - idDetalheDespesa= {} - idFuncionario= {} - dsMes= {} - dsAno= {} - bReprocessarTodosValores= {}", idDespesa, idDetalheDespesa, idFuncionario, dsMes, dsAno, bReprocessarTodosValores);
        importacaoServices.processarImportacaoDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario, dsMes, dsAno, bReprocessarTodosValores);
        this.atualizaStatusDespesasParceladasEmAberto(idFuncionario);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void gravarDespesaMensal(DespesasMensaisRequest request) throws InvocationTargetException, IllegalAccessException {
        DespesasMensaisDAO mensaisDAO = new DespesasMensaisDAO();
        BeanUtils.copyProperties(mensaisDAO, request);
        detalheDespesasServices.gravarDespesasMensais(mensaisDAO);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void gravarDetalheDespesasMensais(DetalheDespesasMensaisRequest request) throws Exception {
        DetalheDespesasMensaisDAO detalheDAO = new DetalheDespesasMensaisDAO();
        BeanUtils.copyProperties(detalheDAO, request);
        detalheDespesasServices.gravarDetalheDespesasMensais(detalheDAO);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void processarPagamentoDetalheDespesas(PagamentoDespesasRequest request) throws Exception {
        log.info("Processando pagamento despesas mensais - Filtros: {}", request.toString());
        detalheDespesasServices.baixarPagamentoDespesas(request);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void adiantarFluxoParcelas(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario) throws Exception {
        log.info("Processando adiantamento fluxo de parcelas - Filtros: idDespesa = {}, idDetalheDespesa = {}, idDespesaParcelada = {}, idParcela = {}, idFuncionario = {}", idDespesa, idDetalheDespesa, idDespesaParcelada, idParcela, idFuncionario);
        despesasParceladasServices.adiantarFluxoParcelas(idDespesa, idDetalheDespesa, idDespesaParcelada, idParcela, idFuncionario);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void desfazerAdiantamentoFluxoParcelas(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario) throws Exception {
        log.info("Processando fluxo para desfazer o adiantamento de parcelas - Filtros: idDespesa = {}, idDetalheDespesa = {}, idDespesaParcelada = {}, idParcela = {}, idFuncionario = {}", idDespesa, idDetalheDespesa, idDespesaParcelada, idParcela, idFuncionario);
        despesasParceladasServices.desfazerAdiantamentoFluxoParcelas(idDespesa, idDetalheDespesa, idDespesaParcelada, idParcela, idFuncionario);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    private void atualizaStatusDespesasParceladasEmAberto(Integer idFuncionario) {
        repository.updateDespesasParceladasEmAberto(idFuncionario);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void alterarOrdemRegistroDetalheDespesas(Integer idDespesa, Integer idDetalheDespesa, Integer iOrdemAtual, Integer iOrdemNova, Integer idFuncionario) {
        log.info("Alterando ordem registros DetalheDespesasMensais - Filtros: idDespesa = {}, idDetalheDespesa = {}, iOrdemAtual = {}, iOrdemNova = {}, idFuncionario = {}", idDespesa, idDetalheDespesa, iOrdemAtual, iOrdemNova, idFuncionario);
        detalheDespesasServices.alterarOrdemRegistroDetalheDespesas(idDespesa, idDetalheDespesa, iOrdemAtual, iOrdemNova, idFuncionario);
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

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public DespesaFixaTemporariaResponse gerarTemporariamenteDespesasMensais(Integer sMes, Integer sAno, Integer idFuncionario) throws Exception {
        log.info("Gerando temporariamente despesas mensais para pre-visualizacao...");
        return importacaoServices.gerarTemporariamenteDespesasMensais(sMes, sAno, idFuncionario);
    }

    public MesAnoResponse obterMesAnoPorID(Integer idDespesa, Integer idFuncionario) throws Exception {
        log.info("Consultando MesAnoPorID >>> despesaID: {}", idDespesa);

        var result = repository.getMesAnoPorID(idDespesa, idFuncionario);
        if (isNull(result)) {
            result = repository.getMesAnoPorIDTemp(idDespesa, idFuncionario);
        }

        log.info("Consultando MesAnoPorID >>> response: {}", result);
        return MesAnoResponse.builder()
                .mesAno(result)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public ChaveKeyDAO retornaNovaChaveKey(String tipoChave) {
        log.info("========= Gerando nova chaveKey, tipoChave: {} =========", tipoChave);

        if (isEmpty(tipoChave)) {
            throw new CamposObrigatoriosException("tipochave e obrigatorio.");
        }

        ChaveKeyDAO keyDAO = repository.getNovaChaveKey(tipoChave);
        if (isEmpty(keyDAO)) {
            throw new Exception("Sequencia não identificada na tabela de chaves primárias, favor contatar imediatamente o desenvolvedor do software.");
        }

        log.info("========= ChaveKey gerada com sucesso, tipoChave: {} novaChave: {} =========", tipoChave, keyDAO.getNovaChave());
        repository.updateChaveKeyUtilizada(keyDAO.getIdChaveKey());

        return keyDAO;
    }
}
