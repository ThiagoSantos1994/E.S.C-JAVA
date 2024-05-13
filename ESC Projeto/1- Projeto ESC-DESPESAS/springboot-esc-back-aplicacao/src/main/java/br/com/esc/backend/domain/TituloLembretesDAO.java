package br.com.esc.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TituloLembretesDAO {
    private Integer idLembrete;
    private Integer idFuncionario;
    private String dsTituloLembrete;
}
