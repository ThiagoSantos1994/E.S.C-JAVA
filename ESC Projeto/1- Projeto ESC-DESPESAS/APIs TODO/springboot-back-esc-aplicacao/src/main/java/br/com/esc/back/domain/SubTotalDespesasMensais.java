package br.com.esc.back.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SubTotalDespesasMensais {
    public Double vl_SaldoPositivoMes;
    public Double vl_SaldoPendentePagamentoMes;
    public Double vl_SaldoSubTotalMes;
}
