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
 * Documentação Swagger para LancamentosFinanceirosController
 *
 * Endpoints para lançamentos financeiros e despesas mensais (controller principal).
 */
@Configuration
public class LancamentosFinanceirosSwagger {

    @Bean
    public Docket lancamentosFinanceirosApi() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("10-lancamentos-financeiros")
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.esc.backend.controller"))
                .paths(PathSelectors.ant("/api/lancamentosFinanceiros/**"))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Lançamentos Financeiros")
                .description("## Endpoints de Lançamentos Financeiros\n\n" +
                        "Controller principal do sistema com mais de 50 endpoints para:\n\n" +
                        "### Consultas\n" +
                        "- Obter lançamentos financeiros por período\n" +
                        "- Consultar detalhes de despesas mensais\n" +
                        "- Obter subtotais e extratos\n" +
                        "- Consultar categorias de despesas\n\n" +
                        "### Operações de Despesas\n" +
                        "- Gravar despesas mensais\n" +
                        "- Gravar despesas fixas\n" +
                        "- Excluir despesas\n" +
                        "- Alterar títulos e referências\n\n" +
                        "### Consolidação\n" +
                        "- Associar/desassociar despesas à consolidação\n" +
                        "- Processar pagamentos\n" +
                        "- Desfazer pagamentos\n\n" +
                        "### Importação\n" +
                        "- Processar importação de despesas\n" +
                        "- Importar despesas parceladas\n" +
                        "- Gerar despesas futuras\n\n" +
                        "### Gestão de Parcelas\n" +
                        "- Adiar fluxo de parcelas\n" +
                        "- Desfazer adiamento\n" +
                        "- Incluir amortização\n\n" +
                        "### Utilidades\n" +
                        "- Obter nova chave key\n" +
                        "- Ordenar listas\n" +
                        "- Validações diversas\n\n" +
                        "**Endpoints principais:**\n" +
                        "- GET /api/lancamentosFinanceiros/consultar\n" +
                        "- GET /api/lancamentosFinanceiros/detalheDespesasMensais/consultar\n" +
                        "- POST /api/lancamentosFinanceiros/despesasMensais/incluir\n" +
                        "- POST /api/lancamentosFinanceiros/baixarPagamentoDespesa\n" +
                        "- POST /api/lancamentosFinanceiros/importacao/processamento")
                .version("1.0")
                .contact(new Contact("ESC Team", "", ""))
                .build();
    }
}

