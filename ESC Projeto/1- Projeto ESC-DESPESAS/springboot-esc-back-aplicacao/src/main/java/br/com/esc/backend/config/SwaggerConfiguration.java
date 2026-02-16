package br.com.esc.backend.config;

import br.com.esc.backend.controller.swaggers.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Configuração do Swagger/OpenAPI para documentação da API REST.
 *
 * A documentação dos endpoints está organizada em arquivos separados por controller.
 * Cada controller tem seu próprio grupo no Swagger UI.
 *
 * Acesse a documentação em: http://localhost:8020/swagger-ui/
 *
 * Use o dropdown "Select a definition" no canto superior direito para alternar entre os grupos.
 */
@Configuration
@Import({
        LoginSwagger.class,
        BackupSwagger.class,
        SessaoSwagger.class,
        ParametrosSwagger.class,
        LembretesSwagger.class,
        ConsolidacaoSwagger.class,
        DespesasParceladasSwagger.class,
        DetalheDespesasSwagger.class,
        LancamentosMensaisSwagger.class,
        LancamentosFinanceirosSwagger.class,
        SenhaSwagger.class
})
public class SwaggerConfiguration {
    // Configuração centralizada
    // Cada controller tem seu próprio Docket bean nos arquivos Swagger separados
}





