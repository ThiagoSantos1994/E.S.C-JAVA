package br.com.esc.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StringResponse {
    /*Classe generica para ser utilizada em operacoes que retorne um campo do tipo STRING*/
    private String mensagem;
    private String vlSubTotalDespesa;
    private String mesAno;
}
