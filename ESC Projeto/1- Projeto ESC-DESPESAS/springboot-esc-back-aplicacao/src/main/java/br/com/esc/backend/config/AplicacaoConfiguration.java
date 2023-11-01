package br.com.esc.backend.config;

import br.com.esc.backend.service.DespesasParceladasServices;
import br.com.esc.backend.service.DetalheDespesasServices;
import br.com.esc.backend.service.ImportarLancamentosServices;
import br.com.esc.backend.service.LancamentosFinanceirosServices;
import br.com.esc.backend.repository.AplicacaoRepository;
import br.com.esc.backend.business.LancamentosBusinessService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AplicacaoConfiguration {

    @Bean
    LancamentosBusinessService lancamentosFinanceirosService(AplicacaoRepository repository, ImportarLancamentosServices importacaoBusiness, LancamentosFinanceirosServices lancamentosBusiness, DetalheDespesasServices detalheDespesasServices, DespesasParceladasServices despesasParceladasServices) {
        return new LancamentosBusinessService(repository, importacaoBusiness, lancamentosBusiness, detalheDespesasServices, despesasParceladasServices);
    }

    @Bean
    ImportarLancamentosServices importarLancamentosBusiness(AplicacaoRepository repository, DetalheDespesasServices detalheDespesasServices) {
        return new ImportarLancamentosServices(repository, detalheDespesasServices);
    }

    @Bean
    LancamentosFinanceirosServices lancamentosFinanceirosBusiness(AplicacaoRepository repository) {
        return new LancamentosFinanceirosServices(repository);
    }

    @Bean
    DetalheDespesasServices detalheDespesasBusiness(AplicacaoRepository repository, DespesasParceladasServices despesasParceladasServices) {
        return new DetalheDespesasServices(repository, despesasParceladasServices);
    }

    @Bean
    DespesasParceladasServices despesasParceladasServices(AplicacaoRepository repository) {
        return new DespesasParceladasServices(repository);
    }
}
