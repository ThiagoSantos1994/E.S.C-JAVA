package br.com.esc.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChaveKeyDAO {
    private Integer idChaveKey;
    private Integer novaChave;
    private String dsNomeTabela;
    private String dsNomeColuna;
}
