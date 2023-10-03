package br.com.esc.back.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Properties;

public abstract class GlobalUtils {
    private static final Logger logger = LoggerFactory.getLogger(GlobalUtils.class);

    public static String obterPercentual(Double valorLimite, Double valorAtual) throws Exception {
        logger.info("Chamando metodo obterPercentual");
        try {
            if (valorAtual <= 0d) {
                return "0%";
            }

            Double calculoPorcentagem = ((valorLimite / valorAtual));
            calculoPorcentagem = (calculoPorcentagem * 100);

            DecimalFormat decimal = new DecimalFormat("###,#00.00");
            return decimal.format(calculoPorcentagem).concat("%");
        } catch (Exception ex) {
            throw new Exception("Ocorreu um erro ao obterPercentual >>> " + ex);
        }
    }

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
