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
public class ConsolidacaoDAO {
    private Integer idConsolidacao;
    private String dsTituloConsolidacao;
    private String tpBaixado;
    private String dataCadastro;
    private Integer idFuncionario;
    private List<ConsolidacaoDespesasDAO> despesasConsolidadas;
}
