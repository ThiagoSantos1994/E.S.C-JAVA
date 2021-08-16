package br.com.esc.login.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrocarSenhaRequest {
    private String usuario;
    private String senhaAtual;
    private String senhaNova;
}
