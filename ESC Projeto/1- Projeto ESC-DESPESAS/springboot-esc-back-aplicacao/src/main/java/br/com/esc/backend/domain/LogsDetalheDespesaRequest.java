package br.com.esc.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogsDetalheDespesaRequest {
    private Integer idDetalheDespesaLog;
    private Integer idDespesa;
    private Integer idDetalheDespesa;
    private Integer idOrdem;
    private Integer idFuncionario;
    private String dsLogDespesa;
}
