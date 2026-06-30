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
 * Documentação Swagger para ConsolidacaoController
 *
 * Endpoints para gerenciamento de consolidações de despesas.
 */
@Configuration
public class ConsolidacaoSwagger {

    @Bean
    public Docket consolidacaoApi() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("6-consolidacao")
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.esc.backend.controller"))
                .paths(PathSelectors.ant("/api/consolidacao/**"))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Consolidação de Despesas")
                .description("## Endpoints de Consolidação\n\n" +
                        "### GET /api/consolidacao/importacao/consultarConsolidacoes\n" +
                        "Retorna lista de consolidações disponíveis para associar a uma despesa.\n\n" +
                        "### GET /api/consolidacao/obterTituloConsolidacoes\n" +
                        "Retorna lista de títulos das consolidações.\n\n" +
                        "### GET /api/consolidacao/consultar\n" +
                        "Retorna os detalhes completos de uma consolidação.\n\n" +
                        "### POST /api/consolidacao/gravar\n" +
                        "Cria ou atualiza uma consolidação.\n\n" +
                        "### POST /api/consolidacao/excluir\n" +
                        "Remove uma consolidação do sistema.\n\n" +
                        "### POST /api/consolidacao/despesas/desassociar\n" +
                        "Remove associação entre despesas e consolidação.")
                .version("1.0")
                .contact(new Contact("ESC Team", "", ""))
                .build();
    }
}

