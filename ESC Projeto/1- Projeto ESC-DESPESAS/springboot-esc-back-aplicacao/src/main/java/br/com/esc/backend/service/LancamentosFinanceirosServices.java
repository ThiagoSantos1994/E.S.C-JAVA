package br.com.esc.backend.service;

import br.com.esc.backend.domain.DespesasFixasMensaisDAO;
import br.com.esc.backend.domain.DespesasFixasMensaisRequest;
import br.com.esc.backend.domain.LancamentosFinanceirosDTO;
import br.com.esc.backend.domain.LancamentosMensaisDAO;
import br.com.esc.backend.repository.AplicacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static br.com.esc.backend.utils.MotorCalculoUtils.*;
import static br.com.esc.backend.utils.ObjectUtils.isNull;

@Slf4j
@RequiredArgsConstructor
public class LancamentosFinanceirosServices {

    private final AplicacaoRepository repository;

    public LancamentosFinanceirosDTO obterLancamentosFinanceiros(String dsMes, String dsAno, Integer idFuncionario) {
        LancamentosFinanceirosDTO dto = new LancamentosFinanceirosDTO();

        List<DespesasFixasMensaisDAO> despesasFixasMensais = repository.getDespesasFixasMensais(dsMes, dsAno, idFuncionario);
        if (despesasFixasMensais.size() == 0) {
            return dto;
        }

        var idDespesa = despesasFixasMensais.get(0).getIdDespesa();
        var idDespesaAnterior = (idDespesa - 1);

        dto.setIdDespesa(idDespesa);
        dto.setDsMesReferencia(dsMes);
        dto.setDsAnoReferencia(dsAno);
        dto.setVlSaldoPositivo(calcularReceitaPositivaMes(repository.getCalculoReceitaPositivaMES(idDespesa, idFuncionario)));
        dto.setVlTotalDespesas(repository.getCalculoReceitaNegativaMES(idDespesa, idFuncionario));
        dto.setVlTotalPendentePagamento(repository.getCalculoReceitaPendentePgtoMES(idDespesa, idFuncionario));
        dto.setVlSaldoInicialMes(repository.getCalculoSaldoInicialMES(idDespesa, idFuncionario));
        dto.setPcUtilizacaoDespesasMes(new DecimalFormat("00").format(this.obterPercentualUtilizacaoDespesaMes(dto)).concat("%"));
        dto.setVlSaldoDisponivelMes(dto.getVlSaldoPositivo().subtract(dto.getVlTotalDespesas()));
        dto.setDespesasFixasMensais(despesasFixasMensais);
        dto.setLancamentosMensais(this.obterLancamentosMensais(idDespesa, idDespesaAnterior, idFuncionario));
        dto.setLabelQuitacaoParcelasMes(this.obterExtratoDespesasParceladasQuitacaoMes(idDespesa, idFuncionario));

        /*Especifico para aplicação VB6*/
        dto.setSizeDespesasFixasMensaisVB(despesasFixasMensais.size());
        dto.setSizeLancamentosMensaisVB(dto.getLancamentosMensais().size());

        return dto;
    }

    private String obterExtratoDespesasParceladasQuitacaoMes(Integer idDespesa, Integer idFuncionario) {
        var qtdeDespesasParceladasMes = repository.getQuantidadeDespesasParceladasMes(idDespesa, idFuncionario);
        var qtdeDespesasQuitacaoMes = repository.getQuantidadeDespesasParceladasQuitacaoMes(idDespesa, idFuncionario);

        StringBuffer buffer = new StringBuffer();

        buffer.append("Neste mês foi quitado ");
        buffer.append(qtdeDespesasQuitacaoMes.getQtdeParcelas() + "/" + qtdeDespesasParceladasMes);
        buffer.append(" Despesas Parceladas, Totalizando: " + qtdeDespesasQuitacaoMes.getVlParcelas() + "R$");

        return buffer.toString();
    }

    private List<LancamentosMensaisDAO> obterLancamentosMensais(Integer idDespesa, Integer idDespesaAnterior, Integer idFuncionario) {
        List<LancamentosMensaisDAO> lancamentosMensaisList = new ArrayList<>();

        for (LancamentosMensaisDAO detalhes : repository.getLancamentosMensais(idDespesa, idFuncionario)) {
            var idDetalheDespesa = detalhes.getIdDetalheDespesa();

            var bLinhaSeparacao = detalhes.getTpLinhaSeparacao();
            if (bLinhaSeparacao.equalsIgnoreCase("S")) {
                lancamentosMensaisList.add(detalhes);
                continue;
            }

            var bDespesaRelatorio = detalhes.getTpRelatorio();
            if (bDespesaRelatorio.equalsIgnoreCase("S")) {
                detalhes.setVlTotalDespesa(repository.getCalculoDespesaTipoRelatorio(idDespesa, idDetalheDespesa, idFuncionario));
            }

            var bRefValorDespesaMesAnterior = detalhes.getTpReferenciaSaldoMesAnterior();
            if (bRefValorDespesaMesAnterior.equalsIgnoreCase("S")) {
                var vlLimiteMesAnterior = repository.getCalculoTotalDespesa(idDespesaAnterior, idDetalheDespesa, idFuncionario);
                detalhes.setVlLimite(vlLimiteMesAnterior.toString());
            }

            detalhes.setVlTotalDespesaPendente(repository.getCalculoTotalDespesaPendente(idDespesa, idDetalheDespesa, idFuncionario));
            detalhes.setVlTotalDespesaPaga(repository.getCalculoTotalDespesaPaga(idDespesa, idDetalheDespesa, idFuncionario));

            if (detalhes.getTpEmprestimo().equalsIgnoreCase("N")) {
                var percentual = calculaPorcentagem(convertStringToDecimal(detalhes.getVlLimite()), detalhes.getVlTotalDespesa());
                detalhes.setPercentualUtilizacao(new DecimalFormat("0.00").format(percentual).concat("%"));
            }

            lancamentosMensaisList.add(detalhes);
        }

        return lancamentosMensaisList;
    }

    public void gravarDespesasFixasMensais(DespesasFixasMensaisRequest request) {
        if (this.isDespesaFixaExistente(request)) {
            log.info("Atualizando despesas fixas mensais - request: {}", request);
            repository.updateDespesasFixasMensais(request);
        } else {
            var idOrdemInclusao = repository.getMaxOrdemDespesasFixasMensais(request.getIdDespesa(), request.getIdFuncionario());
            request.setIdOrdem(idOrdemInclusao);

            log.info("Inserindo despesas fixas mensais - request: {}", request);
            repository.insertDespesasFixasMensais(request);
        }
    }

    private BigDecimal obterPercentualUtilizacaoDespesaMes(LancamentosFinanceirosDTO dto) {
        var iPercentualUtilizadoBase = calculaPorcentagem(dto.getVlSaldoPositivo(), (dto.getVlSaldoInicialMes().subtract(dto.getVlSaldoPositivo())), 2);
        var iPercentualDespesaMesCalculado = calculaPorcentagem(dto.getVlSaldoInicialMes(), (dto.getVlTotalDespesas().subtract(dto.getVlSaldoInicialMes())), 2).add(iPercentualUtilizadoBase);

        return iPercentualDespesaMesCalculado;
    }

    private Boolean isDespesaFixaExistente(DespesasFixasMensaisRequest request) {
        var despesaFixaMensal = repository.getDespesaFixaMensalPorFiltro(request.getIdDespesa(), request.getIdOrdem(), request.getIdFuncionario());
        if (isNull(despesaFixaMensal)) {
            return false;
        }
        return true;
    }
}
