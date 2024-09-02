package br.com.esc.backend.config;

import br.com.esc.backend.business.LancamentosBusinessService;
import br.com.esc.backend.repository.AplicacaoRepository;
import br.com.esc.backend.repository.AutenticacaoRepository;
import br.com.esc.backend.repository.BackupRepository;
import br.com.esc.backend.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AplicacaoConfiguration {

    @Bean
    @Primary
    LancamentosBusinessService lancamentosFinanceirosService(AplicacaoRepository repository, ImportarLancamentosServices importacaoBusiness,
                                                             LancamentosFinanceirosServices lancamentosBusiness, DetalheDespesasServices detalheDespesasServices,
                                                             DespesasParceladasServices despesasParceladasServices, BackupServices backupServices, AutenticacaoServices autenticacaoServices,
                                                             LembreteServices lembreteServices, ConsolidacaoService consolidacaoService) {
        return new LancamentosBusinessService(repository, importacaoBusiness, lancamentosBusiness, detalheDespesasServices, despesasParceladasServices, backupServices, autenticacaoServices, lembreteServices, consolidacaoService);
    }

    @Bean
    @Primary
    ImportarLancamentosServices importarLancamentosBusiness(AplicacaoRepository repository, DetalheDespesasServices detalheDespesasServices, ConsolidacaoService consolidacaoService) {
        return new ImportarLancamentosServices(repository, detalheDespesasServices, consolidacaoService);
    }

    @Bean
    @Primary
    LancamentosFinanceirosServices lancamentosFinanceirosBusiness(AplicacaoRepository repository) {
        return new LancamentosFinanceirosServices(repository);
    }

    @Bean
    @Primary
    DetalheDespesasServices detalheDespesasBusiness(AplicacaoRepository repository, DespesasParceladasServices despesasParceladasServices, ConsolidacaoService consolidacaoService) {
        return new DetalheDespesasServices(repository, despesasParceladasServices, consolidacaoService);
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

    @Bean
    @Primary
    AutenticacaoServices autenticacaoServices(AplicacaoRepository aplicacaoRepository, AutenticacaoRepository repository) {
        return new AutenticacaoServices(aplicacaoRepository, repository);
    }

    @Bean
    @Primary
    LembreteServices lembreteServices(AplicacaoRepository repository) {
        return new LembreteServices(repository);
    }

    @Bean
    @Primary
    ConsolidacaoService consolidacaoService(AplicacaoRepository repository) {
        return new ConsolidacaoService(repository);
    }
}
