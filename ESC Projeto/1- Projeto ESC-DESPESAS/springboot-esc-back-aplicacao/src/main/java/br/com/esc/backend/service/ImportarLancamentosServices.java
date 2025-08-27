package br.com.esc.backend.service;

import br.com.esc.backend.domain.*;
import br.com.esc.backend.exception.ErroNegocioException;
import br.com.esc.backend.repository.AplicacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.esc.backend.utils.DataUtils.*;
import static br.com.esc.backend.utils.ObjectUtils.*;
import static br.com.esc.backend.utils.VariaveisGlobais.*;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportarLancamentosServices {

    private final AplicacaoRepository repository;
    private final DetalheDespesasServices detalheDespesasServices;
    private final ConsolidacaoService consolidacaoService;
    private boolean bDespesaComStatusPago;
    private boolean bDespesaComParcelaAdiada;
    private boolean bProcessamentoTemporario;

    public void processarImportacaoDespesasMensais(Integer idDespesa, Integer idFuncionario, String dsMes, String dsAno) throws Exception {
        if (!bProcessamentoTemporario) {
            /*Limpa a base dos dados temporarios gerados para visualizacao temporaria*/
            repository.deleteDespesasFixasMensaisTemp(idFuncionario);
            repository.deleteDespesasMensaisTemp(idFuncionario);
            repository.deleteDetalheDespesasMensaisTemp(idFuncionario);
        }

        Integer idDespesaImportacao = idDespesa;
        if (idDespesa == 0) {
            var despesasFixasMensais = this.processarDespesasFixasMensais(idDespesa, idFuncionario, dsMes, dsAno);
            for (DespesasFixasMensaisRequest despesasFixas : despesasFixasMensais) {
                log.info("Inserindo despesa fixa mensal >>>  {}", despesasFixas);
                repository.insertDespesasFixasMensais(despesasFixas);
            }
            idDespesaImportacao = despesasFixasMensais.get(0).getIdDespesa();
        }

        for (DespesasMensaisDAO despesaMensal : this.processarDespesasMensais(idDespesaImportacao, idFuncionario)) {
            var isDespesaNaoImportada = isEmpty(repository.getDespesasMensais(idDespesa, despesaMensal.getIdFuncionario(), despesaMensal.getIdDetalheDespesa()));
            if (isDespesaNaoImportada) {
                detalheDespesasServices.gravarDespesasMensais(despesaMensal);

                var bReprocessarTodosValores = despesaMensal.getTpReprocessar().equalsIgnoreCase("S");
                this.processarImportacaoDetalheDespesasMensais(idDespesaImportacao, despesaMensal.getIdDetalheDespesa(), idFuncionario, dsMes, dsAno, bReprocessarTodosValores);
            }
        }
    }

    public void processarImportacaoDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String dsMes, String dsAno, Boolean bReprocessarTodosValores) {
        for (DetalheDespesasMensaisDAO detalheDespesa : this.processarDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario, dsMes, dsAno, bReprocessarTodosValores)) {
            if (this.isDetalheDespesaExistente(detalheDespesa)) {
                if (bDespesaComStatusPago) {
                    log.info("Despesa mensal com status PAGO, nao foi atualizado. >>>  {}", detalheDespesa);
                    continue;
                }
                if (bDespesaComParcelaAdiada) {
                    log.info("Despesa mensal com status adiantamento de parcela, nao foi atualizado. >>>  {}", detalheDespesa);
                    continue;
                }

                log.info("Atualizando detalhe despesa mensal >>>  {}", detalheDespesa);
                repository.updateDetalheDespesasMensaisImportacao(detalheDespesa);

                if (bReprocessarTodosValores) {
                    repository.updateValorDetalheDespesasMensais(detalheDespesa);
                }
            } else {
                var idConsolidacao = detalheDespesa.getIdConsolidacao();
                if (idConsolidacao > 0) {
                    var isDespesaParceladaConsolidada = repository.getDespesasParceladasConsolidadasImportacao(idConsolidacao, idFuncionario).size();
                    if (isDespesaParceladaConsolidada == 0) {
                        // Caso a despesa consolidação não tenha mais nenhuma despesa parcelada em aberto, não inclui adiciona a despesa.
                        log.info("Identificado despesa consolidada porem sem despesas parceladas em aberto, não foi adicionada a despesa. - idConsolidacao: {}", idConsolidacao);
                        continue;
                    }
                }

                log.info("Inserindo detalhe despesas mensais >>>  {}", detalheDespesa);
                repository.insertDetalheDespesasMensais(asList(detalheDespesa));

                if (bDespesaComParcelaAdiada) {
                    log.info("Despesa mensal com status adiantamento de parcela, realizando tratamento para gravar. >>>  {}", detalheDespesa);

                    /*Altera a flag de ParcelaAdiada no detalhe das despesas mensais e baixa o pagamento e marca como despesa de anotacao*/
                    var logProcessamento = "Operacao realizada em: " + dataHoraAtual() + " - Usuario: ** " + repository.getUsuarioLogado(idFuncionario);
                    repository.updateDetalheDespesasMensaisParcelaAdiada(idDespesa, idDetalheDespesa, detalheDespesa.getIdDespesaParcelada(), "Despesa adiantada no fluxo de adiantamento de parcelas.", logProcessamento, detalheDespesa.getVlTotal(), idFuncionario);
                }
            }
        }
    }

    public DespesaFixaTemporariaResponse gerarTemporariamenteDespesasMensais(Integer sMes, Integer sAno, Integer idFuncionario) throws Exception {
        Integer idDespesaReferencia;
        Integer iMesReferencia = ((sMes + 1) > 12) ? 1 : (sMes + 1);
        Integer iAnoReferencia = ((sMes + 1) > 12) ? (sAno + 1) : sAno;

        var idDespesaTemp = repository.getMaxIdDespesaTemp(idFuncionario);
        idDespesaReferencia = isNull(idDespesaTemp) ? repository.getMaxIdDespesa(idFuncionario) : idDespesaTemp;
        idDespesaReferencia++;

        log.info("Gravando Despesa Fixa Temporaria >>> idDespesaTemp: {} - mesRef: {} - anoRef: {}", idDespesaReferencia, iMesReferencia, iAnoReferencia);
        repository.insertDespesaFixaTemp(idDespesaReferencia, iMesReferencia, iAnoReferencia, idFuncionario);

        bProcessamentoTemporario = true;
        this.processarImportacaoDespesasMensais(idDespesaReferencia, idFuncionario, parserMesToString(iMesReferencia), iAnoReferencia.toString());
        bProcessamentoTemporario = false;

        log.info("Importacao das despesas temporarias realizada com sucesso, setando flag Temporario nas Despesas Mensais e Detalhes");
        repository.updateDespesasMensaisTipoTemporario(idDespesaReferencia, idFuncionario);
        repository.updateDetalheDespesasMensaisTipoTemporario(idDespesaReferencia, idFuncionario);

        return DespesaFixaTemporariaResponse.builder()
                .idDespesaTemp(idDespesaReferencia)
                .dsMesAnoTemp(parserMesToString(iMesReferencia).concat("/").concat(iAnoReferencia.toString()))
                .build();
    }

    private List<DespesasFixasMensaisRequest> processarDespesasFixasMensais(Integer idDespesa, Integer idFuncionario, String dsMes, String dsAno) throws Exception {
        List<DespesasFixasMensaisRequest> despesasFixasMensaisList = new ArrayList<>();
        var dsMesProcessamento = retornaMesAnterior(dsMes);
        var dsMesAnoProcessamento = (parseInt(dsMes) == 1 ? parseInt(dsAno) - 1 : dsAno).toString();

        for (DespesasFixasMensaisDAO dao : repository.getDespesasFixasMensais(dsMesProcessamento, dsMesAnoProcessamento, idFuncionario)) {
            var fixasMensais = DespesasFixasMensaisRequest.builder()
                    .idDespesa(idDespesa == 0 ? (dao.getIdDespesa() + 1) : idDespesa)
                    .dsDescricao(dao.getDsDescricao())
                    .vlTotal(dao.getVlTotal())
                    .tpStatus(dao.getTpStatus())
                    .dsMes(dsMes)
                    .dsAno(dsAno)
                    .idFuncionario(idFuncionario)
                    .idOrdem(dao.getIdOrdem())
                    .tpFixasObrigatorias(dao.getTpFixasObrigatorias())
                    .tpDespesaDebitoCartao(dao.getTpDespesaDebitoCartao())
                    .build();

            despesasFixasMensaisList.add(fixasMensais);
        }

        if (despesasFixasMensaisList.isEmpty()) {
            throw new ErroNegocioException("Não há lançamentos mensais no mês anterior para processamento.");
        }
        return despesasFixasMensaisList;
    }

    private List<DespesasMensaisDAO> processarDespesasMensais(Integer idDespesa, Integer idFuncionario) {
        var idDespesaAnterior = (idDespesa - 1);
        List<DespesasMensaisDAO> despesasMensaisList = new ArrayList<>();

        for (DespesasMensaisDAO dao : repository.getDespesasMensais(idDespesaAnterior, idFuncionario, null)) {
            dao.setIdDespesa(idDespesa);
            if (dao.getTpLinhaSeparacao().equalsIgnoreCase("S")) {
                dao.setTpReprocessar("N");
            }
            despesasMensaisList.add(dao);
        }

        return despesasMensaisList;
    }

    private List<DetalheDespesasMensaisDAO> processarDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String dsMes, String dsAno, Boolean bReprocessarTodosValores) {
        var idDespesaAnterior = (idDespesa - 1);
        List<DetalheDespesasMensaisDAO> detalheDespesasList = new ArrayList<>();
        for (DetalheDespesasMensaisDAO dao : repository.getDetalheDespesasMensais(idDespesaAnterior, idDetalheDespesa, idFuncionario, "a.id_Ordem")) {
            var idDespesaParcelada = dao.getIdDespesaParcelada();

            if (isNotNull(idDespesaParcelada) && idDespesaParcelada > 0) {
                var isDespesaBaixada = repository.getStatusDespesaParcelada(idDespesaParcelada, idFuncionario).equalsIgnoreCase("S");
                if (isDespesaBaixada) {
                    log.info("Despesa parcelada baixada, não será adicionada a despesa mensal..");
                    continue;
                }

                var dataVencimento = (dsMes + "/" + dsAno);
                ParcelasDAO parcela = repository.getParcelaPorDataVencimento(idDespesaParcelada, dataVencimento, idFuncionario);
                if (isNull(parcela)) {
                    //Em caso de reprocessamento, exclui a parcela da despesa se nao existir no fluxo de parcelas
                    repository.deleteDespesaParceladaImportada(idDespesa, idDetalheDespesa, idDespesaParcelada, idFuncionario);
                    continue;
                }

                var isParcelaAmortizada = parcela.getTpParcelaAmortizada().equalsIgnoreCase("S");
                var isDespesaComParcelaAmortizada = dao.getTpParcelaAmortizada().equalsIgnoreCase("S");

                if (isParcelaAmortizada) {
                    var parcelaSemAmortizacao = repository.getParcelaDisponivelSemAmortizacao(idDespesaParcelada, idFuncionario);

                    if (isNull(parcelaSemAmortizacao) || !isDespesaComParcelaAmortizada) {
                        // Ocorre quando é uma despesa com parcelas amortizadas onde a despesa foi quitada totalmente
                        continue;
                    } else {
                        var parcelasAmortizadasDespesaAtualStream = repository.getDespesasParceladasDetalheDespesasMensais(idDespesa, idFuncionario).stream()
                                .filter(d -> d.getTpParcelaAmortizada().equalsIgnoreCase("S"))
                                .collect(Collectors.toList());

                        if (parcelasAmortizadasDespesaAtualStream.size() == 0) {
                            log.info("Adicionando parcela amortizada na despesa mensal... Parcela: {}.", parcelaSemAmortizacao.getIdParcela());
                            parcelaSemAmortizacao.setTpParcelaAmortizada("S");
                            parcela = parcelaSemAmortizacao;

                            log.info("Atualizando status amortizacao da parcela com sucesso");
                            repository.updateParcelaStatusAmortizado(idDespesaParcelada, parcela.getIdParcela(), idFuncionario);
                        } else {
                            var parcelaDespesaAtual = parcelasAmortizadasDespesaAtualStream.stream()
                                    .collect(Collectors.maxBy(Comparator.comparingInt(DetalheDespesasMensaisDAO::getIdParcela)))
                                    .get().getIdParcela();

                            if (parcelaSemAmortizacao.getIdParcela() >= parcelaDespesaAtual) {
                                continue;
                            }

                            //Exclui a parcela atual da despesa para gravar novamente.
                            repository.deleteDespesaParceladaAmortizadaImportada(idDespesa, idDetalheDespesa, idDespesaParcelada, parcelaDespesaAtual, idFuncionario);

                            log.info("Atualizando status amortizacao da parcela com sucesso");
                            repository.updateParcelaStatusAmortizado(idDespesaParcelada, parcelaDespesaAtual, idFuncionario);

                            parcelaSemAmortizacao.setTpParcelaAmortizada("S");
                            parcela = parcelaSemAmortizacao;
                        }
                    }
                }

                dao.setDsTituloDespesa(DESCRICAO_DESPESA_PARCELADA);
                dao.setIdDespesaParcelada(parcela.getIdDespesaParcelada());
                dao.setIdParcela(parcela.getIdParcela());
                dao.setVlTotal(parcela.getVlParcela().trim());
                dao.setTpAnotacao(dao.getTpParcelaAdiada().equalsIgnoreCase("S") ? "N" : dao.getTpParcelaAdiada());
                dao.setTpParcelaAdiada(isEmpty(dao.getTpParcelaAdiada()) ? "N" : dao.getTpParcelaAdiada());
                dao.setTpParcelaAmortizada(isEmpty(parcela.getTpParcelaAmortizada()) ? "N" : parcela.getTpParcelaAmortizada());
            } else {
                dao.setIdDespesaParcelada(0);
                dao.setIdParcela(0);
                dao.setTpAnotacao("N");
                dao.setTpParcelaAdiada("N");

                if (bReprocessarTodosValores) {
                    dao.setVlTotal(dao.getVlTotal().trim());
                } else {
                    dao.setVlTotal(dao.getTpReprocessar().equalsIgnoreCase("N") ? VALOR_ZERO : dao.getVlTotal().trim());
                }
            }

            dao.setVlTotalPago(VALOR_ZERO);
            dao.setTpStatus(PENDENTE);
            dao.setDsObservacao("");
            dao.setDsObservacao2("");
            dao.setIdDespesa(idDespesa);
            dao.setIdObservacao(0);
            dao.setIdDetalheDespesaLog(0);
            dao.setIdConsolidacao(dao.getIdConsolidacao());
            dao.setIdDespesaConsolidacao(dao.getIdDespesaConsolidacao());

            detalheDespesasList.add(dao);
        }

        return detalheDespesasList;
    }

    private Boolean isDetalheDespesaExistente(DetalheDespesasMensaisDAO detalhe) {
        bDespesaComStatusPago = false;
        bDespesaComParcelaAdiada = false;

        var filtro = DetalheDespesasMensaisDAO.builder()
                .idDespesa(detalhe.getIdDespesa())
                .idDetalheDespesa(detalhe.getIdDetalheDespesa())
                .idDespesaParcelada(detalhe.getIdDespesaParcelada())
                .idParcela(detalhe.getIdParcela())
                .dsDescricao(detalhe.getDsDescricao())
                .idFuncionario(detalhe.getIdFuncionario())
                .idOrdem(detalhe.getTpLinhaSeparacao().equalsIgnoreCase("S") ? detalhe.getIdOrdem() : null)
                .tpLinhaSeparacao(detalhe.getTpLinhaSeparacao())
                .idConsolidacao(detalhe.getIdConsolidacao())
                .idDespesaConsolidacao(detalhe.getIdDespesaConsolidacao())
                .build();

        var detalheDespesasMensais = repository.getDetalheDespesaMensalPorFiltro(filtro);
        if (isNull(detalheDespesasMensais)) {
            /*Em caso de reprocessamento onde a despesa foi excluida anteriormente, valida se a parcela esta com a flag adiantada ativa*/
            var isParcelaAdiada = repository.getValidaParcelaAdiada(detalhe.getIdDespesaParcelada(), detalhe.getIdParcela(), detalhe.getIdFuncionario());
            if (isParcelaAdiada.equalsIgnoreCase("S")) {
                bDespesaComParcelaAdiada = true;
            }
            return false;
        }

        if (detalheDespesasMensais.getTpStatus().equalsIgnoreCase(PAGO) && detalheDespesasMensais.getTpLinhaSeparacao().equalsIgnoreCase("N")) {
            //No reprocessamento, se a despesa atual estiver com status pago, altera a flag para nao permitir update na despesa
            bDespesaComStatusPago = true;
        }

        if (detalheDespesasMensais.getTpParcelaAdiada().equalsIgnoreCase("S")) {
            bDespesaComParcelaAdiada = true;
        }

        return true;
    }

    public void processarImportacaoDespesaParcelada(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idConsolidacao, Integer idFuncionario) throws Exception {
        var despesaParcelada = repository.getDespesaParcelada(idDespesaParcelada, null, idFuncionario);
        if (isEmpty(despesaParcelada)) {
            throw new ErroNegocioException("Despesa parcelada não existente na base de dados, não será possivel realizar a importação.");
        }

        for (Integer idDespesaRef : repository.getIdDespesaProcessada(idDespesa, idFuncionario)) {
            var filtro = DetalheDespesasMensaisDAO.builder()
                    .idDespesa(idDespesaRef)
                    .idDetalheDespesa(idDetalheDespesa)
                    .idDespesaParcelada(idDespesaParcelada)
                    .idFuncionario(idFuncionario)
                    .build();

            var isDespesaJaImportada = repository.getDetalheDespesaMensalPorFiltro(filtro);

            if (isNull(isDespesaJaImportada)) {
                var request = DetalheDespesasMensaisDAO.builder()
                        .idDespesa(idDespesaRef)
                        .idDetalheDespesa(idDetalheDespesa)
                        .tpStatus(PENDENTE)
                        .idOrdem(null)
                        .idObservacao(0)
                        .idFuncionario(idFuncionario)
                        .idDespesaParcelada(despesaParcelada.getIdDespesaParcelada())
                        .idDespesaLinkRelatorio(0)
                        .idConsolidacao(0)
                        .idDespesaConsolidacao(0)
                        .tpAnotacao("N")
                        .tpReprocessar("N")
                        .tpMeta("N")
                        .tpRelatorio("N")
                        .tpParcelaAdiada("N")
                        .tpParcelaAmortizada("N")
                        .tpLinhaSeparacao("N")
                        .build();

                detalheDespesasServices.gravarDetalheDespesasMensais(request);

                log.info("Importacao realizada com sucesso >> idDespesa: {} - idDetalheDespesa: {}", idDespesaRef, idDetalheDespesa);
            } else {
                log.info("Despesa Parcelada ja importada na despesa anteriormente, importação não concluida!  >> idDespesa: {} - idDetalheDespesa: {}", idDespesaRef, idDetalheDespesa);
            }
        }
    }

    public void processarImportacaoConsolidacao(Integer idDespesa, Integer idDetalheDespesa, Integer idConsolidacao, Integer idFuncionario) throws Exception {
        var consolidacao = repository.getConsolidacao(idConsolidacao, idFuncionario);
        if (isEmpty(consolidacao)) {
            throw new ErroNegocioException("Consolidacao não existente na base de dados, não será possivel realizar a importação.");
        }

        for (Integer idDespesaRef : repository.getIdDespesaProcessada(idDespesa, idFuncionario)) {
            var filtro = DetalheDespesasMensaisDAO.builder()
                    .idDespesa(idDespesaRef)
                    .idDetalheDespesa(idDetalheDespesa)
                    .idConsolidacao(idConsolidacao)
                    .idFuncionario(idFuncionario)
                    .build();

            var isConsolidacaoJaImportada = repository.getDetalheDespesaMensalPorFiltro(filtro);

            if (isNull(isConsolidacaoJaImportada)) {
                var request = DetalheDespesasMensaisDAO.builder()
                        .idDespesa(idDespesaRef)
                        .idDetalheDespesa(idDetalheDespesa)
                        .dsTituloDespesa(consolidacao.getDsTituloConsolidacao())
                        .dsDescricao(DESCRICAO_DESPESA_CONSOLIDACAO)
                        .tpStatus(PENDENTE)
                        .idOrdem(null)
                        .idObservacao(0)
                        .idFuncionario(idFuncionario)
                        .idDespesaParcelada(0)
                        .idParcela(0)
                        .idConsolidacao(consolidacao.getIdConsolidacao())
                        .idDespesaConsolidacao(0)
                        .idDespesaLinkRelatorio(0)
                        .vlTotal(VALOR_ZERO)
                        .vlTotalPago(VALOR_ZERO)
                        .tpAnotacao("N")
                        .tpReprocessar("N")
                        .tpMeta("N")
                        .tpRelatorio("N")
                        .tpParcelaAdiada("N")
                        .tpParcelaAmortizada("N")
                        .tpLinhaSeparacao("N")
                        .build();

                detalheDespesasServices.gravarDetalheDespesasMensais(request);

                consolidacaoService.associarConsolidacaoDetalheDespesaMensal(idDespesaRef, idDetalheDespesa, idConsolidacao, idFuncionario);

                log.info("Consolidacao importada com sucesso >> idDespesa: {} - idDetalheDespesa: {} - idConsolidacao: {}", idDespesaRef, idDetalheDespesa, idConsolidacao);
            } else {
                log.info("Consolidacao importada na despesa anteriormente, importação não concluida!  >> idDespesa: {} - idDetalheDespesa: {} - idConsolidacao: {}", idDespesaRef, idDetalheDespesa, idConsolidacao);
            }
        }
    }

    public void incluirDespesaParceladaAmortizada(Integer idDespesa, Integer idDetalheDespesa, List<ParcelasDAO> parcelas, Integer idFuncionario) throws Exception {
        var despesaParcelada = repository.getDespesaParcelada(parcelas.get(0).getIdDespesaParcelada(), null, idFuncionario);
        if (isEmpty(despesaParcelada)) {
            throw new ErroNegocioException("Despesa parcelada não existente na base de dados, não será possivel gravar a parcela amortizada.");
        }

        for (ParcelasDAO parcela : parcelas) {
            var idParcela = parcela.getIdParcela();
            var idDespesaParcelada = parcela.getIdDespesaParcelada();

            log.info("Atualizando status amortizacao da parcela com sucesso");
            repository.updateParcelaStatusAmortizado(idDespesaParcelada, idParcela, idFuncionario);

            log.info("Incluindo despesa parcelada - idDespesaParcelada: {} - idParcela: {}.", idDespesaParcelada, idParcela);
            var request = DetalheDespesasMensaisDAO.builder()
                    .idDespesa(idDespesa)
                    .idDetalheDespesa(idDetalheDespesa)
                    .tpStatus(PENDENTE)
                    .idOrdem(null)
                    .idObservacao(0)
                    .idFuncionario(idFuncionario)
                    .dsObservacao("<AUT AMORTIZACAO - ".concat(dataHoraAtual()).concat(">"))
                    .idDespesaParcelada(idDespesaParcelada)
                    .idParcela(idParcela)
                    .idDespesaLinkRelatorio(0)
                    .idConsolidacao(0)
                    .idDespesaConsolidacao(0)
                    .tpAnotacao("N")
                    .tpReprocessar("N")
                    .tpMeta("N")
                    .tpRelatorio("N")
                    .tpParcelaAdiada("N")
                    .tpParcelaAmortizada("S")
                    .tpLinhaSeparacao("N")
                    .build();

            detalheDespesasServices.gravarDetalheDespesasMensais(request);
            log.info("Parcela amortizada incluida com sucesso.");
        }
    }
}
