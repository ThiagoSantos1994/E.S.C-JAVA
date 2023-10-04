package br.com.votorantim.gctr.vpen.consulta.doc.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

/**
 * Configurações do Swagger
 */
@Configuration
public class DocketConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(Collections.singletonList(new BasicAuth("basic")));
    }

    //Customize conforme sua necessidade
    private ApiInfo apiInfo() {
        return new ApiInfo(
                "gctr.vpen.consulta.doc",
                "API Backend da gctr.vpen",
                "0.0.1",
                "",
                new Contact("gctr",
                        "https://<url-do-bit-bucket>",
                        "<email@bv.com.br>"),
                "Licence API", "https://www.bv.com.br/", Collections.emptyList());
    }
    
}
