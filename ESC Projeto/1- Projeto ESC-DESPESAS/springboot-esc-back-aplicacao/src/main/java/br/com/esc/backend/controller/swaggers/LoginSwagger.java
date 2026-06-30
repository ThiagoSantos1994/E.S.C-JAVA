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
 * Documentação Swagger para LoginController
 *
 * Endpoints de autenticação e gerenciamento de sessão de usuários.
 */
@Configuration
public class LoginSwagger {

    @Bean
    public Docket loginApi() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("1-autenticacao")
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.esc.backend.controller"))
                .paths(PathSelectors.ant("/api/login/**"))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Autenticação")
                .description("## Endpoints de Autenticação\n\n" +
                        "### POST /api/login/autenticar\n" +
                        "Realiza autenticação do usuário no sistema.\n\n" +
                        "**Request Body:**\n" +
                        "```json\n" +
                        "{\n" +
                        "  \"usuario\": \"string\",\n" +
                        "  \"senha\": \"string\"\n" +
                        "}\n" +
                        "```\n\n" +
                        "**Responses:**\n" +
                        "- 200: Autenticação realizada com sucesso\n" +
                        "- 401: Credenciais inválidas\n" +
                        "- 403: Usuário bloqueado ou excluído\n\n" +
                        "---\n\n" +
                        "### DELETE /api/login/limparDadosTemporarios\n" +
                        "Remove dados temporários de lançamentos do usuário.\n\n" +
                        "**Parameters:**\n" +
                        "- idFuncionario (query, required): ID do funcionário\n\n" +
                        "**Responses:**\n" +
                        "- 200: Dados temporários limpos com sucesso")
                .version("1.0")
                .contact(new Contact("ESC Team", "", ""))
                .build();
    }
}

