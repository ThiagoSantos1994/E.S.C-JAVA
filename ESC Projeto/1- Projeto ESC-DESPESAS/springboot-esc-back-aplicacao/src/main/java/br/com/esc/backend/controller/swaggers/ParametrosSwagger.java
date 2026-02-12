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
 * Documentação Swagger para ParametrosController
 *
 * Endpoints para configurações do sistema e parâmetros de usuário.
 */
@Configuration
public class ParametrosSwagger {

    @Bean
    public Docket parametrosApi() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("4-parametros")
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.esc.backend.controller"))
                .paths(PathSelectors.ant("/api/parametros/**"))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Parâmetros e Configurações")
                .description("## Endpoints de Parâmetros\n\n" +
                        "### GET /api/parametros/obterConfiguracaoLancamentos/usuario\n" +
                        "Retorna as configurações de lançamentos do usuário.\n\n" +
                        "**Parameters:**\n" +
                        "- idFuncionario (query, required): ID do funcionário\n\n" +
                        "**Response:** Objeto ConfiguracaoLancamentosResponse\n\n" +
                        "---\n\n" +
                        "### POST /api/parametros/gravar\n" +
                        "Atualiza as configurações do sistema.\n\n" +
                        "**Request Body:** ConfiguracaoLancamentosRequest\n" +
                        "```json\n" +
                        "{\n" +
                        "  \"dataViradaMes\": 0,\n" +
                        "  \"mesReferencia\": 0,\n" +
                        "  \"anoReferencia\": 0,\n" +
                        "  \"bViradaAutomatica\": true,\n" +
                        "  \"idFuncionario\": 0\n" +
                        "}\n" +
                        "```\n\n" +
                        "**Responses:**\n" +
                        "- 200: Configurações salvas com sucesso")
                .version("1.0")
                .contact(new Contact("ESC Team", "", ""))
                .build();
    }
}

