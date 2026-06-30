package br.com.esc.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BooleanResponse {
    /*Classe generica para ser utilizada em operacoes que retorne um campo do tipo Boolean*/
    private Boolean isValid;

    /*private Boolean isTituloJaExistente;
    private Boolean isDespesaExistente;
    private Boolean isSessaoValida;*/
}
