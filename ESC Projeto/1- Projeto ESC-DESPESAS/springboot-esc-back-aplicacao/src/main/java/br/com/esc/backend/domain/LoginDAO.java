package br.com.esc.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDAO {
    private Integer idLogin;
    private String dsLogin;
    private String dsSenha;
    private String isUsuarioBloqueado;
    private String isUsuarioExcluido;
    private String isGravaSenha;
}
