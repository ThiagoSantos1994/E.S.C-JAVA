package br.com.esc.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DespesaParceladaDAO {
    private Integer idDespesaParcelada;
    private String dsTituloDespesaParcelada;
    private String dsMesVigIni;
    private String dsAnoVigIni;
    private Integer nrTotalParcelas;
    private String dsVigenciaFin;
    private String vlFatura;
    private Integer idFuncionario;
    private Integer nrParcelasAdiantadas;
    private String tpBaixado;
    private String dtCadastro;
}
