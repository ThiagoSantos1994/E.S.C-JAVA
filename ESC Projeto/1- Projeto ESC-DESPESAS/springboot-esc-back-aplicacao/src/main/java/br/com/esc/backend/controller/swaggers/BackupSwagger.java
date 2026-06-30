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
 * Documentação Swagger para BackupController
 *
 * Endpoints para processamento de backup da base de dados.
 */
@Configuration
public class BackupSwagger {

    @Bean
    public Docket backupApi() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("2-backup")
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.esc.backend.controller"))
                .paths(PathSelectors.ant("/api/backup/**"))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Backup")
                .description("## Endpoints de Backup\n\n" +
                        "### POST /api/backup/processar\n" +
                        "Executa o backup completo da base de dados.\n\n" +
                        "**Descrição:**\n" +
                        "- Realiza backup tabela por tabela\n" +
                        "- Habilita IDENTITY_INSERT quando necessário\n" +
                        "- Registra sucessos e falhas em logs\n" +
                        "- Retorna mensagem consolidada do processo\n\n" +
                        "**Responses:**\n" +
                        "- 200: Backup processado com sucesso\n" +
                        "- 500: Erro ao processar backup")
                .version("1.0")
                .contact(new Contact("ESC Team", "", ""))
                .build();
    }
}

