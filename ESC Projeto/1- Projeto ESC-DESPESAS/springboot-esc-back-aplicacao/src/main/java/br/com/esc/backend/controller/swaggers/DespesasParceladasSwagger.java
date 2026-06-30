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
 * Documentação Swagger para DespesasParceladasController
 *
 * Endpoints para gerenciamento de despesas parceladas.
 */
@Configuration
public class DespesasParceladasSwagger {

    @Bean
    public Docket despesasParceladasApi() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("7-despesas-parceladas")
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.esc.backend.controller"))
                .paths(PathSelectors.ant("/api/despesasParceladas/**"))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Despesas Parceladas")
                .description("## Endpoints de Despesas Parceladas\n\n" +
                        "Gerenciamento completo de despesas parceladas incluindo:\n\n" +
                        "- Listagem de despesas parceladas\n" +
                        "- Consulta de valores e parcelas\n" +
                        "- Geração de fluxo de parcelas\n" +
                        "- Validações de títulos\n" +
                        "- Quitação de despesas\n" +
                        "- Amortização de parcelas\n" +
                        "- Relatórios de quitação\n\n" +
                        "**Principais endpoints:**\n" +
                        "- GET /api/despesasParceladas/obterListaDespesas\n" +
                        "- GET /api/despesasParceladas/consultar\n" +
                        "- GET /api/despesasParceladas/gerarFluxoParcelas\n" +
                        "- POST /api/despesasParceladas/gravar\n" +
                        "- POST /api/despesasParceladas/quitar\n" +
                        "- DELETE /api/despesasParceladas/excluir")
                .version("1.0")
                .contact(new Contact("ESC Team", "", ""))
                .build();
    }
}

