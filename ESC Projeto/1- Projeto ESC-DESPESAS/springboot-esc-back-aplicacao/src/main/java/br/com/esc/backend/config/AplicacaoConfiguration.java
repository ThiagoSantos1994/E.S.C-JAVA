package br.com.esc.backend.config;

import br.com.esc.backend.repository.AplicacaoRepository;
import br.com.esc.backend.service.LancamentosFinanceirosService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AplicacaoConfiguration {

    @Bean
    LancamentosFinanceirosService lancamentosFinanceirosService(AplicacaoRepository repository) {
        return new LancamentosFinanceirosService(repository);
    }

}
