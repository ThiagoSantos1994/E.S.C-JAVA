package br.com.esc.backend.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DespesasParceladasQuitacaoDAO {
    private Integer qtde_Parcelas;
    private BigDecimal vl_Parcelas;
}
