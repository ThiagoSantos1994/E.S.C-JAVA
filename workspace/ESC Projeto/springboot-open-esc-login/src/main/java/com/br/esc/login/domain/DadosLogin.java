package com.br.esc.login.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DadosLogin {
    private Integer id_Login;
    private String ds_NomeLogin;
    private String ds_SenhaLogin;
    private String tp_PermiteExcluirPedidos;
    private String tp_UsuarioBloqueado;
    private String tp_FuncionarioExcluido;
    private Integer tp_GravaSenha;
}
