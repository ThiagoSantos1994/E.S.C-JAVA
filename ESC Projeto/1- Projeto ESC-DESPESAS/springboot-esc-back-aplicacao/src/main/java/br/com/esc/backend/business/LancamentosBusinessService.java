package br.com.esc.backend.business;

import br.com.esc.backend.domain.*;
import br.com.esc.backend.exception.CamposObrigatoriosException;
import br.com.esc.backend.exception.ErroNegocioException;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.esc.backend.service.DetalheDespesasServices.isDetalheDespesaTipoRelatorio;
import static br.com.esc.backend.service.LembreteServices.validarCamposEntradaLembrete;
import static br.com.esc.backend.utils.DataUtils.anoSeguinte;
import static br.com.esc.backend.utils.ObjectUtils.isNull;


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
    private final ConsolidacaoService consolidacaoService;

    @SneakyThrows
    public LancamentosFinanceirosDTO obterLancamentosFinanceiros(String dsMes, String dsAno, Integer idFuncionario) {
        log.info("Consultando lancamentos financeiros - request: dsMes= {} - dsAno= {} - idFuncionario= {}", dsMes, dsAno, idFuncionario);

        var result = lancamentosServices.obterLancamentosFinanceiros(dsMes, dsAno, idFuncionario);
        result.setLabelQuitacaoParcelasMes(detalheDespesasServices.obterExtratoDespesasMes(result.getIdDespesa(), null, idFuncionario, "lancamentosMensais").getMensagem());

        return result;
    }

    @SneakyThrows
    public List<LancamentosMensaisDAO> obterDespesasMensaisConsolidadas(Integer idDespesa, Integer idConsolidacao, Integer idFuncionario) {
        log.info("Consultando despesas mensais consolidadas >>>  idDespesa = {} - idConsolidacao = {} - idFuncionario = {}", idDespesa, idConsolidacao, idFuncionario);
        return lancamentosServices.obterLancamentosMensaisConsolidados(idDespesa, idConsolidacao, idFuncionario);
    }

    @SneakyThrows
    public DetalheDespesasMensaisDTO obterDetalheDespesaMensal(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String ordem, Boolean visualizarConsolidacao) {
        log.info("Consultando detalhes despesa mensal >>>  idDespesa = {} - idDetalheDespesa = {} - idFuncionario = {} - ordem = {}", idDespesa, idDetalheDespesa, idFuncionario, ordem);
        return detalheDespesasServices.obterDetalheDespesaMensal(idDespesa, idDetalheDespesa, idFuncionario, ordem, visualizarConsolidacao);
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
        repository.desassociarConsolidacaoDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario);
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

            if (detalhe.getIdConsolidacao() > 0) {
                log.info("Removendo a consolidacao das despesas parceladas importadas que foram consolidadas.. idConsolidacao: {}", detalhe.getIdConsolidacao());
                consolidacaoService.excluirConsolidacaoDetalheDespesaMensal(detalhe.getIdDespesa(), detalhe.getIdDetalheDespesa(), detalhe.getIdConsolidacao(), detalhe.getIdFuncionario());
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void deleteTodosLancamentosMensais(Integer idDespesa, Integer idFuncionario) {
        log.info("Excluindo todas despesas fixas mensais - request: idDespesa= {} - idFuncionario= {}", idDespesa, idFuncionario);
        repository.deleteTodasDespesasFixasMensais(idDespesa, idFuncionario);

        log.info("Excluindo todas despesas mensais - request: idDespesa= {} - idFuncionario= {}", idDespesa, idFuncionario);
        repository.deleteTodasDespesasMensais(idDespesa, idFuncionario);

        var listDespesasParceladas = repository.getDespesasParceladasDetalheDespesasMensais(idDespesa, idFuncionario).stream()
                .filter(d -> d.getTpParcelaAmortizada().equalsIgnoreCase("S"))
                .collect(Collectors.toList());

        for (DetalheDespesasMensaisDAO detalhe : listDespesasParceladas) {
            despesasParceladasServices.validaStatusDespesaParcelada(detalhe.getIdDespesa(), detalhe.getIdDetalheDespesa(), detalhe.getIdDespesaParcelada(), detalhe.getIdParcela(), detalhe.getIdFuncionario(), detalhe.getTpStatus(), true);
        }

        log.info("Excluindo todos detalhes despesas mensais - request: idDespesa= {} - idFuncionario= {}", idDespesa, idFuncionario);
        repository.deleteTodosDetalhesDespesasMensais(idDespesa, idFuncionario);

        log.info("Excluindo todas observacoes detalhes despesas mensais - request: idDespesa= {} - idFuncionario= {}", idDespesa, idFuncionario);
        repository.deleteTodasObservacaoDetalheDespesaMensal(idDespesa, null, idFuncionario);

        log.info("Excluindo todos logs detalhes despesas mensais - request: idDespesa= {} - idFuncionario= {}", idDespesa, idFuncionario);
        repository.deleteTodasDespesasMensaisLogs(idDespesa, idFuncionario);

        log.info("Atualizando status das parcelas comuns e amortizadas para Pendente - request: idDespesa= {} - idFuncionario= {}", idDespesa, idFuncionario);
        repository.updateParcelaStatusPendenteDespesasExcluidas(idDespesa, idFuncionario);

        log.info("Atualizando status das despesas parceladas para Em Aberto - request: idFuncionario= {}", idFuncionario);
        this.atualizaStatusDespesasParceladasEmAberto(idFuncionario);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void processarImportacaoDespesasMensais(Integer idDespesa, Integer idFuncionario, String dsMes, String dsAno) {
        log.info("Processando importacao lancamentos e despesas mensais - idDespesa {} - idFuncionario {} - dsMes {} - dsAno {}", idDespesa, idFuncionario, dsMes, dsAno);
        importacaoServices.processarImportacaoDespesasMensais(idDespesa, idFuncionario, dsMes, dsAno);
        this.atualizaStatusDespesasParceladasEmAberto(idFuncionario);
        this.ordenarListaDespesasMensais(idDespesa, idFuncionario);
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
    public void processarImportacaoDespesaParcelada(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idConsolidacao, Integer idFuncionario) {
        if (idConsolidacao > 0) {
            log.info("Iniciando processamento importacao consolidacao - Filtros: idDespesa= {} - idDetalheDespesa= {} - idDespesaParcelada= {} - idConsolidacao= {} - idFuncionario= {}", idDespesa, idDetalheDespesa, idDespesaParcelada, idConsolidacao, idFuncionario);
            importacaoServices.processarImportacaoConsolidacao(idDespesa, idDetalheDespesa, idConsolidacao, idFuncionario);
        } else {
            log.info("Iniciando processamento importacao despesa parcelada - Filtros: idDespesa= {} - idDetalheDespesa= {} - idDespesaParcelada= {} - idFuncionario= {}", idDespesa, idDetalheDespesa, idDespesaParcelada, idFuncionario);
            importacaoServices.processarImportacaoDespesaParcelada(idDespesa, idDetalheDespesa, idDespesaParcelada, null, idFuncionario);
        }

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

        if ((request.getTpLinhaSeparacao().equalsIgnoreCase("S") || request.getTpDespesaConsolidacao().equalsIgnoreCase("S"))
                && request.getIdDetalheDespesa().equals(-1)) {
            mensaisDAO.setIdDetalheDespesa(this.retornaNovaChaveKey("DETALHEDESPESA").getNovaChave());
        }
        detalheDespesasServices.gravarDespesasMensais(mensaisDAO);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void gravarDetalheDespesasMensais(List<DetalheDespesasMensaisRequest> request) {
        for (DetalheDespesasMensaisRequest detalhe : request) {
            DetalheDespesasMensaisDAO dao = new DetalheDespesasMensaisDAO();
            BeanUtils.copyProperties(dao, detalhe);
            detalheDespesasServices.gravarDetalheDespesasMensais(dao);

            if (detalhe.getIdConsolidacao() > 0) {
                for (DetalheDespesasMensaisDAO despesa : consolidacaoService.obterListDetalheDespesasConsolidadas(detalhe.getIdDespesa(), detalhe.getIdDetalheDespesa(), detalhe.getIdConsolidacao(), detalhe.getIdFuncionario())) {
                    var despesaRequest = detalheDespesasServices.parserToDetalheDespesasConsolidadas(detalhe, despesa);
                    detalheDespesasServices.gravarDetalheDespesasMensais(despesaRequest);
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void processarPagamentoDetalheDespesa(List<PagamentoDespesasRequest> request) {
        for (PagamentoDespesasRequest despesa : request) {
            log.info("Processando pagamento detalhe despesa mensal - Request: {}", despesa);
            detalheDespesasServices.baixarPagamentoDespesa(despesa);
            detalheDespesasServices.baixarPagamentoDespesasConsolidadas(despesa);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void processarDesfazerPagamentoDetalheDespesa(List<PagamentoDespesasRequest> request) {
        for (PagamentoDespesasRequest despesa : request) {
            log.info("Processando rollback pagamento detalhe despesa mensal - Request: {}", despesa);
            detalheDespesasServices.desfazerBaixaPagamentoDespesas(despesa);
            detalheDespesasServices.desfazerBaixaPagamentoDespesasConsolidadas(despesa);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void processarPagamentoDespesa(List<LancamentosMensaisDAO> lancamentosMensaisList) {
        for (LancamentosMensaisDAO despesa : lancamentosMensaisList) {
            var isDespesaConsolidacao = despesa.getTpDespesaConsolidacao().equals("S");

            if (isDespesaConsolidacao) {
                List<LancamentosMensaisDAO> lancamentosMensais = repository.getLancamentosMensaisConsolidados(despesa.getIdDespesa(), despesa.getIdConsolidacao(), despesa.getIdFuncionario());
                for (LancamentosMensaisDAO mensaisDAO : lancamentosMensais) {
                    this.processarPagamentoDetalheDespesa(detalheDespesasServices.obterDetalheDespesasParaPagamento(mensaisDAO));
                }
            } else {
                this.processarPagamentoDetalheDespesa(detalheDespesasServices.obterDetalheDespesasParaPagamento(despesa));
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void desfazerPagamentoDespesas(List<LancamentosMensaisDAO> lancamentosMensaisList) {
        for (LancamentosMensaisDAO despesa : lancamentosMensaisList) {
            var isDespesaConsolidacao = despesa.getTpDespesaConsolidacao().equals("S");

            if (isDespesaConsolidacao) {
                List<LancamentosMensaisDAO> lancamentosMensais = repository.getLancamentosMensaisConsolidados(despesa.getIdDespesa(), despesa.getIdConsolidacao(), despesa.getIdFuncionario());
                for (LancamentosMensaisDAO mensaisDAO : lancamentosMensais) {
                    this.processarDesfazerPagamentoDetalheDespesa(detalheDespesasServices.obterDetalheDespesasParaDesfazerPagamento(mensaisDAO));
                }
            } else {
                this.processarDesfazerPagamentoDetalheDespesa(detalheDespesasServices.obterDetalheDespesasParaDesfazerPagamento(despesa));
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void adiarFluxoParcelas(List<DetalheDespesasMensaisRequest> request) {
        for (DetalheDespesasMensaisRequest despesa : request) {
            if (despesa.getIdConsolidacao() == 0) {
                log.info("Processando fluxo adiar parcelas - request: = idDespesa: {}, idDetalheDespesa = {}, idDespesaParcelada = {}, idParcela = {}, idFuncionario = {}", despesa.getIdDespesa(), despesa.getIdDetalheDespesa(), despesa.getIdDespesaParcelada(), despesa.getIdParcela(), despesa.getIdFuncionario());
                despesasParceladasServices.adiarFluxoParcelas(despesa.getIdDespesa(), despesa.getIdDetalheDespesa(), despesa.getIdDespesaParcelada(), despesa.getIdParcela(), despesa.getIdFuncionario());
            } else if (despesa.getIdConsolidacao() > 0) {
                for (DetalheDespesasMensaisDAO consolidacao : consolidacaoService.obterListDetalheDespesasConsolidadas(despesa.getIdDespesa(), despesa.getIdDetalheDespesa(), despesa.getIdConsolidacao(), despesa.getIdFuncionario())) {
                    log.info("Processando fluxo adiar parcelas (Despesas Consolidadas) - request = idDespesa: {}, idDetalheDespesa = {}, idDespesaParcelada = {}, idParcela = {}, idDespesaConsolidacao = {}, idFuncionario = {}", consolidacao.getIdDespesa(), consolidacao.getIdDetalheDespesa(), consolidacao.getIdDespesaParcelada(), consolidacao.getIdParcela(), consolidacao.getIdDespesaConsolidacao(), consolidacao.getIdFuncionario());
                    despesasParceladasServices.adiarFluxoParcelas(consolidacao.getIdDespesa(), consolidacao.getIdDetalheDespesa(), consolidacao.getIdDespesaParcelada(), consolidacao.getIdParcela(), consolidacao.getIdFuncionario());
                }
                /*Altera a flag de ParcelaAdiada no detalhe das despesas mensais tipo consolidacao e baixa o pagamento e marca como despesa de anotacao*/
                repository.updateDetalheDespesasMensaisDespesaConsolidadaAdiada(despesa.getIdDespesa(), despesa.getIdDetalheDespesa(), despesa.getIdConsolidacao(), despesa.getVlTotal(), despesa.getIdFuncionario());
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void desfazerAdiamentoFluxoParcelas(List<DetalheDespesasMensaisRequest> request) {
        for (DetalheDespesasMensaisRequest despesa : request) {
            if (despesa.getIdConsolidacao() == 0) {
                log.info("Processando fluxo para DESFAZER** o adiamento fluxo de parcelas - request: idDespesa = {}, idDetalheDespesa = {}, idDespesaParcelada = {}, idParcela = {}, idFuncionario = {}", despesa.getIdDespesa(), despesa.getIdDetalheDespesa(), despesa.getIdDespesaParcelada(), despesa.getIdParcela(), despesa.getIdFuncionario());
                despesasParceladasServices.desfazerFluxoParcelasAdiadas(despesa.getIdDespesa(), despesa.getIdDetalheDespesa(), despesa.getIdDespesaParcelada(), despesa.getIdParcela(), despesa.getIdFuncionario());
            } else if (despesa.getIdConsolidacao() > 0) {
                for (DetalheDespesasMensaisDAO consolidacao : consolidacaoService.obterListDetalheDespesasConsolidadas(despesa.getIdDespesa(), despesa.getIdDetalheDespesa(), despesa.getIdConsolidacao(), despesa.getIdFuncionario())) {
                    log.info("Processando fluxo para DESFAZER** o adiamento fluxo de parcelas (Despesas Consolidadas) - request = idDespesa: {}, idDetalheDespesa = {}, idDespesaParcelada = {}, idParcela = {}, idDespesaConsolidacao = {}, idFuncionario = {}", consolidacao.getIdDespesa(), consolidacao.getIdDetalheDespesa(), consolidacao.getIdDespesaParcelada(), consolidacao.getIdParcela(), consolidacao.getIdDespesaConsolidacao(), consolidacao.getIdFuncionario());
                    despesasParceladasServices.desfazerFluxoParcelasAdiadas(consolidacao.getIdDespesa(), consolidacao.getIdDetalheDespesa(), consolidacao.getIdDespesaParcelada(), consolidacao.getIdParcela(), consolidacao.getIdFuncionario());
                }
                /*Altera a flag de ParcelaAdiada no detalhe das despesas mensais tipo consolidacao, desfaz a baixa o pagamento e desmarca como despesa de anotacao*/
                repository.updateDetalheDespesasMensaisDespesaConsolidadaAdiadaDesfazer(despesa.getIdDespesa(), despesa.getIdDetalheDespesa(), despesa.getIdConsolidacao(), despesa.getIdFuncionario());
            }
        }
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
        detalheDespesasServices.alterarOrdemRegistroDetalheDespesas(idDespesa, idDetalheDespesa, isDetalheDespesaTipoRelatorio(iOrdemAtual), iOrdemNova, idFuncionario);
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
    public void alterarTituloDespesa(Integer idDetalheDespesa, Integer idFuncionario, String dsNomeDespesa, String anoReferencia) {
        log.info("Alterando titulo despesas >> idDetalheDespesa: {} para: {}", idDetalheDespesa, dsNomeDespesa);
        lancamentosServices.alterarTituloDespesa(idDetalheDespesa, idFuncionario, dsNomeDespesa, anoReferencia);
    }

    @SneakyThrows
    public StringResponse validaTituloDespesaDuplicado(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String dsTituloDespesa) {
        var response = lancamentosServices.validaTituloDespesaDuplicado(idDespesa, idDetalheDespesa, idFuncionario, dsTituloDespesa);

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

    public StringResponse obterObservacoesDetalheDespesa(Integer idDespesa, Integer idDetalheDespesa, Integer idObservacao, Integer idFuncionario) {
        log.info("Consultando observações dos detalhes da despesa");
        return detalheDespesasServices.getObservacoesDetalheDespesa(idDespesa, idDetalheDespesa, idObservacao, idFuncionario);
    }

    public StringResponse obterHistoricoDetalheDespesa(Integer idDetalheDespesaLog, Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario) {
        return detalheDespesasServices.getHistoricoDetalheDespesa(idDetalheDespesaLog, idDespesa, idDetalheDespesa, idFuncionario);
    }

    public void gravarObservacoesDetalheDespesa(ObservacoesDetalheDespesaRequest request) {
        log.info("Gravando observações detalhe despesa >> Request: {}", request);
        detalheDespesasServices.gravarObservacoesDetalheDespesa(request);
    }

    public DespesasParceladasResponse obterDespesasParceladas(Integer idFuncionario, String status) {
        log.info("Consultando lista de despesas parceladas");
        var response = despesasParceladasServices.getDespesasParceladas(idFuncionario, status);
        response.setAnosReferenciaFiltro(this.obterListaAnosReferencia());
        return response;
    }

    public StringResponse obterValorDespesaParcelada(Integer idDespesaParcelada, Integer idParcela, String mesAnoReferencia, Integer idFuncionario) {
        log.info("Consultando valor despesa parcelada >>> filtros: idDespesaParcelada: {} - idParcela: {} - mesAnoReferencia: {} - idFuncionario: {}", idDespesaParcelada, idParcela, mesAnoReferencia, idFuncionario);
        return despesasParceladasServices.obterValorDespesa(idDespesaParcelada, idParcela, mesAnoReferencia, idFuncionario);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void deleteParcela(List<ParcelasDAO> parcelas) {
        for (ParcelasDAO parcela : parcelas) {
            log.info("Excluindo parcela >>> filtros: idDespesaParcelada: {} - idParcela: {} - idFuncionario: {}", parcela.getIdDespesaParcelada(), parcela.getIdParcela(), parcela.getIdFuncionario());
            despesasParceladasServices.excluirParcela(parcela.getIdDespesaParcelada(), parcela.getIdParcela(), parcela.getIdFuncionario());
        }
        /*Retirada a baixa automatica da despesa apos exclusão das parcelas para reprocessamento. 24/10/2024*/
        //despesasParceladasServices.validarBaixaCadastroDespesaParcelada(parcelas.get(0).getIdDespesaParcelada(), parcelas.get(0).getIdFuncionario());
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void deleteDespesaParcelada(Integer idDespesaParcelada, Integer idFuncionario) {
        log.info("Excluindo despesa parcelada >>> filtros: idDespesaParcelada: {} - idFuncionario: {}", idDespesaParcelada, idFuncionario);
        despesasParceladasServices.excluirDespesaParcelada(idDespesaParcelada, idFuncionario);
    }

    public DetalheDespesasParceladasResponse obterDespesaParceladaPorNome(String nomeDespesaParcelada, Integer idFuncionario) {
        log.info("Consultando detalhe despesa parcelada por filtros >>> nomeDespesaParcelada= {} - idFuncionario= {}", nomeDespesaParcelada, idFuncionario);
        return despesasParceladasServices.obterDespesaParceladaPorFiltros(null, nomeDespesaParcelada, idFuncionario, false);
    }

    public CategoriaDespesasResponse obterSubTotalCategoriaDespesa(Integer idDespesa, Integer idFuncionario) {
        log.info("Consultando subTotal categorias despesa >>> idDespesa = {} - idFuncionario= {}", idDespesa, idFuncionario);
        return CategoriaDespesasResponse.builder()
                .mesAnoReferencia(repository.getMesAnoPorID(idDespesa, idFuncionario))
                .categorias(detalheDespesasServices.getSubTotalCategoriaDespesa(idDespesa, idFuncionario))
                .build();
    }

    public CategoriaDespesasResponse obterSubTotalCategoriaDespesaAno(Integer dsAno, Integer idFuncionario) {
        log.info("Consultando subTotal categorias despesa >>> dsAno = {} - idFuncionario= {}", dsAno, idFuncionario);
        return CategoriaDespesasResponse.builder()
                .categorias(detalheDespesasServices.getSubTotalCategoriaDespesaAno(dsAno, idFuncionario))
                .build();
    }

    public DetalheDespesasParceladasResponse obterDespesaParceladaPorID(Integer idDespesaParcelada, Integer idFuncionario, Boolean isPendentes) {
        log.info("Consultando detalhe despesa parcelada por filtros >>> idDespesaParcelada= {} - idFuncionario= {}", idDespesaParcelada, idFuncionario);
        return despesasParceladasServices.obterDespesaParceladaPorFiltros(idDespesaParcelada, null, idFuncionario, isPendentes);
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

        return DetalheDespesasParceladasResponse.builder()
                .idDespesaParcelada(idDespesa)
                .despesas(DespesaParceladaDAO.builder().tpBaixado("N").build())
                .despesaVinculada("Novo fluxo de parcelas, clique em SALVAR para gravar esta despesa parcelada.")
                .parcelas(fluxoParcelas.getParcelas())
                .build();
    }

    public StringResponse consultarNomeDespesaParceladaPorFiltro(Integer idDespesaParcelada, Integer idFuncionario) {
        log.info("Obtendo Nome Despesa Parcelada Por Filtros >>> idDespesaParcelada: {} - idFuncionario: {}", idDespesaParcelada, idFuncionario);
        return despesasParceladasServices.getNomeDespesaParceladaPorFiltro(idDespesaParcelada, idFuncionario);
    }

    public TituloDespesaResponse consultarDespesasParceladasParaImportacao(Integer idFuncionario, String tipo) {
        log.info("Obtendo Lista de Nomes Despesas Parceladas para Importacao - Filtros >>> idFuncionario: {} - tipo: {}", idFuncionario, tipo);
        return despesasParceladasServices.getNomeDespesasParceladasParaImportacao(idFuncionario, tipo);
    }

    public TituloDespesaResponse consultarConsolidacoesParaAssociacao(Integer idFuncionario, Integer idDespesa, Integer idDetalheDespesa, String tipo) {
        log.info("Obtendo Lista de Nomes Consolidacoes para Associacao de despesas - Filtros >>> idFuncionario: {} - idDespesa: {} - idDetalheDespesa: {} - tipo: {}", idFuncionario, idDespesa, idDetalheDespesa, tipo);
        return consolidacaoService.getNomeConsolidacaoParaAssociacao(idFuncionario, idDespesa, idDetalheDespesa, tipo);
    }

    public TituloDespesaResponse consultarDespesasMensaisParaAssociacao(Integer idDespesa, Integer idFuncionario, Integer anoReferencia) {
        log.info("Obtendo Lista de Despesas Mensais para Associacao - Filtros >>> idDespesa: {} - idFuncionario: {} - anoReferencia: {}", idDespesa, idFuncionario, anoReferencia);
        return detalheDespesasServices.getNomeDespesasMensaisParaAssociacao(idDespesa, idFuncionario, anoReferencia);
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
        log.info("Realizando a quitação total da despesa parcelada - idDespesaParcelada: {} - valorQuitacao: {}", idDespesaParcelada, valorQuitacao);
        despesasParceladasServices.quitarTotalmenteDespesaParcelada(idDespesaParcelada, idFuncionario, valorQuitacao);
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

        boolean result = !ObjectUtils.isEmpty(response);

        log.info("Response: Despesa Existente ?? - {}", result);
        return StringResponse.builder()
                .isDespesaExistente(result)
                .build();
    }

    @SneakyThrows
    public StringResponse obterRelatorioDespesasParceladasQuitacao(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario) {
        log.info("Obtendo relatorio despesas parceladas que serão quitadas - idDespesa: {} - idDetalheDespesa: {} - idFuncionario: {}", idDespesa, idDetalheDespesa, idFuncionario);
        return despesasParceladasServices.obterRelatorioDespesasParceladasQuitacao(idDespesa, idDetalheDespesa, idFuncionario);
    }

    @SneakyThrows
    public StringResponse obterRelatorioDespesasParceladasConsolidadas(Integer idDespesa, Integer idDetalheDespesa, Integer idConsolidacao, Integer idFuncionario) {
        log.info("Obtendo relatorio despesas parceladas consolidadas - idDespesa: {} - idDetalheDespesa: {} - idConsolidacao: {} - idFuncionario: {}", idDespesa, idDetalheDespesa, idConsolidacao, idFuncionario);
        return consolidacaoService.obterRelatorioDespesasParceladasConsolidadas(idDespesa, idDetalheDespesa, idConsolidacao, idFuncionario);
    }

    @SneakyThrows
    public StringResponse processarBackupBaseDados() {
        log.info("========== Realizando o backup da base de dados ==========");
        return backupServices.processarBackup();
    }

    public AutenticacaoResponse autenticarUsuario(LoginRequest request) {
        return autenticacaoServices.autenticarUsuario(request);
    }

    private List<String> obterListaAnosReferencia() {
        List<String> responseList = new ArrayList<>();
        log.info("Obtendo lista de anos referencia...");

        try {
            var listAnosRef = repository.getListaAnoReferencia();
            if (!listAnosRef.contains(anoSeguinte())) {
                responseList.add(anoSeguinte());
            }
            responseList.addAll(listAnosRef);
        } catch (Exception ex) {
            responseList.add(DataUtils.anoAtual());
        }

        return responseList;
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
        response.setAnosReferenciaFiltro(this.obterListaAnosReferencia());

        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void gravarConfiguracoesLancamentos(ConfiguracaoLancamentosRequest request) {
        log.info("Gravando parametros sistemicos - {}", request);
        request.setViradaAutomatica(request.isBViradaAutomatica() ? 'S' : 'N');
        repository.updateConfiguracoesLancamentos(request);
    }

    public LembretesDAO obterDetalheLembrete(Integer idLembrete, Integer idFuncionario) {
        log.info("Obtendo detalhes lembrete id = {}", idLembrete);
        return lembreteServices.getLembreteDetalhe(idLembrete, idFuncionario);
    }

    public ConsolidacaoDAO obterDetalheConsolidacao(Integer idConsolidacao, Integer idFuncionario) {
        log.info("Obtendo detalhes consolidacao id = {}", idConsolidacao);
        return consolidacaoService.getDetalheConsolidacao(idConsolidacao, idFuncionario);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void baixarLembretesMonitor(List<TituloLembretesDAO> request, String tipoBaixa) {
        log.info("Realizando a baixa de lembretes >>> tipoBaixa: {} - request: {}", request, tipoBaixa);
        lembreteServices.baixarLembretesMonitor(request, tipoBaixa);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void gravarConsolidacao(ConsolidacaoDAO request) {
        log.info("Gravando consolidacao >>> request: {}", request);
        consolidacaoService.gravarConsolidacao(request);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void excluirConsolidacao(ConsolidacaoDAO request) {
        log.info("Excluindo consolidacao >>> request: {}", request);
        consolidacaoService.excluirConsolidacao(request);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void associarDespesaDetalheDespesasConsolidacao(Integer idConsolidacao, List<DetalheDespesasMensaisRequest> request) {
        log.info("Associando detalhe despesa a consolidacao >>> - idConsolidacao: {} - request: {}", idConsolidacao, request);
        for (DetalheDespesasMensaisRequest detalhe : request) {
            var isDespesaExistente = repository.getDetalhesConsolidacao(idConsolidacao, detalhe.getIdFuncionario()).stream()
                    .filter(d -> d.getIdDespesaParcelada().equals(detalhe.getIdDespesaParcelada()))
                    .collect(Collectors.toList());

            if (isDespesaExistente.isEmpty()) {
                consolidacaoService.associarDespesa(Arrays.asList(ConsolidacaoDespesasRequest.builder()
                        .idConsolidacao(idConsolidacao)
                        .idDespesaParcelada(detalhe.getIdDespesaParcelada())
                        .idFuncionario(detalhe.getIdFuncionario())
                        .build()));
            }

            consolidacaoService.associarConsolidacaoDetalheDespesaMensal(detalhe.getIdDespesa(), detalhe.getIdDetalheDespesa(), idConsolidacao, detalhe.getIdFuncionario());
        }

        final var idDespesaRef = request.get(0).getIdDespesa();
        final var idFuncionarioRef = request.get(0).getIdFuncionario();

        this.lancamentosServices.ordenarListaDespesasMensais(idDespesaRef, idFuncionarioRef);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void associarDespesaMensalConsolidacao(Integer idConsolidacao, List<DespesasMensaisRequest> request) {
        log.info("Associando despesa a consolidacao >>> - idConsolidacao: {} - request: {}", idConsolidacao, request);
        for (DespesasMensaisRequest despesa : request) {
            DespesasMensaisDAO isDespesaExistente = repository.getDespesaMensalPorFiltro(despesa.getIdDespesa(), despesa.getIdDetalheDespesa(), despesa.getIdFuncionario());

            if (ObjectUtils.isEmpty(isDespesaExistente)) {
                throw new ErroNegocioException("Despesa inexistente na base de dados, não será possivel associar a um grupo de consolidacao.");
            }

            DespesasMensaisDAO mensaisDAO = new DespesasMensaisDAO();
            BeanUtils.copyProperties(mensaisDAO, despesa);

            mensaisDAO.setIdConsolidacao(idConsolidacao);
            mensaisDAO.setTpDespesaConsolidacao("N");
            detalheDespesasServices.gravarDespesasMensais(mensaisDAO);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void desassociarDespesaMensalConsolidacao(Integer idDespesa, Integer idDetalheDespesa, Integer idConsolidacao, Integer idFuncionario) {
        log.info("Desassociando despesa da consolidacao >>> - idDespesa: {} - idConsolidacao: {}", idDespesa, idConsolidacao);

        List<DespesasMensaisDAO> despesa = repository.getDespesasMensais(idDespesa, idFuncionario, idDetalheDespesa);

        if (ObjectUtils.isEmpty(despesa)) {
            throw new ErroNegocioException("Despesa inexistente na base de dados, não será possivel desassociar do grupo de consolidacao.");
        }

        DespesasMensaisDAO mensaisDAO = new DespesasMensaisDAO();
        BeanUtils.copyProperties(mensaisDAO, despesa.get(0));

        mensaisDAO.setIdConsolidacao(0);
        mensaisDAO.setTpDespesaConsolidacao("N");
        detalheDespesasServices.gravarDespesasMensais(mensaisDAO);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void desassociarDespesasConsolidacao(List<ConsolidacaoDespesasRequest> request) {
        log.info("Desassociando despesa a consolidacao >>> request: {}", request);
        consolidacaoService.desassociarDespesa(request);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void gravarLembrete(LembretesDAO request) {
        log.info("Validando parametros de entrada do lembrete...");
        validarCamposEntradaLembrete(request);

        log.info("Gravando detalhes do lembrete...");
        if (isNull(request.getIdLembrete()) || request.getIdLembrete() == -1) {
            request.setIdLembrete(this.retornaNovaChaveKey("LEMBRETES").getNovaChave());
        }

        lembreteServices.gravarLembrete(request);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void excluirLembrete(LembretesDAO request) {
        log.info("Excluindo lembrete >> request {}", request);
        repository.deleteLembrete(request.getIdLembrete(), request.getIdFuncionario());
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

    public StringResponse validaSessaoUsuario(Integer idFuncionario) {
        return autenticacaoServices.validarSessaoUsuario(idFuncionario);
    }

    public List<TituloConsolidacao> obterTituloConsolidacoes(Integer idFuncionario, Boolean tpBaixado) {
        log.info("Obtendo lista de consolidacoes - baixado : {}", tpBaixado);
        return consolidacaoService.getListaNomesConsolidacoes(idFuncionario, tpBaixado);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public ChaveKeyDAO retornaNovaChaveKey(String tipoChave) {
        log.info("========= Gerando chaveKey, tipoChave: {} =========", tipoChave);

        if (ObjectUtils.isEmpty(tipoChave)) {
            throw new CamposObrigatoriosException("tipoChave e obrigatorio.");
        }

        ChaveKeyDAO keyDAO = repository.getNovaChaveKey(tipoChave);
        if (ObjectUtils.isEmpty(keyDAO)) {
            throw new Exception("Sequencia não identificada na tabela de chaves primárias, favor contatar imediatamente o desenvolvedor do software.");
        }

        ChaveKeySemUsoDAO keySemUsoDAO = repository.getChaveKeySemUso(keyDAO.getDsNomeColuna(), keyDAO.getDsNomeTabela());
        if (keySemUsoDAO.getChave().equals(keyDAO.getNovaChave())) {
            log.info("========= Nova ChaveKey gerada com sucesso, tipoChave: {} novaChave: {} =========", tipoChave, keyDAO.getNovaChave());
            repository.updateChaveKeyUtilizada(keyDAO.getIdChaveKey());
        } else {
            log.info("========= Reutilizando ChaveKey existente sem uso, tipoChave: {} chave: {} =========", tipoChave, keySemUsoDAO.getChave());
            keyDAO.setNovaChave(keySemUsoDAO.getChave());
        }

        //Alterado a logica em 24/11/25 para não considerar o reuso de chaves existentes sem uso.
        //log.info("========= Nova ChaveKey gerada com sucesso, tipoChave: {} novaChave: {} =========", tipoChave, keyDAO.getNovaChave());
        //repository.updateChaveKeyUtilizada(keyDAO.getIdChaveKey());

        return keyDAO;
    }
}
