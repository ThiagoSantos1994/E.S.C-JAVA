package br.com.esc.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DespesasParceladasResponse {
    private List<DespesaParceladaDAO> despesas;
    private Integer sizeDespesasParceladasVB;
}
