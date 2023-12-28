package br.com.esc.backend.config;

import br.com.esc.backend.business.LancamentosBusinessService;
import br.com.esc.backend.repository.AplicacaoRepository;
import br.com.esc.backend.repository.BackupRepository;
import br.com.esc.backend.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AplicacaoConfiguration {

    @Bean
    @Primary
    LancamentosBusinessService lancamentosFinanceirosService(AplicacaoRepository repository, ImportarLancamentosServices importacaoBusiness, LancamentosFinanceirosServices lancamentosBusiness, DetalheDespesasServices detalheDespesasServices, DespesasParceladasServices despesasParceladasServices, BackupServices backupServices) {
        return new LancamentosBusinessService(repository, importacaoBusiness, lancamentosBusiness, detalheDespesasServices, despesasParceladasServices, backupServices);
    }

    @Bean
    @Primary
    ImportarLancamentosServices importarLancamentosBusiness(AplicacaoRepository repository, DetalheDespesasServices detalheDespesasServices) {
        return new ImportarLancamentosServices(repository, detalheDespesasServices);
    }

    @Bean
    @Primary
    LancamentosFinanceirosServices lancamentosFinanceirosBusiness(AplicacaoRepository repository) {
        return new LancamentosFinanceirosServices(repository);
    }

    @Bean
    @Primary
    DetalheDespesasServices detalheDespesasBusiness(AplicacaoRepository repository, DespesasParceladasServices despesasParceladasServices) {
        return new DetalheDespesasServices(repository, despesasParceladasServices);
    }

    @Bean
    @Primary
    DespesasParceladasServices despesasParceladasServices(AplicacaoRepository repository) {
        return new DespesasParceladasServices(repository);
    }

    @Bean
    @Primary
    BackupServices backupServices(BackupRepository repository) {
        return new BackupServices(repository);
    }
}
