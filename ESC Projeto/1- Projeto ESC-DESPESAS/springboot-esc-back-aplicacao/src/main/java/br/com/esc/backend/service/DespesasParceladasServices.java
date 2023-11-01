package br.com.esc.backend.service;

import br.com.esc.backend.domain.DetalheDespesasMensaisDAO;
import br.com.esc.backend.repository.AplicacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import static br.com.esc.backend.utils.ObjectUtils.isNull;
import static br.com.esc.backend.utils.VariaveisGlobais.*;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@RequiredArgsConstructor
public class DespesasParceladasServices {

    private final AplicacaoRepository repository;

    public void isDespesaParceladaExcluida(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idFuncionario) {
        if (isNull(idOrdem) || idOrdem == -1) {
            /*Regra especifica para exclusao de todas as despesas parceladas da despesa com idOrdem = -1 ou Nulo*/
            for (DetalheDespesasMensaisDAO detalhe : repository.getDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario)) {
                if (detalhe.getTpLinhaSeparacao().equalsIgnoreCase("N") && detalhe.getIdDespesaParcelada().intValue() > 0) {
                    this.atualizarStatusPagamentoDespesaParcelada(detalhe.getIdDespesa(), detalhe.getIdDetalheDespesa(), detalhe.getIdDespesaParcelada(), detalhe.getIdParcela(), detalhe.getIdFuncionario(), detalhe.getTpStatus(), true);
                }
            }
            return;
        }

        var filtro = DetalheDespesasMensaisDAO.builder()
                .idDespesa(idDespesa)
                .idDetalheDespesa(idDetalheDespesa)
                .idFuncionario(idFuncionario)
                .idOrdem(idOrdem)
                .build();

        var detalhe = repository.getDetalheDespesaMensalPorFiltro(filtro);
        if (!isEmpty(detalhe) && detalhe.getTpLinhaSeparacao().equalsIgnoreCase("N") && detalhe.getIdDespesaParcelada().intValue() > 0) {
            this.atualizarStatusPagamentoDespesaParcelada(detalhe.getIdDespesa(), detalhe.getIdDetalheDespesa(), detalhe.getIdDespesaParcelada(), detalhe.getIdParcela(), detalhe.getIdFuncionario(), detalhe.getTpStatus(), true);
        }
    }

    public void atualizarStatusPagamentoDespesaParcelada(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idParcela, Integer idFuncionario, String statusParcela, Boolean bExcluirDespesa) {
        if (isNull(idDespesa) || idDespesa.equals(0) || idParcela.equals(0)) {
            return;
        }

        var isParcelaComAmortizacao = repository.getValidaParcelaAmortizacao(idDespesaParcelada, idParcela, idFuncionario);
        if (bExcluirDespesa.equals(true)) {
            if (isParcelaComAmortizacao.equalsIgnoreCase("S")) {
                repository.updateStatusParcelaSemAmortizacao(idDespesaParcelada, idParcela, idFuncionario);
            }
            statusParcela = PENDENTE;
        }

        if (statusParcela.equalsIgnoreCase(PENDENTE)) {
            repository.updateParcelaStatusPendente(idDespesa, idDetalheDespesa, idDespesaParcelada, idParcela, idFuncionario);
        } else {
            if (isParcelaComAmortizacao.equalsIgnoreCase("S")) {
                repository.updateStatusParcelaPaga(STATUS_BAIXA_AMORTIZADA_PELO_SISTEMA.concat(" #" + idParcela + "#"), idDespesa, idDetalheDespesa, idDespesaParcelada, idParcela, idFuncionario);
            } else {
                repository.updateStatusParcelaPaga(STATUS_BAIXA_REALIZADA_PELO_SISTEMA.concat(" #" + idParcela + "#"), idDespesa, idDetalheDespesa, idDespesaParcelada, idParcela, idFuncionario);
            }
        }

        validarStatusDespesaParcelada(idDespesaParcelada, idFuncionario);
    }

    public void validarStatusDespesaParcelada(Integer idDespesaParcelada, Integer idFuncionario) {
        var qtdeParcelas = repository.getQuantidadeParcelasEmAberto(idDespesaParcelada, idFuncionario);

        if (qtdeParcelas.intValue() <= 0) {
            repository.updateDespesasParceladasEncerrado(idDespesaParcelada, idFuncionario);
        }

        repository.updateDespesasParceladasEmAberto(idFuncionario);
    }

}
