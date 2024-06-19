package br.com.esc.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StringResponse {
    /*Classe generica para ser utilizada em operacoes que retorne um campo do tipo STRING*/
    private String mesAno;
    private String mensagem;
    private String observacoes;
    private String nomeDespesaParcelada;
    private String nomeLembrete;
    private String vlSubTotalDespesa;
    private String vlDespesaParcelada;
    private String vlCalculo;
    private String relatorioDespesas;
    private Boolean isTituloJaExistente;
    private Boolean isDespesaExistente;
    private Boolean isSessaoValida;
}
