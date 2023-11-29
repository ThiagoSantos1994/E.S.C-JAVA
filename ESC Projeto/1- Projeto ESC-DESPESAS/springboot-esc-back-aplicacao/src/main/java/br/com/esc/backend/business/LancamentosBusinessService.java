package br.com.esc.backend.business;

import br.com.esc.backend.domain.*;
import br.com.esc.backend.exception.CamposObrigatoriosException;
import br.com.esc.backend.repository.AplicacaoRepository;
import br.com.esc.backend.service.DespesasParceladasServices;
import br.com.esc.backend.service.DetalheDespesasServices;
import br.com.esc.backend.service.ImportarLancamentosServices;
import br.com.esc.backend.service.LancamentosFinanceirosServices;
import br.com.esc.backend.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

        var result = lancamentosServices.obterLancamentosFinanceiros(dsMes, dsAno, idFuncionario);
        result.setLabelQuitacaoParcelasMes(detalheDespesasServices.obterExtratoDespesasMes(result.getIdDespesa(), null, idFuncionario, "lancamentosMensais").getMensagem());

        return result;
    }

    @SneakyThrows
    public DetalheDespesasMensaisDTO obterDetalheDespesaMensal(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String ordem) {
        log.info("Consultando detalhes despesa mensal >>>  idDespesa = {} - idDetalheDespesa = {} - idFuncionario = {} - ordem = {}", idDespesa, idDetalheDespesa, idFuncionario, ordem);
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
    public void deleteDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idFuncionario) {
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
    public void processarImportacaoDespesasMensais(Integer idDespesa, Integer idFuncionario, String dsMes, String dsAno) {
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
    public void gravarDespesaMensal(DespesasMensaisRequest request) {
        DespesasMensaisDAO mensaisDAO = new DespesasMensaisDAO();
        BeanUtils.copyProperties(mensaisDAO, request);

        if (request.getTpLinhaSeparacao().equalsIgnoreCase("S") && request.getIdDetalheDespesa().equals(-1)) {
            mensaisDAO.setIdDetalheDespesa(this.retornaNovaChaveKey("DETALHEDESPESA").getNovaChave());
        }
        detalheDespesasServices.gravarDespesasMensais(mensaisDAO);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void gravarDetalheDespesasMensais(DetalheDespesasMensaisRequest request) {
        DetalheDespesasMensaisDAO detalheDAO = new DetalheDespesasMensaisDAO();
        BeanUtils.copyProperties(detalheDAO, request);
        detalheDespesasServices.gravarDetalheDespesasMensais(detalheDAO);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void processarPagamentoDetalheDespesas(PagamentoDespesasRequest request) {
        log.info("Processando pagamento despesas mensais - Filtros: {}", request.toString());
        detalheDespesasServices.baixarPagamentoDespesas(request);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void adiantarFluxoParcelas(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario) {
        log.info("Processando adiantamento fluxo de parcelas - Filtros: idDespesa = {}, idDetalheDespesa = {}, idDespesaParcelada = {}, idParcela = {}, idFuncionario = {}", idDespesa, idDetalheDespesa, idDespesaParcelada, idParcela, idFuncionario);
        despesasParceladasServices.adiantarFluxoParcelas(idDespesa, idDetalheDespesa, idDespesaParcelada, idParcela, idFuncionario);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void desfazerAdiantamentoFluxoParcelas(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario) {
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
    public DespesaFixaTemporariaResponse gerarTemporariamenteDespesasMensais(Integer sMes, Integer sAno, Integer idFuncionario) {
        log.info("Gerando temporariamente despesas mensais para pre-visualizacao...");
        return importacaoServices.gerarTemporariamenteDespesasMensais(sMes, sAno, idFuncionario);
    }

    public StringResponse obterSubTotalDespesa(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String ordem) {
        log.info("Consultando subtotal despesa >> idDespesa: {}", idDespesa);
        return detalheDespesasServices.obterSubTotalDespesa(idDespesa, idDetalheDespesa, idFuncionario, ordem);
    }

    public StringResponse obterMesAnoPorID(Integer idDespesa, Integer idFuncionario) {
        return lancamentosServices.obterMesAnoPorID(idDespesa, idFuncionario);
    }

    public ExtratoDespesasDAO obterExtratoDespesasMes(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String tipo) {
        return detalheDespesasServices.obterExtratoDespesasMes(idDespesa, idDetalheDespesa, idFuncionario, tipo);
    }

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
    public void alterarTituloDespesa(Integer idDetalheDespesa, Integer idFuncionario, String dsNomeDespesa) {
        log.info("Alterando titulo despesas >> idDetalheDespesa: {} para: {}", idDetalheDespesa, dsNomeDespesa);
        lancamentosServices.alterarTituloDespesa(idDetalheDespesa, idFuncionario, dsNomeDespesa);
    }

    public StringResponse validaTituloDespesaDuplicado(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String dsTituloDespesa) {
        var response = lancamentosServices.validaTituloDespesaDuplicado(idDespesa, idDetalheDespesa, idFuncionario, dsTituloDespesa);

        log.info("Validando Titulo Duplicado >> Response: {}", response);
        return StringResponse.builder()
                .mensagem(response)
                .build();
    }

    public TituloDespesaResponse obterTitulosDespesas(Integer idFuncionario) {
        log.info("Consultando titulos das despesas cadastradas");
        return lancamentosServices.getTituloDespesa(idFuncionario);
    }

    public TituloDespesaResponse obterTitulosEmprestimos(Integer idFuncionario) {
        log.info("Consultando titulos dos emprestimos cadastrados");
        return lancamentosServices.getTituloEmprestimo(idFuncionario);
    }

    public DespesasParceladasResponse obterDespesasParceladas(Integer idFuncionario, String status) {
        log.info("Consultando lista de despesas parceladas");
        return despesasParceladasServices.getDespesasParceladas(idFuncionario, status);
    }

    public StringResponse obterValorDespesaParcelada(Integer idDespesaParcelada, Integer idParcela, String mesAnoReferencia, Integer idFuncionario) {
        log.info("Consultando valor despesa parcelada >>> filtros: idDespesaParcelada: {} - idParcela: {} - mesAnoReferencia: {} - idFuncionario: {}", idDespesaParcelada, idParcela, mesAnoReferencia, idFuncionario);
        return despesasParceladasServices.obterValorDespesa(idDespesaParcelada, idParcela, mesAnoReferencia, idFuncionario);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void deleteParcela(Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario) {
        log.info("Excluindo parcela >>> filtros: idDespesaParcelada: {} - idParcela: {} - idFuncionario: {}", idDespesaParcelada, idParcela, idFuncionario);
        despesasParceladasServices.excluirParcela(idDespesaParcelada, idParcela, idFuncionario);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void deleteDespesaParcelada(Integer idDespesaParcelada, Integer idFuncionario) {
        log.info("Excluindo despesa parcelada >>> filtros: idDespesaParcelada: {} - idFuncionario: {}", idDespesaParcelada, idFuncionario);
        despesasParceladasServices.excluirDespesaParcelada(idDespesaParcelada, idFuncionario);
    }

    public DetalheDespesasParceladasResponse obterDespesaParceladaPorNome(String nomeDespesaParcelada, Integer idFuncionario) {
        log.info("Consultando detalhe despesa parcelada por filtros >>> nomeDespesaParcelada= {} - idFuncionario= {}", nomeDespesaParcelada, idFuncionario);
        return despesasParceladasServices.obterDespesaParceladaPorNome(nomeDespesaParcelada, idFuncionario);
    }

    @SneakyThrows
    public ExplodirFluxoParcelasResponse gerarFluxoParcelas(Integer idDespesaParcelada, String valorParcela, Integer qtdeParcelas, String dataReferencia, Integer idFuncionario) {
        log.info("Gerando fluxo de parcelas >>> filtros: idDespesaParcelada: {} - valorParcela: {} - qtdeParcelas: {} - dataReferencia: {} - idFuncionario: {}", idDespesaParcelada, valorParcela, qtdeParcelas, dataReferencia, idFuncionario);
        return despesasParceladasServices.gerarFluxoParcelas(idDespesaParcelada, valorParcela, qtdeParcelas, dataReferencia, idFuncionario);
    }

    public StringResponse consultarNomeDespesaParceladaPorFiltro(Integer idDespesaParcelada, Integer idFuncionario) {
        log.info("Obtendo Nome Despesa Parcelada Por Filtros >>> idDespesaParcelada: {} - idFuncionario: {}", idDespesaParcelada, idFuncionario);
        return despesasParceladasServices.getNomeDespesaParceladaPorFiltro(idDespesaParcelada, idFuncionario);
    }

    public TituloDespesaResponse consultarDespesasParceladasParaImportacao(Integer idFuncionario, String tipo) {
        log.info("Obtendo Lista de Nomes Despesas Parceladas para Importacao - Filtros >>> idFuncionario: {} - tipo: {}", idFuncionario, tipo);
        return despesasParceladasServices.getNomeDespesasParceladasParaImportacao(idFuncionario, tipo);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void gravarDespesaParcelada(DespesaParceladaDAO despesa) {
        despesasParceladasServices.gravarDespesaParcelada(despesa);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void gravarParcela(ParcelasDAO parcela) {
        despesasParceladasServices.gravarParcela(parcela);
    }

    @SneakyThrows
    public StringResponse validarTituloDespesaParceladaExistente(String dsTituloDespesaParcelada, Integer idDespesaParcelada, Integer idFuncionario) {
        log.info("Validando se o titulo da despesa parcelada ja existe na base de dados - Filtros >>> dsTituloDespesaParcelada: {}", dsTituloDespesaParcelada);
        return despesasParceladasServices.validarTituloDespesaParceladaExistente(dsTituloDespesaParcelada, idDespesaParcelada, idFuncionario);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public ChaveKeyDAO retornaNovaChaveKey(String tipoChave) {
        log.info("========= Gerando nova chaveKey, tipoChave: {} =========", tipoChave);

        if (ObjectUtils.isEmpty(tipoChave)) {
            throw new CamposObrigatoriosException("tipochave e obrigatorio.");
        }

        ChaveKeyDAO keyDAO = repository.getNovaChaveKey(tipoChave);
        if (ObjectUtils.isEmpty(keyDAO)) {
            throw new Exception("Sequencia não identificada na tabela de chaves primárias, favor contatar imediatamente o desenvolvedor do software.");
        }

        log.info("========= ChaveKey gerada com sucesso, tipoChave: {} novaChave: {} =========", tipoChave, keyDAO.getNovaChave());
        repository.updateChaveKeyUtilizada(keyDAO.getIdChaveKey());

        return keyDAO;
    }
}
