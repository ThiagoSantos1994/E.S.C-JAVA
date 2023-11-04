package br.com.esc.backend.service;

import br.com.esc.backend.domain.*;
import br.com.esc.backend.repository.AplicacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import static br.com.esc.backend.utils.MotorCalculoUtils.convertDecimalToString;
import static br.com.esc.backend.utils.ObjectUtils.isNotNull;
import static br.com.esc.backend.utils.ObjectUtils.isNull;
import static br.com.esc.backend.utils.VariaveisGlobais.*;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@RequiredArgsConstructor
public class DetalheDespesasServices {

    private final AplicacaoRepository repository;
    private final DespesasParceladasServices despesasParceladasServices;

    public DetalheDespesasMensaisDTO obterDetalheDespesaMensal(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario) {
        var despesaMensal = repository.getDespesasMensais(idDespesa, idFuncionario, idDetalheDespesa);

        if (despesaMensal.size() <= 0) {
            return new DetalheDespesasMensaisDTO();
        }

        var detalheDespesasMensaisList = repository.getDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario);
        return DetalheDespesasMensaisDTO.builder()
                .sizeDetalheDespesaMensalVB(detalheDespesasMensaisList.size())
                .despesaMensal(repository.getDespesasMensais(idDespesa, idFuncionario, idDetalheDespesa).get(0))
                .detalheDespesaMensal(detalheDespesasMensaisList)
                .build();
    }

    public void gravarDetalheDespesasMensais(DetalheDespesasMensaisDAO detalheDAO) throws Exception {
        if (detalheDAO.getTpRelatorio().equalsIgnoreCase("S")) {
            // Despesas do tipo relatorio nao permite a gravacao\atualizacao na base de dados
            return;
        }

        /*Valida se a despesa é do tipo parcelada, se sim, busca o valor total a pagar da tabela de parcelas*/
        if (detalheDAO.getIdDespesaParcelada() > 0) {
            var referencia = this.obterReferenciaMesProcessamento(detalheDAO.getIdDespesa(), detalheDAO.getIdFuncionario());
            var dataVencimento = (referencia.getDsMes() + "/" + referencia.getDsAno());
            ParcelasDAO parcela = repository.getParcelaPorDataVencimento(detalheDAO.getIdDespesaParcelada(), dataVencimento, detalheDAO.getIdFuncionario());
            detalheDAO.setVlTotal(parcela.getVlParcela());
            detalheDAO.setDsDescricao(DESCRICAO_DESPESA_PARCELADA);
        }

        if (detalheDAO.getTpStatus().equalsIgnoreCase(PENDENTE)) {
            detalheDAO.setVlTotalPago(VALOR_ZERO);
        }

        if (isNotNull(detalheDAO.getIdOrdem()) && this.isDetalheDespesaExistente(detalheDAO)) {
            log.info("Atualizando DetalheDespesaMensal: request = {}", detalheDAO);
            repository.updateDetalheDespesasMensais(detalheDAO);
            despesasParceladasServices.validaStatusDespesaParcelada(detalheDAO.getIdDespesa(), detalheDAO.getIdDetalheDespesa(), detalheDAO.getIdDespesaParcelada(), detalheDAO.getIdParcela(), detalheDAO.getIdFuncionario(), detalheDAO.getTpStatus(), false);
        } else {
            var idOrdemInsert = detalheDAO.getTpRelatorio().equalsIgnoreCase("N") ?
                    repository.getMaxOrdemDetalheDespesasMensais(detalheDAO.getIdDespesa(), detalheDAO.getIdDetalheDespesa(), detalheDAO.getIdFuncionario()) :
                    repository.getMaxOrdemDetalheDespesasTipoRelatorio(detalheDAO.getIdDespesa(), detalheDAO.getIdFuncionario());

            detalheDAO.setIdOrdem(idOrdemInsert);

            log.info("Inserindo DetalheDespesaMensal: request = {}", detalheDAO);
            repository.insertDetalheDespesasMensais(detalheDAO);
        }
    }

    public void gravarDespesasMensais(DespesasMensaisDAO mensaisDAO) {
        if (mensaisDAO.getTpLinhaSeparacao().equalsIgnoreCase("N")) {
            if (mensaisDAO.getTpEmprestimo().equalsIgnoreCase("S") && mensaisDAO.getIdEmprestimo().intValue() > 0) {
                var idEmprestimo = repository.getCodigoEmprestimo(mensaisDAO.getDsTituloDespesa(), mensaisDAO.getIdFuncionario());
                if (idEmprestimo > 0) {
                    mensaisDAO.setDsNomeDespesa(DESCRICAO_DESPESA_EMPRESTIMO);
                    mensaisDAO.setVlLimite(repository.getCalculoTotalDespesa(mensaisDAO.getIdDespesa(), mensaisDAO.getIdDetalheDespesa(), mensaisDAO.getIdFuncionario()).toString());
                    mensaisDAO.setTpReferenciaSaldoMesAnterior("N");
                }
            }
        }

        if (this.isDespesaExistente(mensaisDAO)) {
            log.info("Atualizando despesa mensal >>>  {}", mensaisDAO);
            repository.updateDespesasMensais(mensaisDAO);
        } else {
            var idOrdemInsert = isNull(mensaisDAO.getIdOrdemExibicao()) ?
                    repository.getMaxOrdemDespesasMensais(mensaisDAO.getIdDespesa(), mensaisDAO.getIdFuncionario()) :
                    mensaisDAO.getIdOrdemExibicao();

            mensaisDAO.setIdOrdemExibicao(idOrdemInsert);

            log.info("Inserindo despesa mensal >>>  {}", mensaisDAO);
            repository.insertDespesasMensais(mensaisDAO);
        }

        this.validaDespesaTipoDebitoCartao(mensaisDAO, false);
    }

    public void deleteDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idFuncionario) throws Exception {
        despesasParceladasServices.isDespesaParceladaExcluida(idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
        this.validaDespesaTipoDebitoCartao(this.mensaisDAOMapper(idDespesa, idDetalheDespesa, idFuncionario), true);

        if (isNull(idOrdem) || idOrdem == -1) {
            repository.deleteDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario);
        } else {
            repository.deleteDetalheDespesasMensaisPorFiltro(idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
        }
    }

    public void validaDespesaTipoDebitoCartao(DespesasMensaisDAO mensaisDAO, Boolean bExcluirDespesa) {
        if (isEmpty(mensaisDAO) || mensaisDAO.getTpLinhaSeparacao().equalsIgnoreCase("S")) {
            return;
        }

        var despesaTipoDebtCart = repository.getDespesasFixasTipoDebitoCartao(mensaisDAO.getIdDespesa(), mensaisDAO.getIdFuncionario());

        if (mensaisDAO.getTpDebitoCartao().equalsIgnoreCase("S")) {
            if (bExcluirDespesa) {
                repository.deleteDespesasFixasMensaisPorFiltro(despesaTipoDebtCart.getIdDespesa(), despesaTipoDebtCart.getIdOrdem(), despesaTipoDebtCart.getIdFuncionario());
                return;
            }

            var referencia = this.obterReferenciaMesProcessamento(mensaisDAO.getIdDespesa(), mensaisDAO.getIdFuncionario());

            var despesaFixa = DespesasFixasMensaisRequest.builder()
                    .idDespesa(referencia.getIdDespesa())
                    .dsDescricao(TIPO_RESERVA_AUTOMATICA_DEBITO_CARTAO)
                    .vlTotal(mensaisDAO.getTpAnotacao().equalsIgnoreCase("S") ? "0,00" :
                            convertDecimalToString(repository.getCalculoValorDespesaTipoCartaoDebito(referencia.getIdDespesa(), mensaisDAO.getIdDetalheDespesa(), referencia.getIdFuncionario())))
                    .tpStatus("+")
                    .tpFixasObrigatorias("N")
                    .dsMes(referencia.getDsMes())
                    .dsAno(referencia.getDsAno())
                    .idFuncionario(referencia.getIdFuncionario())
                    .idOrdem(isEmpty(despesaTipoDebtCart) ? 0 : despesaTipoDebtCart.getIdOrdem())
                    .tpDespesaDebitoCartao("S")
                    .build();

            if (isNull(despesaTipoDebtCart)) {
                var idOrdemInsert = repository.getMaxOrdemDespesasFixasMensais(mensaisDAO.getIdDespesa(), mensaisDAO.getIdFuncionario());
                despesaFixa.setIdOrdem(idOrdemInsert);
                repository.insertDespesasFixasMensais(despesaFixa);
            } else {
                repository.updateDespesasFixasMensais(despesaFixa);
            }
        }
    }

    public void baixarPagamentoDespesas(PagamentoDespesasRequest request) throws Exception {
        if (request.getTpStatus().equalsIgnoreCase(PENDENTE)) {
            var bProcessamentoAdiantamentoParcelas = request.getIsProcessamentoAdiantamentoParcelas();

            if ((bProcessamentoAdiantamentoParcelas.equals(true) && request.getIdDetalheDespesa() > 0) || bProcessamentoAdiantamentoParcelas.equals(false)) {
                var existeParcelasAdiadas = repository.getValidaDetalheDespesaComParcelaAdiada(request.getIdDespesa(), request.getIdDetalheDespesa(), request.getIdFuncionario());
                if (existeParcelasAdiadas.equalsIgnoreCase("N")) {
                    repository.updateStatusPagamentoDetalheDespesa(request.getVlTotal(), request.getVlTotalPago(), PAGO, request.getDsObservacoes(), request.getDsObservacoesComplementar(), request.getIdDespesa(), request.getIdDetalheDespesa(), request.getIdOrdem(), request.getIdFuncionario());
                }
            }

            if (bProcessamentoAdiantamentoParcelas.equals(false)) {
                despesasParceladasServices.validaStatusDespesaParcelada(request.getIdDespesa(), request.getIdDetalheDespesa(), request.getIdDespesaParcelada(), request.getIdParcela(), request.getIdFuncionario(), PAGO, false);
            }
        }

        /*Atualiza o stts pagamento para as linhas de separacao*/
        repository.updateStatusBaixaLinhaSeparacao(request.getIdDespesa(), request.getIdFuncionario());
    }

    private Boolean isDetalheDespesaExistente(DetalheDespesasMensaisDAO detalhe) {
        var filtro = DetalheDespesasMensaisDAO.builder()
                .idDespesa(detalhe.getIdDespesa())
                .idDetalheDespesa(detalhe.getIdDetalheDespesa())
                .idFuncionario(detalhe.getIdFuncionario())
                .idOrdem(detalhe.getTpLinhaSeparacao().equalsIgnoreCase("N") ? null : detalhe.getIdOrdem())
                .build();

        var detalheDespesasMensais = repository.getDetalheDespesaMensalPorFiltro(filtro);
        if (isNull(detalheDespesasMensais)) {
            return false;
        }
        return true;
    }

    private Boolean isDespesaExistente(DespesasMensaisDAO despesa) {
        var filtro = DespesasMensaisDAO.builder()
                .idDespesa(despesa.getIdDespesa())
                .idDetalheDespesa(despesa.getIdDetalheDespesa())
                .idFuncionario(despesa.getIdFuncionario())
                .build();

        var despesaMensal = repository.getDespesaMensalPorFiltro(despesa.getIdDespesa(), despesa.getIdDetalheDespesa(), despesa.getIdFuncionario());
        if (isNull(despesaMensal)) {
            return false;
        }
        return true;
    }

    private DespesasMensaisDAO mensaisDAOMapper(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario) {
        var detalheDespesaMensal = this.obterDetalheDespesaMensal(idDespesa, idDetalheDespesa, idFuncionario);
        if (isEmpty(detalheDespesaMensal.getDespesaMensal())) {
            return null;
        }

        var mensaisDAO = DespesasMensaisDAO.builder()
                .idDespesa(idDespesa)
                .idDetalheDespesa(idDetalheDespesa)
                .idFuncionario(idFuncionario)
                .tpLinhaSeparacao(isEmpty(detalheDespesaMensal.getDespesaMensal().getTpLinhaSeparacao()) ? null : detalheDespesaMensal.getDespesaMensal().getTpLinhaSeparacao())
                .tpDebitoCartao(detalheDespesaMensal.getDespesaMensal().getTpDebitoCartao())
                .tpAnotacao(isEmpty(detalheDespesaMensal.getDespesaMensal().getTpAnotacao()) ? "N" : detalheDespesaMensal.getDespesaMensal().getTpAnotacao())
                .build();

        return mensaisDAO;
    }

    private DespesasFixasMensaisDAO obterReferenciaMesProcessamento(Integer idDespesa, Integer idFuncionario) {
        return repository.getDespesaFixaMensalPorFiltro(idDespesa, 1, idFuncionario);
    }
}
