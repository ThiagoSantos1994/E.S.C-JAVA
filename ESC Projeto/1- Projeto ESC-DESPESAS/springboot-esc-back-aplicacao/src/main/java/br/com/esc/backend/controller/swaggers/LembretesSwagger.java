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
 * Documentação Swagger para LembretesController
 *
 * Endpoints para gerenciamento de lembretes do sistema.
 */
@Configuration
public class LembretesSwagger {

    @Bean
    public Docket lembretesApi() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("5-lembretes")
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.esc.backend.controller"))
                .paths(PathSelectors.ant("/api/lembretes/**"))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Lembretes")
                .description("## Endpoints de Lembretes\n\n" +
                        "### GET /api/lembretes/detalhe\n" +
                        "Retorna os detalhes completos de um lembrete.\n\n" +
                        "### GET /api/lembretes/monitor\n" +
                        "Retorna lista de lembretes pendentes para monitoramento.\n\n" +
                        "### GET /api/lembretes/obterTituloLembretes\n" +
                        "Retorna lista de títulos dos lembretes (filtrado por status).\n\n" +
                        "### POST /api/lembretes/monitor/baixar\n" +
                        "Marca lembretes como concluídos ou adiados.\n\n" +
                        "### POST /api/lembretes/detalhe/gravar\n" +
                        "Cria ou atualiza um lembrete.\n\n" +
                        "### POST /api/lembretes/detalhe/excluir\n" +
                        "Remove um lembrete do sistema.")
                .version("1.0")
                .contact(new Contact("ESC Team", "", ""))
                .build();
    }
}


