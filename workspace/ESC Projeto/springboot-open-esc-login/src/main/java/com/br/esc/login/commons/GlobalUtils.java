package com.br.esc.login.commons;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public abstract class GlobalUtils {

    public static Properties getProperties() throws Exception {
        Properties properties = new Properties();
        FileInputStream file = null;

        try {
            file = new FileInputStream("./src/main/resources/application.properties");
            properties.load(file);
        } catch (FileNotFoundException e) {
            throw new Exception("Erro ao obter arquivo de configuração >>>" + e.getCause());
        } catch (IOException e) {
            throw new Exception("Erro ao obter parametro do arquivo de configurações >>>" + e.getCause());
        }
        return properties;
    }

}
