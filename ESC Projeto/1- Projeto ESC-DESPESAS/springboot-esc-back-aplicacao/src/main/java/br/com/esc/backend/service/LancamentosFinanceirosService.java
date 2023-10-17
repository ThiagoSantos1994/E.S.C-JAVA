package br.com.esc.backend.service;

import br.com.esc.backend.business.ImportarLancamentosBusiness;
import br.com.esc.backend.domain.*;
import br.com.esc.backend.repository.AplicacaoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static br.com.esc.backend.utils.DataUtils.MesAtual;
import static br.com.esc.backend.utils.MotorCalculoUtils.calculaPorcentagem;
import static br.com.esc.backend.utils.MotorCalculoUtils.calcularReceitaPositivaMes;

@RequiredArgsConstructor
@Slf4j
public class LancamentosFinanceirosService {

    private final AplicacaoRepository repository;
    private final ImportarLancamentosBusiness lancamentosBusiness;

    public LancamentosFinanceirosDTO obterLancamentosFinanceiros(String dsMes, String dsAno, Integer idFuncionario) {
        log.info("Consultando lancamentos financeiros - request: dsMes= {} - dsAno= {} - idFuncionario= {}", dsMes, dsAno, idFuncionario);
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
        dto.setPcUtilizacaoDespesasMes(new DecimalFormat("00").format(obterPercentualUtilizacaoDespesaMes(dto)).concat("%"));
        dto.setVlSaldoDisponivelMes(dto.getVlSaldoPositivo().subtract(dto.getVlTotalDespesas()));
        dto.setDespesasFixasMensais(despesasFixasMensais);
        dto.setLancamentosMensais(obterLancamentosMensais(idDespesa, idDespesaAnterior, idFuncionario));
        dto.setLabelQuitacaoParcelasMes(obterExtratoDespesasParceladasQuitacaoMes(idDespesa, idFuncionario, dsMes));

        /*Especifico para aplicação VB6*/
        dto.setSizeDespesasFixasMensaisVB(despesasFixasMensais.size());
        dto.setSizeLancamentosMensaisVB(dto.getLancamentosMensais().size());

        log.info("Consultando lancamentos financeiros - response: {}", dto);
        return dto;
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
                var percentual = calculaPorcentagem(BigDecimal.valueOf(Double.valueOf(detalhes.getVlLimite())), detalhes.getVlTotalDespesa());
                detalhes.setPercentualUtilizacao(new DecimalFormat("0.00").format(percentual).concat("%"));
            }

            lancamentosMensaisList.add(detalhes);
        }

        return lancamentosMensaisList;
    }

    private String obterExtratoDespesasParceladasQuitacaoMes(Integer idDespesa, Integer idFuncionario, String dsMes) {
        var qtdeDespesasParceladasMes = repository.getQuantidadeDespesasParceladasMes(idDespesa, idFuncionario);
        var qtdeDespesasQuitacaoMes = repository.getQuantidadeDespesasParceladasQuitacaoMes(idDespesa, idFuncionario);

        StringBuffer buffer = new StringBuffer();

        buffer = MesAtual().equalsIgnoreCase(dsMes) ? buffer.append("Este mês sera quitado: ") : buffer.append("Este mês foi quitado: ");
        buffer.append(qtdeDespesasQuitacaoMes.getQtdeParcelas() + "/" + qtdeDespesasParceladasMes);
        buffer.append(" Despesas Parceladas Totalizando: " + qtdeDespesasQuitacaoMes.getVlParcelas() + "R$");

        return buffer.toString();
    }

    private BigDecimal obterPercentualUtilizacaoDespesaMes(LancamentosFinanceirosDTO dto) {
        var iPercentualUtilizadoBase = calculaPorcentagem(dto.getVlSaldoPositivo(), (dto.getVlSaldoInicialMes().subtract(dto.getVlSaldoPositivo())), 2);
        var iPercentualDespesaMesCalculado = calculaPorcentagem(dto.getVlSaldoInicialMes(), (dto.getVlTotalDespesas().subtract(dto.getVlSaldoInicialMes())), 2).add(iPercentualUtilizadoBase);

        return iPercentualDespesaMesCalculado;
    }

    public void gravarDespesasFixasMensais(DespesasFixasMensaisRequest request) {
        log.info("Inserindo despesas fixas mensais - request: {}", request);
        repository.insertDespesasFixasMensais(request);
    }

    public void updateDespesasFixasMensais(DespesasFixasMensaisRequest request) {
        log.info("Atualizando despesas fixas mensais - request: {}", request);
        repository.updateDespesasFixasMensais(request);
    }

    public void deleteDespesasFixasMensais(Integer idDespesa, Integer idOrdem, Integer idFuncionario) {
        log.info("Excluindo despesas fixas mensais - request: idDespesa= {} - idOrdem= {} - idFuncionario= {}", idDespesa, idOrdem, idFuncionario);
        repository.deleteDespesasFixasMensais(idDespesa, idOrdem, idFuncionario);
    }

    @Transactional
    public void processarImportacaoDespesasMensais(Integer idDespesa, Integer idFuncionario, String dsMes, String dsAno) throws Exception {
        log.info("Processando importacao despesas mensais - idDespesa {} - idFuncionario {} - dsMes {} - dsAno {}", idDespesa, idFuncionario, dsMes, dsAno);
        if (idDespesa == 0) {
            var despesasFixasMensais = lancamentosBusiness.processarDespesasFixasMensais(idDespesa, idFuncionario, dsMes, dsAno);
            for (DespesasFixasMensaisRequest despesasFixas : despesasFixasMensais) {
                log.info("Gravando despesas fixas mensais >>>  {}", despesasFixas);
                repository.insertDespesasFixasMensais(despesasFixas);
            }

            var idDespesaNova = despesasFixasMensais.get(0).getIdDespesa();
            var despesasMensais = lancamentosBusiness.processarDespesasMensais(idDespesaNova, idFuncionario);
            for (DespesasMensaisDAO mensais : despesasMensais) {
                log.info("Gravando despesas mensais >>>  {}", mensais);
                repository.insertDespesasMensais(mensais);

                var idDetalheDespesa = mensais.getIdDetalheDespesa();
                for (DetalheDespesasMensaisDAO detalheDespesa : lancamentosBusiness.processarDetalheDespesasMensais(idDespesaNova, idDetalheDespesa, idFuncionario, dsMes, dsAno)) {
                    log.info("Gravando detalhe despesas mensais >>>  {}", detalheDespesa);
                    repository.insertDetalheDespesasMensais(detalheDespesa);
                }
            }
        } else {
            /*log.info("Reprocessando importacao despesas mensais");
            lancamentosBusiness.reprocessarDespesasMensais(idDespesa, idFuncionario);

            log.info("Reprocessando importacao detalhes despesas mensais");
             */
        }
        log.info("Importacao despesas mensais concluida com sucesso!");
    }


}
