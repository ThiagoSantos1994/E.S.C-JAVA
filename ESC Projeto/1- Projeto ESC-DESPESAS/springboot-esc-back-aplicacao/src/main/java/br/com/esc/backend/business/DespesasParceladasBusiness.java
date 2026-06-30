package br.com.esc.backend.business;

import br.com.esc.backend.domain.*;
import br.com.esc.backend.repository.AplicacaoRepository;
import br.com.esc.backend.service.DespesasParceladasServices;
import br.com.esc.backend.utils.DataUtils;
import br.com.esc.backend.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.esc.backend.utils.DataUtils.anoSeguinte;

@Component
@RequiredArgsConstructor
@Slf4j
public class DespesasParceladasBusiness {

    private final AplicacaoRepository repository;
    private final DespesasParceladasServices despesasParceladasServices;
    private final ChaveKeyBusiness chaveKeyBusiness;
    private final ConsolidacaoBusiness consolidacaoBusiness;

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
        var idDespesa = (idDespesaParcelada == -1 ? chaveKeyBusiness.retornaNovaChaveKey("DESPESASPARCELADAS").getNovaChave() : idDespesaParcelada);
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
    public BooleanResponse validarTituloDespesaParceladaExistente(String dsTituloDespesaParcelada, Integer idDespesaParcelada, Integer idFuncionario) {
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
                .data(result)
                .build();
    }

    @SneakyThrows
    public BooleanResponse validaDespesaParceladaExistente(String dsTituloDespesaParcelada, Integer idFuncionario) {
        log.info("Validando se a despesa parcelada existe na base - dsTituloDespesaParcelada: {} - idFuncionario: {}", dsTituloDespesaParcelada, idFuncionario);
        var response = repository.getDespesaParcelada(null, dsTituloDespesaParcelada, idFuncionario);

        boolean result = !ObjectUtils.isEmpty(response);

        log.info("Response: Despesa Existente ?? - {}", result);
        return BooleanResponse.builder()
                .isValid(result)
                .build();
    }

    @SneakyThrows
    public StringResponse obterRelatorioDespesasParceladasQuitacao(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario) {
        log.info("Obtendo relatorio despesas parceladas que serão quitadas - idDespesa: {} - idDetalheDespesa: {} - idFuncionario: {}", idDespesa, idDetalheDespesa, idFuncionario);
        return despesasParceladasServices.obterRelatorioDespesasParceladasQuitacao(idDespesa, idDetalheDespesa, idFuncionario);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void adiarFluxoParcelas(List<DetalheDespesasMensaisRequest> request) {
        for (DetalheDespesasMensaisRequest despesa : request) {
            if (despesa.getIdConsolidacao() == 0) {
                log.info("Processando fluxo adiar parcelas - request: = idDespesa: {}, idDetalheDespesa = {}, idDespesaParcelada = {}, idParcela = {}, idFuncionario = {}", despesa.getIdDespesa(), despesa.getIdDetalheDespesa(), despesa.getIdDespesaParcelada(), despesa.getIdParcela(), despesa.getIdFuncionario());
                despesasParceladasServices.adiarFluxoParcelas(despesa.getIdDespesa(), despesa.getIdDetalheDespesa(), despesa.getIdDespesaParcelada(), despesa.getIdParcela(), despesa.getIdFuncionario());
            } else if (despesa.getIdConsolidacao() > 0) {
                for (DetalheDespesasMensaisDAO consolidacao : consolidacaoBusiness.obterListDetalheDespesasConsolidadas(despesa.getIdDespesa(), despesa.getIdDetalheDespesa(), despesa.getIdConsolidacao(), despesa.getIdFuncionario())) {
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
                for (DetalheDespesasMensaisDAO consolidacao : consolidacaoBusiness.obterListDetalheDespesasConsolidadas(despesa.getIdDespesa(), despesa.getIdDetalheDespesa(), despesa.getIdConsolidacao(), despesa.getIdFuncionario())) {
                    log.info("Processando fluxo para DESFAZER** o adiamento fluxo de parcelas (Despesas Consolidadas) - request = idDespesa: {}, idDetalheDespesa = {}, idDespesaParcelada = {}, idParcela = {}, idDespesaConsolidacao = {}, idFuncionario = {}", consolidacao.getIdDespesa(), consolidacao.getIdDetalheDespesa(), consolidacao.getIdDespesaParcelada(), consolidacao.getIdParcela(), consolidacao.getIdDespesaConsolidacao(), consolidacao.getIdFuncionario());
                    despesasParceladasServices.desfazerFluxoParcelasAdiadas(consolidacao.getIdDespesa(), consolidacao.getIdDetalheDespesa(), consolidacao.getIdDespesaParcelada(), consolidacao.getIdParcela(), consolidacao.getIdFuncionario());
                }
                /*Altera a flag de ParcelaAdiada no detalhe das despesas mensais tipo consolidacao, desfaz a baixa o pagamento e desmarca como despesa de anotacao*/
                repository.updateDetalheDespesasMensaisConsolidadaAdiadaDesfazer(despesa.getIdDespesa(), despesa.getIdDetalheDespesa(), despesa.getIdConsolidacao(), despesa.getIdFuncionario());
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void atualizaStatusDespesasParceladasEmAberto(Integer idFuncionario) {
        repository.updateDespesasParceladasEmAberto(idFuncionario);
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
}

