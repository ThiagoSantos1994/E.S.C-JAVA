package br.com.esc.backend.config;

import br.com.esc.backend.business.DetalheDespesasBusiness;
import br.com.esc.backend.business.ImportarLancamentosBusiness;
import br.com.esc.backend.business.LancamentosFinanceirosBusiness;
import br.com.esc.backend.repository.AplicacaoRepository;
import br.com.esc.backend.service.LancamentosFinanceirosService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AplicacaoConfiguration {

    @Bean
    LancamentosFinanceirosService lancamentosFinanceirosService(AplicacaoRepository repository, ImportarLancamentosBusiness importacaoBusiness, LancamentosFinanceirosBusiness lancamentosBusiness, DetalheDespesasBusiness detalheDespesasBusiness) {
        return new LancamentosFinanceirosService(repository, importacaoBusiness, lancamentosBusiness, detalheDespesasBusiness);
    }

    @Bean
    ImportarLancamentosBusiness importarLancamentosBusiness(AplicacaoRepository repository) {
        return new ImportarLancamentosBusiness(repository);
    }

    @Bean
    LancamentosFinanceirosBusiness lancamentosFinanceirosBusiness(AplicacaoRepository repository) {
        return new LancamentosFinanceirosBusiness(repository);
    }

    @Bean
    DetalheDespesasBusiness detalheDespesasBusiness(AplicacaoRepository repository) {
        return new DetalheDespesasBusiness(repository);
    }
}
