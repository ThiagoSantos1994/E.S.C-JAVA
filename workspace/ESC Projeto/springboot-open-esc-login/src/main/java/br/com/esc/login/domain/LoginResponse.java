package br.com.esc.login.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private Integer id_Login;
    private boolean autorizado;
    private String mensagem;
    private String autenticacao;

}
