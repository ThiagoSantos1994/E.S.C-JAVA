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
public class ConfiguracaoLancamentosResponse {
    private Integer dataViradaMes;
    private Integer mesReferencia;
    private Integer anoReferencia;
    private boolean bViradaAutomatica;
    private Integer idFuncionario;
    private Integer qtdeLembretes;
    private Integer qtdeAcessos;
    private List<String> anosReferenciaFiltro;
}
