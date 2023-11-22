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
public class DetalheDespesasParceladasResponse {
    private Integer idDespesaParcelada;
    private Integer qtdeParcelas;
    private Integer qtdeParcelasPagas;
    private String parcelaAtual;
    private String valorParcelaAtual;
    private String valorTotalDespesa;
    private String valorTotalDespesaPaga;
    private String valorTotalDespesaPendente;
    private String isDespesaComParcelaAmortizada;
    private String isDespesaComParcelaAdiantada;
    private String despesaVinculada;
    private DespesaParceladaDAO despesas;
    private List<ParcelasDAO> parcelas;
}
