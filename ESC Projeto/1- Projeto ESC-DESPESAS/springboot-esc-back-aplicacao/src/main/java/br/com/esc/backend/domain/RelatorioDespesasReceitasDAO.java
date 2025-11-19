package br.com.esc.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioDespesasReceitasDAO {
    private BigDecimal[] receitas;
    private BigDecimal[] despesas;
    private String[] meses;
}
