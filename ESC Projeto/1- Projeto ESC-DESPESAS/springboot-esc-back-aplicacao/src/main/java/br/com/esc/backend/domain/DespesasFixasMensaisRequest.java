package br.com.esc.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DespesasFixasMensaisRequest {
    private Integer idDespesa;
    private String dsDescricao;
    private String vlTotal;
    private String tpStatus;
    private Integer idOrdem;
    private String dsMes;
    private String dsAno;
    private Integer idFuncionario;
    private String tpFixasObrigatorias;
    private String tpDespesaDebitoCartao;
}
