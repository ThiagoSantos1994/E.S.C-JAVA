package com.br.unip.dados.uteis.apis.commons;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Component
public class ObjectParser {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Gson gson;

    public ObjectParser() {
        GsonBuilder builder = new GsonBuilder();
        gson = builder.create();
    }

    public String parser(Object origem) {

        logger.info("Realizando a conversão do objeto para JSON");

        String parser = gson.toJson(origem);

        logger.info("<<INICIO>> Refatorando parserResponse (Retirando acentos)");

        ByteBuffer buffer = StandardCharsets.UTF_8.encode(parser);

        String utf8String = StandardCharsets.UTF_8.decode(buffer).toString();

        logger.info("<<FIM>> Parser refatorado com sucesso");

        return utf8String;
    }
}
