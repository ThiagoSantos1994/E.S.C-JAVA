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
public class DespesasFixasMensaisDAO {
    private Integer idDespesa;
    private String dsDescricao;
    private String vlTotal;
    private BigDecimal dVlTotal;
    private String tpStatus;
    private String dsMes;
    private String dsAno;
    private Integer idFuncionario;
    private Integer idOrdem;
    private String tpFixasObrigatorias;
    private String tpDespesaDebitoCartao;
}
