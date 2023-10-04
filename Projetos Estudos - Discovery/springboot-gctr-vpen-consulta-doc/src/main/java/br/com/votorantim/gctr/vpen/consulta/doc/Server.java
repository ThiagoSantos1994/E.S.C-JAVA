package br.com.votorantim.gctr.vpen.consulta.doc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = "br.com.votorantim")
@EnableSwagger2
public class Server {

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }
}


