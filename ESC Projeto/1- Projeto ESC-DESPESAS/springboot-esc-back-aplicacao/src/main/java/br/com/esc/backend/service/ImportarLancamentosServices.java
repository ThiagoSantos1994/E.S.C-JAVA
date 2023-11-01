package br.com.esc.backend.service;

import br.com.esc.backend.domain.*;
import br.com.esc.backend.repository.AplicacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.util.ArrayList;
import java.util.List;

import static br.com.esc.backend.utils.ObjectUtils.isNotNull;
import static br.com.esc.backend.utils.ObjectUtils.isNull;
import static br.com.esc.backend.utils.VariaveisGlobais.PENDENTE;
import static br.com.esc.backend.utils.VariaveisGlobais.VALOR_ZERO;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

@Slf4j
@RequiredArgsConstructor
public class ImportarLancamentosServices {

    private final AplicacaoRepository repository;
    private final DetalheDespesasServices detalheDespesasServices;

    public void processarImportacaoDespesasMensais(Integer idDespesa, Integer idFuncionario, String dsMes, String dsAno) throws Exception {
        /*Limpa a base dos dados temporarios gerados para visualizacao temporaria*/
        repository.deleteDespesasFixasMensaisTemp(idFuncionario);
        repository.deleteDespesasMensaisTemp(idFuncionario);
        repository.deleteDetalheDespesasMensaisTemp(idFuncionario);

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
                log.info("Atualizando detalhe despesa mensal >>>  {}", detalheDespesa);
                repository.updateDetalheDespesasMensaisImportacao(detalheDespesa);

                if (bReprocessarTodosValores) {
                    repository.updateValorDetalheDespesasMensais(detalheDespesa);
                }
            } else {
                log.info("Inserindo detalhe despesas mensais >>>  {}", detalheDespesa);
                repository.insertDetalheDespesasMensais(detalheDespesa);
            }
        }
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
        for (DetalheDespesasMensaisDAO dao : repository.getDetalheDespesasMensais(idDespesaAnterior, idDetalheDespesa, idFuncionario)) {
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

    private Boolean isDetalheDespesaExistente(DetalheDespesasMensaisDAO detalhe) {
        var filtro = DetalheDespesasMensaisDAO.builder()
                .idDespesa(detalhe.getIdDespesa())
                .idDetalheDespesa(detalhe.getIdDetalheDespesa())
                .idDespesaParcelada(detalhe.getIdDespesaParcelada())
                .dsDescricao(detalhe.getDsDescricao())
                .idFuncionario(detalhe.getIdFuncionario())
                .idOrdem(detalhe.getTpLinhaSeparacao().equalsIgnoreCase("N") ? null : detalhe.getIdOrdem())
                .tpReprocessar(detalhe.getTpReprocessar())
                .tpAnotacao(detalhe.getTpAnotacao())
                .tpRelatorio(detalhe.getTpRelatorio())
                .tpLinhaSeparacao(detalhe.getTpLinhaSeparacao())
                .build();

        var detalheDespesasMensais = repository.getDetalheDespesaMensalPorFiltro(filtro);
        if (isNull(detalheDespesasMensais)) {
            return false;
        }
        return true;
    }

}
