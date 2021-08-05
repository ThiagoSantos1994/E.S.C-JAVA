package com.br.esc.login.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private Integer idUsuario;
    private boolean autorizado;
    private String mensagem;
    private String autenticacao;

}
