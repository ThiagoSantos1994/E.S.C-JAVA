package br.com.esc.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TituloDespesa {
    private Integer idDespesa;
    private Integer idDetalheDespesa;
    private Integer idConsolidacao;
    private String tituloDespesa;
}
