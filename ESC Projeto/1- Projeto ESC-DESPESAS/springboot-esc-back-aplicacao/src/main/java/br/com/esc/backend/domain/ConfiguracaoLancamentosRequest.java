package br.com.esc.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfiguracaoLancamentosRequest {
    private Integer dataViradaMes;
    private Integer mesReferencia;
    private boolean bViradaAutomatica;
    private char viradaAutomatica;
    private Integer idFuncionario;
}
