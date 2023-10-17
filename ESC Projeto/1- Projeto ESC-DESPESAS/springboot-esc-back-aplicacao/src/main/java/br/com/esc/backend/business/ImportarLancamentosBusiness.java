package br.com.esc.backend.business;

import br.com.esc.backend.domain.*;
import br.com.esc.backend.repository.AplicacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.util.ArrayList;
import java.util.List;

import static br.com.esc.backend.utils.ObjectUtils.isNotNull;
import static br.com.esc.backend.utils.ObjectUtils.isNull;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

@Slf4j
@RequiredArgsConstructor
public class ImportarLancamentosBusiness {

    private final AplicacaoRepository repository;
    private final static String PENDENTE = "Pendente";
    private final static String PAGO = "Pago";
    private final static String VALOR_ZERO = "0,00";

    public List<DespesasFixasMensaisRequest> processarDespesasFixasMensais(Integer idDespesa, Integer idFuncionario, String dsMes, String dsAno) throws Exception {
        List<DespesasFixasMensaisRequest> despesasFixasMensaisList = new ArrayList<>();

        var idDespesaNova = (idDespesa == 0 ? -1 : idDespesa + 1);
        var dsMesProcessamento = retornaMesAnterior(dsMes);
        var dsMesAnoProcessamento = (parseInt(dsMesProcessamento) == 1 ? parseInt(dsAno) - 1 : dsAno).toString();

        for (DespesasFixasMensaisDAO dao : repository.getDespesasFixasMensais(dsMesProcessamento, dsMesAnoProcessamento, idFuncionario)) {
            var fixasMensais = DespesasFixasMensaisRequest.builder()
                    .idDespesa(idDespesaNova == -1 ? (dao.getIdDespesa() + 1) : idDespesaNova)
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

    public List<DespesasMensaisDAO> processarDespesasMensais(Integer idDespesa, Integer idFuncionario) {
        var idDespesaAnterior = (idDespesa - 1);
        List<DespesasMensaisDAO> despesasMensaisList = new ArrayList<>();

        for (DespesasMensaisDAO dao : repository.getDespesasMensais(idDespesaAnterior, idFuncionario)) {
            dao.setIdDespesa(idDespesa);
            despesasMensaisList.add(dao);
        }

        return despesasMensaisList;
    }

    public List<DetalheDespesasMensaisDAO> processarDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String dsMes, String dsAno) {
        var idDespesaAnterior = (idDespesa - 1);
        List<DetalheDespesasMensaisDAO> detalheDespesasList = new ArrayList<>();
        for (DetalheDespesasMensaisDAO dao : repository.getDetalheDespesasMensais(idDespesaAnterior, idDetalheDespesa, idFuncionario)) {
            var idDespesaParcelada = dao.getIdDespesaParcelada();
            if (isNotNull(idDespesaParcelada) && idDespesaParcelada > 0) {
                var dataVencimento = (dsMes + "/" + dsAno);
                ParcelasDAO parcela = repository.getParcelaPorDataVencimento(idDespesaParcelada, dataVencimento, idFuncionario);
                if (isNull(parcela)) {
                    continue;
                }

                var isDespesaComAmortizacao = repository.getValidaDespesaParceladaAmortizacao(idDespesaParcelada, idFuncionario);
                if (isDespesaComAmortizacao.equalsIgnoreCase("S")) {
                    //Nao importa despesas amortizadas automaticamente, o fluxo precisa ser manual
                    continue;
                }

                dao.setIdDespesaParcelada(parcela.getIdDespesaParcelada());
                dao.setIdParcela(parcela.getIdParcela());
                dao.setVlTotal(parcela.getVlParcela().trim());
            } else {
                dao.setVlTotal(dao.getTpReprocessar().equalsIgnoreCase("N") ? VALOR_ZERO : dao.getVlTotal().trim());
                dao.setTpAnotacao("N");
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

    /*public void reprocessarDespesasMensais(Integer idDespesaAtual, Integer idFuncionario) {
        var idDespesaAnterior = (idDespesaAtual - 1);

        for (DespesasMensaisDAO despesaMesAnterior : repository.getDespesasMensais(idDespesaAnterior, idFuncionario)) {
            var idDetalheDespesa = despesaMesAnterior.getIdDetalheDespesa();

            DespesasMensaisDAO despesaMesAtual = repository.getDespesasMensais(idDespesaAtual, idDetalheDespesa, idFuncionario);
            if (isNull(despesaMesAtual.getIdDetalheDespesa())) {
                var despesaInsert = despesaMesAnterior;
                despesaInsert.setIdDespesa(idDespesaAtual);

                log.info("Inserindo despesa - {}", despesaInsert);
                repository.insertDespesasMensais(despesaInsert);
            } else {
                var despesaUpdate = despesaMesAnterior;
                despesaUpdate.setIdDespesa(idDespesaAtual);

                log.info("Atualizando despesa - {}", despesaUpdate);
                repository.updateDespesasMensais(despesaUpdate);
            }
        }
    }
     */

    private String retornaMesAnterior(String dsMes) {
        var dsMesAnterior = (parseInt(dsMes) - 1 < 1 ? 12 : parseInt(dsMes) - 1);
        var result = (dsMesAnterior <= 9 ? "0" + dsMesAnterior : valueOf(dsMesAnterior));
        return result;
    }

}
