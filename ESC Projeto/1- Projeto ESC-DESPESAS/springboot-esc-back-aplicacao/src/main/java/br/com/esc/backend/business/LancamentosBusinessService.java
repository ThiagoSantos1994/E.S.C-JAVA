package br.com.esc.backend.business;

import br.com.esc.backend.domain.*;
import br.com.esc.backend.exception.CamposObrigatoriosException;
import br.com.esc.backend.repository.AplicacaoRepository;
import br.com.esc.backend.service.*;
import br.com.esc.backend.utils.DataUtils;
import br.com.esc.backend.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static br.com.esc.backend.utils.ObjectUtils.isEmpty;
import static br.com.esc.backend.utils.ObjectUtils.isNull;
import static br.com.esc.backend.utils.VariaveisGlobais.VALOR_ZERO;


@Service
@RequiredArgsConstructor
@Slf4j
public class LancamentosBusinessService {

    private final AplicacaoRepository repository;
    private final ImportarLancamentosServices importacaoServices;
    private final LancamentosFinanceirosServices lancamentosServices;
    private final DetalheDespesasServices detalheDespesasServices;
    private final DespesasParceladasServices despesasParceladasServices;
    private final BackupServices backupServices;
    private final AutenticacaoServices autenticacaoServices;
    private final LembreteServices lembreteServices;

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
        this.ordenarListaDespesasMensais(idDespesa, idFuncionario);
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
    public void deleteDetalheDespesasMensaisV2(List<DetalheDespesasMensaisRequest> request) {
        for (DetalheDespesasMensaisRequest detalhe : request) {
            log.info("Excluindo detalhe despesa mensal - request: {}", detalhe);
            detalheDespesasServices.deleteDetalheDespesasMensais(detalhe.getIdDespesa(), detalhe.getIdDetalheDespesa(), detalhe.getIdOrdem(), detalhe.getIdFuncionario());
        }

        var detalheRequest = request.get(0);
        detalheDespesasServices.ordenarRegistrosAtualizacao(detalheRequest.getIdDespesa(), detalheRequest.getIdDetalheDespesa(), detalheRequest.getIdFuncionario(), "prazo");
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
    public void processarImportacaoDespesaParcelada(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idFuncionario) {
        log.info("Iniciando processamento importacao despesa parcelada - Filtros: idDespesa= {} - idDetalheDespesa= {} - idDespesaParcelada= {} - idFuncionario= {}", idDespesa, idDetalheDespesa, idDespesaParcelada, idFuncionario);
        importacaoServices.processarImportacaoDespesaParcelada(idDespesa, idDetalheDespesa, idDespesaParcelada, idFuncionario);
        this.atualizaStatusDespesasParceladasEmAberto(idFuncionario);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void incluirDespesaParceladaAmortizada(Integer idDespesa, Integer idDetalheDespesa, List<ParcelasDAO> parcelas, Integer idFuncionario) {
        log.info("Gravando despesa parcelada amortizada na despesa mensal - Filtros: idDespesa= {} - idDetalheDespesa= {} - parcelas = {} - idFuncionario= {}", idDespesa, idDetalheDespesa, parcelas.toString(), idFuncionario);
        importacaoServices.incluirDespesaParceladaAmortizada(idDespesa, idDetalheDespesa, parcelas, idFuncionario);
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
        detalheDespesasServices.gravarDetalheDespesasMensais(detalheDAO, false);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void processarPagamentoDetalheDespesa(PagamentoDespesasRequest request) {
        log.info("Processando pagamento detalhe despesa mensal - Filtros: {}", request.toString());
        detalheDespesasServices.baixarPagamentoDespesas(request);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void processarPagamentoDespesa(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String observacaoPagamento) {
        log.info("Processando pagamento despesa mensal - Filtros: idDespesa: {} - idDetalheDespesa: {} - idFuncionario: {} - observacaoPagamento: {}", idDespesa, idDetalheDespesa, idFuncionario, observacaoPagamento);
        var detalheDespesas = detalheDespesasServices.obterDetalheDespesaMensal(idDespesa, idDetalheDespesa, idFuncionario, null)
                .getDetalheDespesaMensal();

        for (DetalheDespesasMensaisDAO despesa : detalheDespesas) {
            var request = PagamentoDespesasRequest.builder()
                    .idDespesa(despesa.getIdDespesa())
                    .idDetalheDespesa(despesa.getIdDetalheDespesa())
                    .idDespesaParcelada(isNull(despesa.getIdDespesaParcelada()) ? 0 : despesa.getIdDespesaParcelada())
                    .idParcela(isNull(despesa.getIdParcela()) ? 0 : despesa.getIdParcela())
                    .idOrdem(despesa.getIdOrdem())
                    .idFuncionario(despesa.getIdFuncionario())
                    .vlTotal(despesa.getVlTotal())
                    .vlTotalPago(despesa.getVlTotal())
                    .tpStatus(despesa.getTpStatus())
                    .dsObservacoes(isEmpty(observacaoPagamento) ? "Pagamento realizado em ".concat(DataUtils.DataHoraAtual()) : observacaoPagamento)
                    .isProcessamentoAdiantamentoParcelas(false)
                    .build();

            detalheDespesasServices.baixarPagamentoDespesas(request);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void desfazerPagamentoDespesas(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario) {
        log.info("Desfazendo pagamento despesa mensal - Filtros: idDespesa: {} - idDetalheDespesa: {} - idFuncionario: {}", idDespesa, idDetalheDespesa, idFuncionario);
        var detalheDespesas = detalheDespesasServices.obterDetalheDespesaMensal(idDespesa, idDetalheDespesa, idFuncionario, null)
                .getDetalheDespesaMensal();

        for (DetalheDespesasMensaisDAO despesa : detalheDespesas) {
            var request = PagamentoDespesasRequest.builder()
                    .idDespesa(despesa.getIdDespesa())
                    .idDetalheDespesa(despesa.getIdDetalheDespesa())
                    .idDespesaParcelada(isNull(despesa.getIdDespesaParcelada()) ? 0 : despesa.getIdDespesaParcelada())
                    .idParcela(isNull(despesa.getIdParcela()) ? 0 : despesa.getIdParcela())
                    .idOrdem(despesa.getIdOrdem())
                    .idFuncionario(despesa.getIdFuncionario())
                    .vlTotal(despesa.getVlTotal())
                    .vlTotalPago(VALOR_ZERO)
                    .tpStatus(despesa.getTpStatus())
                    .dsObservacoes("")
                    .isProcessamentoAdiantamentoParcelas(false)
                    .build();

            detalheDespesasServices.desfazerBaixaPagamentoDespesas(request);
        }
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

    @SneakyThrows
    public StringResponse obterSubTotalDespesa(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String ordem) {
        log.info("Consultando subtotal despesa >> idDespesa: {}", idDespesa);
        return detalheDespesasServices.obterSubTotalDespesa(idDespesa, idDetalheDespesa, idFuncionario, ordem);
    }

    @SneakyThrows
    public StringResponse obterMesAnoPorID(Integer idDespesa, Integer idFuncionario) {
        return lancamentosServices.obterMesAnoPorID(idDespesa, idFuncionario);
    }

    @SneakyThrows
    public ExtratoDespesasDAO obterExtratoDespesasMes(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String tipo) {
        return detalheDespesasServices.obterExtratoDespesasMes(idDespesa, idDetalheDespesa, idFuncionario, tipo);
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
    public void alterarTituloDespesa(Integer idDetalheDespesa, Integer idFuncionario, String dsNomeDespesa) {
        log.info("Alterando titulo despesas >> idDetalheDespesa: {} para: {}", idDetalheDespesa, dsNomeDespesa);
        lancamentosServices.alterarTituloDespesa(idDetalheDespesa, idFuncionario, dsNomeDespesa);
    }

    @SneakyThrows
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

    public StringResponse obterObservacoesDetalheDespesa(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idFuncionario) {
        log.info("Consultando observações dos detalhes da despesa");
        return detalheDespesasServices.getObservacoesDetalheDespesa(idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
    }

    public void gravarObservacoesDetalheDespesa(ObservacoesDetalheDespesaRequest request) {
        log.info("Gravando observações detalhe despesa >> Request: {}", request);
        detalheDespesasServices.gravarObservacoesDetalheDespesa(request);
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
        return despesasParceladasServices.obterDespesaParceladaPorFiltros(null, nomeDespesaParcelada, idFuncionario);
    }

    public DetalheDespesasParceladasResponse obterDespesaParceladaPorID(Integer idDespesaParcelada, Integer idFuncionario) {
        log.info("Consultando detalhe despesa parcelada por filtros >>> idDespesaParcelada= {} - idFuncionario= {}", idDespesaParcelada, idFuncionario);
        return despesasParceladasServices.obterDespesaParceladaPorFiltros(idDespesaParcelada, null, idFuncionario);
    }

    public List<ParcelasDAO> obterParcelasParaAmortizacao(Integer idDespesaParcelada, Integer idFuncionario) {
        log.info("Consultando parcelas para amortizacao >>> idDespesaParcelada= {} - idFuncionario= {}", idDespesaParcelada, idFuncionario);
        return despesasParceladasServices.obterParcelasParaAmortizacao(idDespesaParcelada, idFuncionario);
    }

    @SneakyThrows
    public ExplodirFluxoParcelasResponse gerarFluxoParcelas(Integer idDespesaParcelada, String valorParcela, Integer qtdeParcelas, String dataReferencia, Integer idFuncionario) {
        log.info("Gerando fluxo de parcelas >>> filtros: idDespesaParcelada: {} - valorParcela: {} - qtdeParcelas: {} - dataReferencia: {} - idFuncionario: {}", idDespesaParcelada, valorParcela, qtdeParcelas, dataReferencia, idFuncionario);
        return despesasParceladasServices.gerarFluxoParcelas(idDespesaParcelada, valorParcela, qtdeParcelas, dataReferencia, idFuncionario);
    }

    @SneakyThrows
    public DetalheDespesasParceladasResponse gerarFluxoParcelasV2(Integer idDespesaParcelada, String valorParcela, Integer qtdeParcelas, String dataReferencia, Integer idFuncionario) {
        log.info("Gerando fluxo de parcelas V2 >>> filtros: idDespesaParcelada: {} - valorParcela: {} - qtdeParcelas: {} - dataReferencia: {} - idFuncionario: {}", idDespesaParcelada, valorParcela, qtdeParcelas, dataReferencia, idFuncionario);
        var idDespesa = (idDespesaParcelada == -1 ? this.retornaNovaChaveKey("DESPESASPARCELADAS").getNovaChave() : idDespesaParcelada);
        var fluxoParcelas = despesasParceladasServices.gerarFluxoParcelas(idDespesa, valorParcela, qtdeParcelas, dataReferencia, idFuncionario);

        var response = DetalheDespesasParceladasResponse.builder()
                .idDespesaParcelada(idDespesa)
                .despesaVinculada("Novo fluxo de parcelas, clique em SALVAR para gravar esta despesa parcelada.")
                .parcelas(fluxoParcelas.getParcelas())
                .build();

        return response;
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
    public void gravarParcela(List<ParcelasDAO> parcelas) {
        for (ParcelasDAO parcela : parcelas) {
            despesasParceladasServices.gravarParcela(parcela);
        }
    }

    @SneakyThrows
    public StringResponse validarTituloDespesaParceladaExistente(String dsTituloDespesaParcelada, Integer idDespesaParcelada, Integer idFuncionario) {
        log.info("Validando se o titulo da despesa parcelada ja existe na base de dados - Filtros >>> dsTituloDespesaParcelada: {}", dsTituloDespesaParcelada);
        return despesasParceladasServices.validarTituloDespesaParceladaExistente(dsTituloDespesaParcelada, idDespesaParcelada, idFuncionario);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void quitarDespesaParcelada(Integer idDespesaParcelada, Integer idFuncionario, String valorQuitacao) {
        log.info("Realizando a baixa total da despesa parcelada - idDespesaParcelada: {} - valorQuitacao: {}", idDespesaParcelada, valorQuitacao);
        despesasParceladasServices.quitarDespesaParcelada(idDespesaParcelada, idFuncionario, valorQuitacao);
    }

    @SneakyThrows
    public StringResponse obterCalculoValorTotalDespesaParceladaPendente(Integer idFuncionario) {
        var result = repository.getValorTotalDespesaParceladaPendente(null, idFuncionario);

        log.info("Obtendo calculo valor total despesa parcelada em aberto - valor: {}", result);

        return StringResponse.builder()
                .vlCalculo(result)
                .build();
    }

    @SneakyThrows
    public StringResponse validaDespesaParceladaExistente(String dsTituloDespesaParcelada, Integer idFuncionario) {
        log.info("Validando se a despesa parcelada existe na base - dsTituloDespesaParcelada: {} - idFuncionario: {}", dsTituloDespesaParcelada, idFuncionario);
        var response = repository.getDespesaParcelada(null, dsTituloDespesaParcelada, idFuncionario);

        Boolean result = true;
        if (ObjectUtils.isEmpty(response)) {
            result = false;
        }

        log.info("Response: Despesa Existente ?? - {}", result);
        return StringResponse.builder()
                .isDespesaExistente(result)
                .build();
    }

    @SneakyThrows
    public StringResponse obterRelatorioDespesasParceladasQuitacao(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario) {
        log.info("Obtendo relatorio despesas parceladas que serão quitadas - idDespesa: {} - idDetalheDespesa: {}- idFuncionario: {}", idDespesa, idDetalheDespesa, idFuncionario);
        return despesasParceladasServices.obterRelatorioDespesasParceladasQuitacao(idDespesa, idDetalheDespesa, idFuncionario);
    }

    public StringResponse processarBackupBaseDados() {
        log.info("========== Realizando o backup da base de dados ==========");
        return backupServices.processarBackup();
    }

    public AutenticacaoResponse autenticarUsuario(LoginRequest request) {
        return autenticacaoServices.autenticarUsuario(request);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void limparDadosTemporarios(Integer idFuncionario) {
        log.info("Limpando dados temporarios de lançamentos mensais");
        repository.deleteDespesasFixasMensaisTemp(idFuncionario);
        repository.deleteDespesasMensaisTemp(idFuncionario);
        repository.deleteDetalheDespesasMensaisTemp(idFuncionario);
    }

    public ConfiguracaoLancamentosResponse obterConfiguracaoLancamentos(Integer idFuncionario) {
        log.info("Obtendo parametros sistemicos");
        var response = repository.getConfiguracaoLancamentos(idFuncionario);
        response.setQtdeLembretes(this.obterListaMonitorLembretes(idFuncionario).size());
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void gravarConfiguracoesLancamentos(ConfiguracaoLancamentosRequest request) {
        log.info("Gravando parametros sistemicos - {}", request);
        request.setViradaAutomatica(request.isBViradaAutomatica() == true ? 'S' : 'N');
        repository.updateConfiguracoesLancamentos(request);
    }

    public LembretesDAO obterDetalheLembrete(Integer idLembrete, Integer idFuncionario) {
        log.info("Obtendo detalhes lembrete id = {}", idLembrete);
        return lembreteServices.getLembreteDetalhe(idLembrete, idFuncionario);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void baixarLembretesMonitor(List<TituloLembretesDAO> request, String tipoBaixa) {
        log.info("Realizando a baixa de lembretes >>> tipoBaixa: {} - request: {}", request, tipoBaixa);
        lembreteServices.baixarLembretesMonitor(request, tipoBaixa);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void gravarLembrete(LembretesDAO request) {
        lembreteServices.gravarLembrete(request);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public List<TituloLembretesDAO> obterListaMonitorLembretes(Integer idFuncionario) {
        log.info("Obtendo lista de lembretes");
        return lembreteServices.getListaMonitorLembretes(idFuncionario);
    }

    public List<TituloLembretesDAO> obterTituloLembretes(Integer idFuncionario, Boolean tpBaixado) {
        log.info("Obtendo lista de nomes dos lembretes");
        return lembreteServices.getListaNomesLembretes(idFuncionario, tpBaixado);
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
