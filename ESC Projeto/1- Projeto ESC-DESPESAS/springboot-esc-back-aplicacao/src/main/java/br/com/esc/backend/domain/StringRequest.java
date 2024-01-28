package br.com.esc.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StringRequest {
    /*Classe generica para ser utilizada em operacoes que recebe um campo do tipo STRING*/
    private String observacaoPagamento;
}
