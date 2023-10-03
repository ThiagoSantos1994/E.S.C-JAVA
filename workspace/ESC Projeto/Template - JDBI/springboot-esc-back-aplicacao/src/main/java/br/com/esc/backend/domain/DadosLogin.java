package br.com.esc.backend.domain;

import lombok.Data;

@Data
public class DadosLogin {
    private Integer id_Login;
    private String ds_NomeLogin;
    private String ds_SenhaLogin;
    private String tp_PermiteExcluirPedidos;
    private String tp_UsuarioBloqueado;
    private String tp_FuncionarioExcluido;
    private Integer tp_GravaSenha;
}
