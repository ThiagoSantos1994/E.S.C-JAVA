package br.esc.software.domain.aplicacao;

import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;

public class LoginResponse {

    private boolean isUsuario = false;
    private String message = "Usuario e/ou senha inválidos!";

    private int id_Login;
    private String ds_NomeLogin;
    private boolean tp_PermiteExcluirPedidos = false;
    private boolean tp_UsuarioBloqueado = false;

    public boolean isUsuario() {
        return isUsuario;
    }

    public String getMessage() {
        return message;
    }

    public int getId_Login() {
        return id_Login;
    }

    public void setId_Login(int id_Login) {
        this.id_Login = id_Login;
        this.isUsuario = true;
    }

    public String getDs_NomeLogin() {
        return ds_NomeLogin;
    }

    public void setDs_NomeLogin(String ds_NomeLogin) {
        this.ds_NomeLogin = ds_NomeLogin;
    }

    public boolean isTp_PermiteExcluirPedidos() {
        return tp_PermiteExcluirPedidos;
    }

    public void setTp_PermiteExcluirPedidos(String tp_PermiteExcluirPedidos) {
        if (tp_PermiteExcluirPedidos.equals("S")) {
            this.tp_PermiteExcluirPedidos = true;
        }
    }

    public boolean isTp_UsuarioBloqueado() {
        return tp_UsuarioBloqueado;
    }

    public void setTp_UsuarioBloqueado(String tp_UsuarioBloqueado) {
        if (tp_UsuarioBloqueado.equals("S")) {
            this.tp_UsuarioBloqueado = true;
        }
    }

    public void setMensagemValidacao(String mensagem) {
        this.message = mensagem;
    }

}
