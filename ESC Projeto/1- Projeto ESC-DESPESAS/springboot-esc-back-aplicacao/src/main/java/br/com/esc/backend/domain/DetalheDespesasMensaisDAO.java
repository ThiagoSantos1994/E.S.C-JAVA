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
public class DetalheDespesasMensaisDAO {
    private Integer idDespesa;
    private Integer idDetalheDespesa;
    private String dsTituloDespesa;
    private String dsDescricao;
    private Integer idOrdem;
    private Integer idParcela;
    private Integer idDespesaParcelada;
    private Integer idFuncionario;
    private Integer idDespesaLinkRelatorio;
    private String vlTotal;
    private String vlTotalPago;
    private String dsObservacao;
    private String dsObservacao2;
    private String tpCategoriaDespesa;
    private String tpStatus;
    private String tpReprocessar;
    private String tpAnotacao;
    private String tpRelatorio;
    private String tpLinhaSeparacao;
}
