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
 * Documentação Swagger para LancamentosMensaisController
 *
 * Endpoints para lançamentos mensais consolidados.
 */
@Configuration
public class LancamentosMensaisSwagger {

    @Bean
    public Docket lancamentosMensaisApi() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("9-lancamentos-mensais")
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.esc.backend.controller"))
                .paths(PathSelectors.ant("/api/lancamentosMensais/**"))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Lançamentos Mensais")
                .description("## Endpoints de Lançamentos Mensais\n\n" +
                        "### GET /api/lancamentosMensais/consolidados/consultar\n" +
                        "Retorna lista de lançamentos mensais consolidados.\n\n" +
                        "**Parameters:**\n" +
                        "- idDespesa (query, required): ID da despesa\n" +
                        "- idConsolidacao (query, required): ID da consolidação\n" +
                        "- idFuncionario (query, required): ID do funcionário\n\n" +
                        "**Response:** List<LancamentosMensaisDAO>\n\n" +
                        "**Descrição:**\n" +
                        "Retorna todos os lançamentos mensais associados a uma consolidação específica.")
                .version("1.0")
                .contact(new Contact("ESC Team", "", ""))
                .build();
    }
}

