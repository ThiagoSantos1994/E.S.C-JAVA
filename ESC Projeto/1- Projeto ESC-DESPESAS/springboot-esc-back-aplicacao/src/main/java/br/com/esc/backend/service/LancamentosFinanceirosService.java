package br.com.esc.backend.service;

import br.com.esc.backend.domain.DespesasFixasMensaisDAO;
import br.com.esc.backend.domain.DespesasMensaisDAO;
import br.com.esc.backend.domain.LancamentosFinanceirosDTO;
import br.com.esc.backend.repository.AplicacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static br.com.esc.backend.utils.DataUtils.MesAtual;
import static br.com.esc.backend.utils.MotorCalculoUtils.calculaPorcentagem;
import static br.com.esc.backend.utils.MotorCalculoUtils.calcularReceitaPositivaMes;
import static java.lang.String.valueOf;

@RequiredArgsConstructor
@Slf4j
public class LancamentosFinanceirosService {

    private final AplicacaoRepository repository;

    public LancamentosFinanceirosDTO obterLancamentosFinanceiros(String dsMes, String dsAno, Integer idFuncionario) {
        LancamentosFinanceirosDTO dto = new LancamentosFinanceirosDTO();

        List<DespesasFixasMensaisDAO> despesasFixasMensais = repository.obterDespesasFixasMensais(dsMes, dsAno, idFuncionario);
        if (ObjectUtils.isEmpty(despesasFixasMensais)) {
            return dto;
        }

        var idDespesa = despesasFixasMensais.get(0).getId_Despesa();
        var idDespesaAnterior = (idDespesa - 1);

        dto.setIdDespesa(idDespesa);
        dto.setDsMesReferencia(dsMes);
        dto.setDsAnoReferencia(dsAno);
        dto.setVlSaldoPositivo(calcularReceitaPositivaMes(repository.obterCalculoReceitaPositivaMES(idDespesa, idFuncionario)));
        dto.setVlTotalDespesas(repository.obterCalculoReceitaNegativaMES(idDespesa, idFuncionario));
        dto.setVlTotalPendentePagamento(repository.obterCalculoReceitaPendentePgtoMES(idDespesa, idFuncionario));
        dto.setVlSaldoInicialMes(repository.obterCalculoSaldoInicialMES(idDespesa, idFuncionario));
        dto.setPcUtilizacaoDespesasMes(valueOf(obterPercentualUtilizacaoDespesaMes(dto)).concat("%"));
        dto.setVlSaldoDisponivelMes(dto.getVlSaldoPositivo().subtract(dto.getVlTotalDespesas()));
        dto.setDespesasFixasMensais(despesasFixasMensais);
        dto.setDetalheDespesasMensais(obterDespesasMensais(idDespesa, idDespesaAnterior, idFuncionario));
        dto.setLabelQuitacaoParcelasMes(obterExtratoDespesasParceladasQuitacaoMes(idDespesa, idFuncionario, dsMes));

        /*Especifico para aplicação VB6*/
        dto.setSizeDespesasFixasMensaisVB(despesasFixasMensais.size());
        dto.setSizeDetalheDespesasMensaisVB(dto.getDetalheDespesasMensais().size());
        return dto;
    }

    private List<DespesasMensaisDAO> obterDespesasMensais(Integer idDespesa, Integer idDespesaAnterior, Integer idFuncionario) {
        List<DespesasMensaisDAO> listDetalheDespesas = new ArrayList<>();

        for (DespesasMensaisDAO detalhes : repository.obterDespesasMensais(idDespesa, idFuncionario)) {
            var idDetalheDespesa = detalhes.getId_DetalheDespesa();

            var bLinhaSeparacao = detalhes.getTp_LinhaSeparacao();
            if (bLinhaSeparacao.equalsIgnoreCase("S")) {
                listDetalheDespesas.add(detalhes);
                continue;
            }

            var bDespesaRelatorio = detalhes.getTp_Relatorio();
            if (bDespesaRelatorio.equalsIgnoreCase("S")) {
                detalhes.setVl_TotalDespesa(repository.obterCalculoDespesaTipoRelatorio(idDespesa, idDetalheDespesa, idFuncionario));
            }

            var bRefValorDespesaMesAnterior = detalhes.getTp_ReferenciaSaldoMesAnterior();
            if (bRefValorDespesaMesAnterior.equalsIgnoreCase("S")) {
                var vlLimiteMesAnterior = repository.obterCalculoTotalDespesa(idDespesaAnterior, idDetalheDespesa, idFuncionario);
                detalhes.setVl_Limite(vlLimiteMesAnterior.toString());
            }

            detalhes.setVl_TotalDespesaPendente(repository.obterCalculoTotalDespesaPendente(idDespesa, idDetalheDespesa, idFuncionario));
            detalhes.setVl_TotalDespesaPaga(repository.obterCalculoTotalDespesaPaga(idDespesa, idDetalheDespesa, idFuncionario));

            if (detalhes.getTp_Emprestimo().equalsIgnoreCase("N")) {
                detalhes.setPercentualUtilizacao(valueOf(calculaPorcentagem(BigDecimal.valueOf(Double.valueOf(detalhes.getVl_Limite())), detalhes.getVl_TotalDespesa())).concat("%"));
            }

            listDetalheDespesas.add(detalhes);
        }

        return listDetalheDespesas;
    }

    private String obterExtratoDespesasParceladasQuitacaoMes(Integer idDespesa, Integer idFuncionario, String dsMes) {
        var qtdeDespesasParceladasMes = repository.obterQuantidadeDespesasParceladasMes(idDespesa, idFuncionario);
        var qtdeDespesasQuitacaoMes = repository.obterQuantidadeDespesasParceladasQuitacaoMes(idDespesa, idFuncionario);

        StringBuffer buffer = new StringBuffer();

        buffer = MesAtual().equalsIgnoreCase(dsMes) ? buffer.append("Este mês sera quitado: ") : buffer.append("Este mês foi quitado: ");
        buffer.append(qtdeDespesasQuitacaoMes.getQtde_Parcelas() + "/" + qtdeDespesasParceladasMes);
        buffer.append(" Despesas Parceladas Totalizando: " + qtdeDespesasQuitacaoMes.getVl_Parcelas() + "R$");

        return buffer.toString();
    }

    private BigDecimal obterPercentualUtilizacaoDespesaMes(LancamentosFinanceirosDTO dto) {
        var iPercentualUtilizadoBase = calculaPorcentagem(dto.getVlSaldoPositivo(), (dto.getVlSaldoInicialMes().subtract(dto.getVlSaldoPositivo())), 2);
        var iPercentualDespesaMesCalculado = calculaPorcentagem(dto.getVlSaldoInicialMes(), (dto.getVlTotalDespesas().subtract(dto.getVlSaldoInicialMes())), 2).add(iPercentualUtilizadoBase);

        return iPercentualDespesaMesCalculado;
    }
}
