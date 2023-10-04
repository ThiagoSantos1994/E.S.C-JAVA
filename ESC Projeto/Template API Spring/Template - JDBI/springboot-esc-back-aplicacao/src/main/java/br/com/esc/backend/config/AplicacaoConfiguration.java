package br.com.esc.backend.config;

import br.com.esc.backend.repository.AplicacaoRepository;
import br.com.esc.backend.service.AplicacaoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AplicacaoConfiguration {

    @Bean
    AplicacaoService aplicacaoService(AplicacaoRepository repository) {
        return new AplicacaoService(repository);
    }

}
