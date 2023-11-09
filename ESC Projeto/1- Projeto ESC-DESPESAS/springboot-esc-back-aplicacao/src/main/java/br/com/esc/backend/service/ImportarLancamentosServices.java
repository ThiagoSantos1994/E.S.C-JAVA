package br.com.esc.backend.service;

import br.com.esc.backend.domain.*;
import br.com.esc.backend.repository.AplicacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static br.com.esc.backend.utils.DataUtils.DataHoraAtual;
import static br.com.esc.backend.utils.ObjectUtils.isNotNull;
import static br.com.esc.backend.utils.ObjectUtils.isNull;
import static br.com.esc.backend.utils.VariaveisGlobais.*;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportarLancamentosServices {

    private final AplicacaoRepository repository;
    private final DetalheDespesasServices detalheDespesasServices;
    private boolean bDespesaComStatusPago;
    private boolean bDespesaComParcelaAdiantada;
    private boolean bProcessamentoTemporario;

    public void processarImportacaoDespesasMensais(Integer idDespesa, Integer idFuncionario, String dsMes, String dsAno) throws Exception {
        if (bProcessamentoTemporario == false) {
            /*Limpa a base dos dados temporarios gerados para visualizacao temporaria*/
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

        var despesasMensais = this.processarDespesasMensais(idDespesaImportacao, idFuncionario);
        for (DespesasMensaisDAO despesaMensal : despesasMensais) {
            detalheDespesasServices.gravarDespesasMensais(despesaMensal);

            var bReprocessarTodosValores = despesaMensal.getTpReprocessar().equalsIgnoreCase("S");
            this.processarImportacaoDetalheDespesasMensais(idDespesaImportacao, despesaMensal.getIdDetalheDespesa(), idFuncionario, dsMes, dsAno, bReprocessarTodosValores);
        }
    }

    public void processarImportacaoDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String dsMes, String dsAno, Boolean bReprocessarTodosValores) {
        for (DetalheDespesasMensaisDAO detalheDespesa : this.processarDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario, dsMes, dsAno, bReprocessarTodosValores)) {
            if (this.isDetalheDespesaExistente(detalheDespesa)) {
                if (bDespesaComStatusPago == true) {
                    log.info("Despesa mensal com status PAGO, nao foi atualizado. >>>  {}", detalheDespesa);
                    continue;
                }
                if (bDespesaComParcelaAdiantada == true) {
                    log.info("Despesa mensal com status adiantamento de parcela, nao foi atualizado. >>>  {}", detalheDespesa);
                    continue;
                }

                log.info("Atualizando detalhe despesa mensal >>>  {}", detalheDespesa);
                repository.updateDetalheDespesasMensaisImportacao(detalheDespesa);

                if (bReprocessarTodosValores) {
                    repository.updateValorDetalheDespesasMensais(detalheDespesa);
                }
            } else {
                log.info("Inserindo detalhe despesas mensais >>>  {}", detalheDespesa);
                repository.insertDetalheDespesasMensais(detalheDespesa);

                if (bDespesaComParcelaAdiantada == true) {
                    log.info("Despesa mensal com status adiantamento de parcela, realizando tratamento para gravar. >>>  {}", detalheDespesa);

                    /*Altera a flag de ParcelaAdiada no detalhe das despesas mensais e baixa o pagamento e marca como despesa de anotacao*/
                    var logProcessamento = "Operacao realizada em: " + DataHoraAtual() + " - Usuario: ** " + repository.getUsuarioLogado(idFuncionario);
                    repository.updateDetalheDespesasMensaisParcelaAdiada(idDespesa, idDetalheDespesa, detalheDespesa.getIdDespesaParcelada(), "Despesa adiantada no fluxo de adiantamento de parcelas.", logProcessamento, detalheDespesa.getVlTotal(), idFuncionario);
                    continue;
                }
            }
        }
    }

    public DespesaFixaTemporariaResponse gerarTemporariamenteDespesasMensais(Integer sMes, Integer sAno, Integer idFuncionario) throws Exception {
        Integer idDespesaReferencia;
        Integer iMesReferencia = ((sMes + 1) > 12) ? 1 : (sMes + 1);
        Integer iAnoReferencia = ((sMes + 1) > 12) ? sAno + 1 : sAno;

        var idDespesaTemp = repository.getMaxIdDespesaTemp(idFuncionario);
        idDespesaReferencia = isNull(idDespesaTemp) ? repository.getMaxIdDespesa(idFuncionario) : idDespesaTemp;
        idDespesaReferencia++;

        log.info("Gravando Despesa Fixa Temporaria >>> idDespesaTemp: {} - mesRef: {} - anoRef: {}", idDespesaReferencia, iMesReferencia, iAnoReferencia);
        repository.insertNovaDespesaFixaTemp(idDespesaReferencia, iMesReferencia, iAnoReferencia, idFuncionario);

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

        if (despesasFixasMensaisList.size() == 0) {
            throw new Exception("Nao ha lancamentos mensais no mes anterior para processamento.");
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
                var dataVencimento = (dsMes + "/" + dsAno);
                ParcelasDAO parcela = repository.getParcelaPorDataVencimento(idDespesaParcelada, dataVencimento, idFuncionario);

                if (isNull(parcela)) {
                    //Em caso de reprocessamento, exclui a parcela da despesa se nao existir no fluxo de parcelas
                    repository.deleteDespesaParceladaImportada(idDespesa, idDetalheDespesa, idDespesaParcelada, idFuncionario);
                    continue;
                }

                var isDetalheComParcelaAmortizada = repository.getValidaDetalheDespesaComParcelaAmortizada(dao.getIdDespesa(), dao.getIdDetalheDespesa(), idFuncionario);
                if (isDetalheComParcelaAmortizada.equalsIgnoreCase("S")) {
                    var parcelaSemAmortizacao = repository.getParcelaDisponivelSemAmortizacao(idDespesaParcelada, idFuncionario);

                    if (parcelaSemAmortizacao.getIdParcela() >= parcela.getIdParcela()) {
                        // Somente para parcelas amortizadas, exclui a despesa parcelada para gravar novamente
                        repository.deleteDespesaParceladaImportada(idDespesa, idDetalheDespesa, idDespesaParcelada, idFuncionario);
                        parcela = parcelaSemAmortizacao;
                    } else {
                        dao.setTpAnotacao("S");
                    }
                }

                if (dao.getTpParcelaAdiada().equalsIgnoreCase("S")) {
                    //Se a parcela anterior foi adiantada, retira a flag anotacao
                    dao.setTpAnotacao("N");
                }

                dao.setDsTituloDespesa(DESCRICAO_DESPESA_PARCELADA);
                dao.setIdDespesaParcelada(parcela.getIdDespesaParcelada());
                dao.setIdParcela(parcela.getIdParcela());
                dao.setVlTotal(parcela.getVlParcela().trim());
            } else {
                dao.setIdDespesaParcelada(0);
                dao.setIdParcela(0);
                dao.setTpAnotacao("N");

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

            detalheDespesasList.add(dao);
        }

        return detalheDespesasList;
    }

    private String retornaMesAnterior(String dsMes) {
        var dsMesAnterior = (parseInt(dsMes) - 1 < 1 ? 12 : parseInt(dsMes) - 1);
        var result = (dsMesAnterior <= 9 ? "0" + dsMesAnterior : valueOf(dsMesAnterior));
        return result;
    }

    private String parserMesToString(Integer dsMes) {
        return (dsMes <= 9 ? "0" + dsMes : valueOf(dsMes));
    }

    private Boolean isDetalheDespesaExistente(DetalheDespesasMensaisDAO detalhe) {
        bDespesaComStatusPago = false;
        bDespesaComParcelaAdiantada = false;

        var filtro = DetalheDespesasMensaisDAO.builder()
                .idDespesa(detalhe.getIdDespesa())
                .idDetalheDespesa(detalhe.getIdDetalheDespesa())
                .idDespesaParcelada(detalhe.getIdDespesaParcelada())
                .idParcela(detalhe.getIdParcela())
                .dsDescricao(detalhe.getDsDescricao())
                .idFuncionario(detalhe.getIdFuncionario())
                .idOrdem(detalhe.getTpLinhaSeparacao().equalsIgnoreCase("S") ? detalhe.getIdOrdem() : null)
                .tpLinhaSeparacao(detalhe.getTpLinhaSeparacao())
                .build();

        var detalheDespesasMensais = repository.getDetalheDespesaMensalPorFiltro(filtro);
        if (isNull(detalheDespesasMensais)) {
            /*Em caso de reprocessamento onde a despesa foi excluida anteriormente, valida se a parcela esta com a flag adiantada ativa*/
            var isParcelaAdiantada = repository.getValidaParcelaAdiantamento(detalhe.getIdDespesaParcelada(), detalhe.getIdParcela(), detalhe.getIdFuncionario());
            if (isParcelaAdiantada.equalsIgnoreCase("S")) {
                bDespesaComParcelaAdiantada = true;
            }
            return false;
        }

        if (detalheDespesasMensais.getTpStatus().equalsIgnoreCase(PAGO) && detalheDespesasMensais.getTpLinhaSeparacao().equalsIgnoreCase("N")) {
            //No reprocessamento, se a despesa atual estiver com status pago, altera a flag para nao permitir update na despesa
            bDespesaComStatusPago = true;
        }

        if (detalheDespesasMensais.getTpParcelaAdiada().equalsIgnoreCase("S")) {
            bDespesaComParcelaAdiantada = true;
        }

        return true;
    }

}
