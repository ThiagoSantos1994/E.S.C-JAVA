package br.com.esc.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsolidacaoDespesasResponse {
    private Integer idConsolidacao;
    private Integer idDespesaParcelada;
    private String dsNomeDespesa;
    private String valorDespesa;
    private Integer nrParcelasAdiantadas;
    private String statusDespesa;
    private Integer idFuncionario;
    private String dataAssociacao;
}
