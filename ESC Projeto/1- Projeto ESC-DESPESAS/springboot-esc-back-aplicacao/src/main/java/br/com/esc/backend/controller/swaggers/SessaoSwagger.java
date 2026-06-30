package br.com.esc.backend.controller.swaggers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Documentação Swagger para SessaoController
 *
 * Endpoints para validação de sessão de usuário.
 */
@Configuration
public class SessaoSwagger {

    @Bean
    public Docket sessaoApi() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("3-sessao")
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.esc.backend.controller"))
                .paths(PathSelectors.ant("/api/sessao/**"))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Validação de Sessão")
                .description("## Endpoints de Sessão\n\n" +
                        "### GET /api/sessao/validar\n" +
                        "Valida se a sessão do usuário ainda está ativa.\n\n" +
                        "**Parameters:**\n" +
                        "- idFuncionario (query, required): ID do funcionário\n\n" +
                        "**Validações realizadas:**\n" +
                        "- Verifica se login foi realizado hoje\n" +
                        "- Valida tempo de inatividade\n" +
                        "- Compara com limite de sessão configurado\n\n" +
                        "**Responses:**\n" +
                        "- 200: Sessão validada (retorna isSessaoValida: true/false)")
                .version("1.0")
                .contact(new Contact("ESC Team", "", ""))
                .build();
    }
}

