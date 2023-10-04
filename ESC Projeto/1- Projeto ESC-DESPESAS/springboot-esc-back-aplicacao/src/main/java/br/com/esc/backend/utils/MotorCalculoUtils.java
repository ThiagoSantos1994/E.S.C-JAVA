package br.com.esc.backend.utils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
public class MotorCalculoUtils {

    public static BigDecimal calcularReceitaPositivaMes(List<BigDecimal> listReceitas) {
        try {
            BigDecimal calculo = new BigDecimal(0);
            for (BigDecimal receita : listReceitas) {
                if (calculo.intValue() == 0) {
                    calculo = receita;
                } else {
                    calculo = (calculo.subtract(receita));
                }
            }
            return calculo;
        } catch (Exception e) {
            log.error("[MOTOR_CALCULO] >> Ocorreu um erro ao reaizar ao calcularReceitaPositivaMes >> {}", e.getMessage());
            return BigDecimal.ONE;
        }

    }

    public static BigDecimal calculaPorcentagem(BigDecimal valorLimite, BigDecimal valorCalculado) {
        return valorCalculado.divide(valorLimite, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
    }

    public static BigDecimal calculaPorcentagem(BigDecimal valorLimite, BigDecimal valorCalculado, Integer scale) {
        return valorCalculado.divide(valorLimite, scale, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
    }
}
