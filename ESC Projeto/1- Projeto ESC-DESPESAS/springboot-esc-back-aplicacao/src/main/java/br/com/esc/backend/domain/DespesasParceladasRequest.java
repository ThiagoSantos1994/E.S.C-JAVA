package br.com.esc.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DespesasParceladasRequest {
    private Integer idDespesaParcelada;
    private String nomeDespesaParcelada;
    private String vigenciaInicial;
    private Integer qtdeParcelas;
    private String valorParcela;
    private DetalheDespesasMensaisRequest detalheDespesasMensais;
}
