package br.com.esc.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioDespesasParceladasQuitacaoDAO {
    private String dsTituloDespesaParcelada;
    private String valorDespesa;
}
