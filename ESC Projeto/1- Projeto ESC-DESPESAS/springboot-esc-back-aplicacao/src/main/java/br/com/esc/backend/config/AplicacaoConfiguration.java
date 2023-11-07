package br.com.esc.backend.config;

import br.com.esc.backend.service.DespesasParceladasServices;
import br.com.esc.backend.service.DetalheDespesasServices;
import br.com.esc.backend.service.ImportarLancamentosServices;
import br.com.esc.backend.service.LancamentosFinanceirosServices;
import br.com.esc.backend.repository.AplicacaoRepository;
import br.com.esc.backend.business.LancamentosBusinessService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AplicacaoConfiguration {

    @Bean
    @Primary
    LancamentosBusinessService lancamentosFinanceirosService(AplicacaoRepository repository, ImportarLancamentosServices importacaoBusiness, LancamentosFinanceirosServices lancamentosBusiness, DetalheDespesasServices detalheDespesasServices, DespesasParceladasServices despesasParceladasServices) {
        return new LancamentosBusinessService(repository, importacaoBusiness, lancamentosBusiness, detalheDespesasServices, despesasParceladasServices);
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
}
