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
 * Documentação Swagger para SenhaController
 *
 * Endpoints administrativos para gerenciamento de senhas e hashes BCrypt.
 * ATENÇÃO: Estes endpoints devem ser protegidos em ambiente de produção!
 */
@Configuration
public class SenhaSwagger {

    @Bean
    public Docket senhaApi() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("11-admin-senha")
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.esc.backend.controller"))
                .paths(PathSelectors.ant("/api/admin/senha/**"))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Administração de Senhas")
                .description("## Endpoints Administrativos de Senhas (BCrypt)\n\n" +
                        "⚠️ **ATENÇÃO:** Estes endpoints são para uso administrativo apenas!\n" +
                        "Devem ser protegidos em ambiente de produção.\n\n" +
                        "---\n\n" +
                        "### POST /api/admin/senha/gerarHash\n" +
                        "Gera um hash BCrypt para uma senha fornecida.\n\n" +
                        "**Parâmetros:**\n" +
                        "- `senha` (query) - Senha em texto plano\n\n" +
                        "**Response:**\n" +
                        "```json\n" +
                        "{\n" +
                        "  \"mensagem\": \"$2a$10$...(hash BCrypt)\"\n" +
                        "}\n" +
                        "```\n\n" +
                        "---\n\n" +
                        "### POST /api/admin/senha/validarHash\n" +
                        "Valida se uma senha corresponde a um hash BCrypt.\n\n" +
                        "**Parâmetros:**\n" +
                        "- `senha` (query) - Senha em texto plano\n" +
                        "- `hash` (query) - Hash BCrypt para validar\n\n" +
                        "**Response:**\n" +
                        "```json\n" +
                        "{\n" +
                        "  \"mensagem\": \"Senha válida\",\n" +
                        "  \"isSessaoValida\": true\n" +
                        "}\n" +
                        "```\n\n" +
                        "---\n\n" +
                        "### GET /api/admin/senha/verificarFormato\n" +
                        "Verifica se uma string é um hash BCrypt válido.\n\n" +
                        "**Parâmetros:**\n" +
                        "- `valor` (query) - Valor a ser verificado\n\n" +
                        "**Response:**\n" +
                        "```json\n" +
                        "{\n" +
                        "  \"mensagem\": \"Formato BCrypt válido\",\n" +
                        "  \"isSessaoValida\": true\n" +
                        "}\n" +
                        "```")
                .version("1.0.0")
                .contact(new Contact("ESC Backend", "", ""))
                .build();
    }
}

