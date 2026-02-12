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
 * Documentação Swagger para DetalheDespesasController
 *
 * Endpoints para relatórios e detalhes de despesas.
 */
@Configuration
public class DetalheDespesasSwagger {

    @Bean
    public Docket detalheDespesasApi() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("8-detalhe-despesas")
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.esc.backend.controller"))
                .paths(PathSelectors.ant("/api/detalheDespesas/**"))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Detalhe de Despesas")
                .description("## Endpoints de Detalhe de Despesas\n\n" +
                        "### GET /api/detalheDespesas/despesasParceladas/obterRelatorioDespesasParceladasQuitacao\n" +
                        "Retorna relatório de despesas parceladas quitadas.\n\n" +
                        "**Parameters:**\n" +
                        "- idDespesa (query, required)\n" +
                        "- idDetalheDespesa (query, required)\n" +
                        "- idFuncionario (query, required)\n\n" +
                        "---\n\n" +
                        "### GET /api/detalheDespesas/consolidacao/obterRelatorioDespesasParceladas\n" +
                        "Retorna relatório de despesas parceladas de uma consolidação.\n\n" +
                        "**Parameters:**\n" +
                        "- idDespesa (query, required)\n" +
                        "- idDetalheDespesa (query, required)\n" +
                        "- idConsolidacao (query, required)\n" +
                        "- idFuncionario (query, required)")
                .version("1.0")
                .contact(new Contact("ESC Team", "", ""))
                .build();
    }
}

