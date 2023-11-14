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
public class TituloDespesaResponse {
    private List<String> tituloDespesa;
    private Integer sizeTituloDespesaVB;
}
