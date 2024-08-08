package br.com.esc.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaDespesasDAO {
    private String nomeCategoria;
    private String vlDespesa;
}
