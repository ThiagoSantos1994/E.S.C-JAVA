package br.com.esc.backend.config;

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
    ImportarLancamentosServices importarLancamentosServices(AplicacaoRepository repository, DetalheDespesasServices detalheDespesasServices, ConsolidacaoService consolidacaoService) {
        return new ImportarLancamentosServices(repository, detalheDespesasServices, consolidacaoService);
    }

    @Bean
    @Primary
    LancamentosFinanceirosServices lancamentosFinanceirosServices(AplicacaoRepository repository) {
        return new LancamentosFinanceirosServices(repository);
    }

    @Bean
    @Primary
    DetalheDespesasServices detalheDespesasServices(AplicacaoRepository repository, DespesasParceladasServices despesasParceladasServices, ConsolidacaoService consolidacaoService) {
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
    AutenticacaoServices autenticacaoServices(
            AutenticacaoRepository autenticacaoRepository,
            AuditoriaAcessoService auditoriaAcessoService,
            ConfiguracaoLancamentosService configuracaoLancamentosService) {
        return new AutenticacaoServices(autenticacaoRepository, auditoriaAcessoService, configuracaoLancamentosService);
    }

    @Bean
    @Primary
    AuditoriaAcessoService auditoriaAcessoService(AutenticacaoRepository repository) {
        return new AuditoriaAcessoService(repository);
    }

    @Bean
    @Primary
    ConfiguracaoLancamentosService configuracaoLancamentosService(AplicacaoRepository repository) {
        return new ConfiguracaoLancamentosService(repository);
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
