package br.com.esc.backend.utils;

import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static br.com.esc.backend.utils.ObjectUtils.isNull;
import static br.com.esc.backend.utils.VariaveisGlobais.VALOR_ZERO;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
public class MotorCalculoUtils {
    private static DecimalFormat decimalFormat = new DecimalFormat("###,###,##0.00");

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
        if (valorLimite.compareTo(BigDecimal.ZERO) == 0 || valorCalculado.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return valorCalculado.divide(valorLimite, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
    }

    public static BigDecimal calculaPorcentagem(BigDecimal valorLimite, BigDecimal valorCalculado, Integer scale) {
        if (valorLimite.compareTo(BigDecimal.ZERO) == 0 || valorCalculado.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return valorCalculado.divide(valorLimite, scale, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
    }

    public static BigDecimal convertStringToDecimal(String valor) {
        if (valor.trim().equalsIgnoreCase("-") || isEmpty(valor)) {
            return BigDecimal.ZERO;
        }

        var dValor = 0d;
        if(valor.contains(".") && valor.contains(",")) {
            dValor = Double.parseDouble(valor.trim().replace("- ", "-").replace(".","").replace(",", "."));
        } else {
            dValor = Double.parseDouble(valor.trim().replace("- ", "-").replace(",", "."));
        }

        if (dValor <= 0 || isNull(dValor)) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(dValor);
    }

    public static String convertDecimalToString(BigDecimal valor) {
        if (isEmpty(valor)) {
            return VALOR_ZERO;
        }

        return decimalFormat.format(valor);
    }

    public static String convertToMoedaBR(BigDecimal valor) {
        Locale localeBR = new Locale("pt", "BR");
        NumberFormat dinheiro = NumberFormat.getCurrencyInstance(localeBR);

        return dinheiro.format(valor).replace("R$", "");
    }
}
