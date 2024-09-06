package br.com.esc.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsolidacaoDespesasRequest {
    private Integer idConsolidacao;
    private Integer idDespesaParcelada;
    private Integer idFuncionario;
}
