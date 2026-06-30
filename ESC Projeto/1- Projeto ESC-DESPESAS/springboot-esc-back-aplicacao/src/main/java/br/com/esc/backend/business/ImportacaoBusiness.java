package br.com.esc.backend.business;

import br.com.esc.backend.config.RetryOnDeadlock;
import br.com.esc.backend.domain.DespesaFixaTemporariaResponse;
import br.com.esc.backend.domain.DetalheDespesasMensaisDAO;
import br.com.esc.backend.domain.ParcelasDAO;
import br.com.esc.backend.repository.AplicacaoRepository;
import br.com.esc.backend.service.DespesasParceladasServices;
import br.com.esc.backend.service.ImportarLancamentosServices;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImportacaoBusiness {

    private final AplicacaoRepository repository;
    private final ImportarLancamentosServices importacaoServices;
    private final DespesasParceladasServices despesasParceladasServices;
    private final DespesasParceladasBusiness despesasParceladasBusiness;
    private final LancamentosFinanceirosBusiness lancamentosFinanceirosBusiness;

    @RetryOnDeadlock
    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void processarImportacaoDespesasMensais(Integer idDespesa, Integer idFuncionario, String dsMes, String dsAno) {
        log.info("Processando importacao lancamentos e despesas mensais - idDespesa {} - idFuncionario {} - dsMes {} - dsAno {}", idDespesa, idFuncionario, dsMes, dsAno);
        importacaoServices.processarImportacaoDespesasMensais(idDespesa, idFuncionario, dsMes, dsAno);
        despesasParceladasBusiness.atualizaStatusDespesasParceladasEmAberto(idFuncionario);
        lancamentosFinanceirosBusiness.ordenarListaDespesasMensais(idDespesa, idFuncionario);
    }

    @RetryOnDeadlock
    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void processarImportacaoDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String dsMes, String dsAno, Boolean bReprocessarTodosValores) {
        log.info("Iniciando processamento importacao detalhe despesas mensais - Filtros: idDespesa= {} - idDetalheDespesa= {} - idFuncionario= {} - dsMes= {} - dsAno= {} - bReprocessarTodosValores= {}", idDespesa, idDetalheDespesa, idFuncionario, dsMes, dsAno, bReprocessarTodosValores);
        importacaoServices.processarImportacaoDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario, dsMes, dsAno, bReprocessarTodosValores);
        despesasParceladasBusiness.atualizaStatusDespesasParceladasEmAberto(idFuncionario);
    }

    @RetryOnDeadlock
    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void processarImportacaoDespesaParcelada(Integer idDespesa, Integer idDetalheDespesa, Integer idDespesaParcelada, Integer idConsolidacao, Integer idFuncionario) {
        if (idConsolidacao > 0) {
            log.info("Iniciando processamento importacao consolidacao - Filtros: idDespesa= {} - idDetalheDespesa= {} - idDespesaParcelada= {} - idConsolidacao= {} - idFuncionario= {}", idDespesa, idDetalheDespesa, idDespesaParcelada, idConsolidacao, idFuncionario);
            importacaoServices.processarImportacaoConsolidacao(idDespesa, idDetalheDespesa, idConsolidacao, idFuncionario);
        } else {
            log.info("Iniciando processamento importacao despesa parcelada - Filtros: idDespesa= {} - idDetalheDespesa= {} - idDespesaParcelada= {} - idFuncionario= {}", idDespesa, idDetalheDespesa, idDespesaParcelada, idFuncionario);
            importacaoServices.processarImportacaoDespesaParcelada(idDespesa, idDetalheDespesa, idDespesaParcelada, null, idFuncionario);
        }

        despesasParceladasBusiness.atualizaStatusDespesasParceladasEmAberto(idFuncionario);
    }

    @RetryOnDeadlock
    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void incluirDespesaParceladaAmortizada(Integer idDespesa, Integer idDetalheDespesa, List<ParcelasDAO> parcelas, Integer idFuncionario) {
        log.info("Gravando despesa parcelada amortizada na despesa mensal - Filtros: idDespesa= {} - idDetalheDespesa= {} - parcelas = {} - idFuncionario= {}", idDespesa, idDetalheDespesa, parcelas.toString(), idFuncionario);
        importacaoServices.incluirDespesaParceladaAmortizada(idDespesa, idDetalheDespesa, parcelas, idFuncionario);
        despesasParceladasBusiness.atualizaStatusDespesasParceladasEmAberto(idFuncionario);
    }

    @RetryOnDeadlock
    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public DespesaFixaTemporariaResponse gerarTemporariamenteDespesasMensais(Integer sMes, Integer sAno, Integer idFuncionario) {
        log.info("Gerando temporariamente despesas mensais para pre-visualizacao...");
        return importacaoServices.gerarTemporariamenteDespesasMensais(sMes, sAno, idFuncionario);
    }

    @RetryOnDeadlock
    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void deleteTodosLancamentosMensais(Integer idDespesa, Integer idFuncionario) {
        log.info("Excluindo todas despesas fixas mensais - request: idDespesa= {} - idFuncionario= {}", idDespesa, idFuncionario);
        repository.deleteTodasDespesasFixasMensais(idDespesa, idFuncionario);

        log.info("Excluindo todas despesas mensais - request: idDespesa= {} - idFuncionario= {}", idDespesa, idFuncionario);
        repository.deleteTodasDespesasMensais(idDespesa, idFuncionario);

        var listDespesasParceladas = repository.getDespesasParceladasDetalheDespesasMensais(idDespesa, idFuncionario).stream()
                .filter(d -> d.getTpParcelaAmortizada().equalsIgnoreCase("S"))
                .collect(Collectors.toList());

        for (DetalheDespesasMensaisDAO detalhe : listDespesasParceladas) {
            despesasParceladasServices.validaStatusDespesaParcelada(detalhe.getIdDespesa(), detalhe.getIdDetalheDespesa(), detalhe.getIdDespesaParcelada(), detalhe.getIdParcela(), detalhe.getIdFuncionario(), detalhe.getTpStatus(), true);
        }

        log.info("Excluindo todos detalhes despesas mensais - request: idDespesa= {} - idFuncionario= {}", idDespesa, idFuncionario);
        repository.deleteTodosDetalhesDespesasMensais(idDespesa, idFuncionario);

        log.info("Excluindo todas observacoes detalhes despesas mensais - request: idDespesa= {} - idFuncionario= {}", idDespesa, idFuncionario);
        repository.deleteTodasObservacaoDetalheDespesaMensal(idDespesa, null, idFuncionario);

        log.info("Excluindo todos logs detalhes despesas mensais - request: idDespesa= {} - idFuncionario= {}", idDespesa, idFuncionario);
        repository.deleteTodasDespesasMensaisLogs(idDespesa, idFuncionario);

        log.info("Atualizando status das parcelas comuns e amortizadas para Pendente - request: idDespesa= {} - idFuncionario= {}", idDespesa, idFuncionario);
        repository.updateParcelaStatusPendenteDespesasExcluidas(idDespesa, idFuncionario);

        log.info("Atualizando status das despesas parceladas para Em Aberto - request: idFuncionario= {}", idFuncionario);
        despesasParceladasBusiness.atualizaStatusDespesasParceladasEmAberto(idFuncionario);
    }
}
