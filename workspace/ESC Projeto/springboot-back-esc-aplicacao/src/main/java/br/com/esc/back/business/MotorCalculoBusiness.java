package br.com.esc.back.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;

@Component
public class MotorCalculoBusiness {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public String obterPercentual(Double valorLimite, Double valorAtual) throws Exception {
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
}
