package com.br.esc.login.commons;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ObjectParser {

    private static final Logger log = LoggerFactory.getLogger(ObjectParser.class);
    private Gson gson;

    public ObjectParser() {
        GsonBuilder builder = new GsonBuilder();
        gson = builder.create();
    }

    public String parser(Object origem) {

        log.info("Realizando a conversão do objeto para JSON");

        String parser = gson.toJson(origem);

        log.info("<<INICIO>> Refatorando parserResponse (Retirando acentos)");

        ByteBuffer buffer = StandardCharsets.UTF_8.encode(parser);

        String utf8String = StandardCharsets.UTF_8.decode(buffer).toString();

        log.info("<<FIM>> Parser refatorado com sucesso");

        return utf8String;
    }

}
