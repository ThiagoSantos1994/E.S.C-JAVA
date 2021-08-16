package br.com.esc.back.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String usuario;
    private String senha;
}
