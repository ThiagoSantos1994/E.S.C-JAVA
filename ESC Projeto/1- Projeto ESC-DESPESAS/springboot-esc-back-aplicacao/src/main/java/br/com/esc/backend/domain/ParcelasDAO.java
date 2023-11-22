package br.com.esc.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParcelasDAO {
    private Integer idDespesaParcelada;
    private Integer idParcela;
    private String nrParcela;
    private String vlParcela;
    private String vlAmortizacao;
    private String vlDesconto;
    private Integer idDespesa;
    private Integer idDetalheDespesa;
    private Integer idFuncionario;
    private String dsDataVencimento;
    private String dsObservacoes;
    private String tpBaixado;
    private String tpQuitado;
    private String tpParcelaAdiada;
    private String tpParcelaAmortizada;
}
