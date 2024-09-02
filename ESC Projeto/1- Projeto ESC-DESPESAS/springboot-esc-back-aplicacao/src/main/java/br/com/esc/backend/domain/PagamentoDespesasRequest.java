package br.com.esc.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PagamentoDespesasRequest {
    private Integer idDespesa;
    private Integer idDetalheDespesa;
    private Integer idDespesaParcelada;
    private Integer idConsolidacao;
    private Integer idParcela;
    private Integer idOrdem;
    private Integer idFuncionario;
    private String vlTotal;
    private String vlTotalPago;
    private String tpStatus;
    private String dsObservacoes;
    private String dsObservacoesComplementar;
    private Boolean isProcessamentoAdiantamentoParcelas;
}
