package br.esc.software.repository.aplicacao;

import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.domain.aplicacao.LoginResponse;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import static br.esc.software.configuration.ConnectionSQL.Select_Table;

@Repository
public class LoginRepository {

    private ResultSet RSAdo;

    /*Este metodo valida se existe o login na base e, se esta bloqueado - 25/03/21*/
    public LoginResponse autenticar(String usuario, String senha) throws ExcecaoGlobal, SQLException {
        LoginResponse response = new LoginResponse();

        RSAdo = Select_Table("SELECT id_Login, ds_NomeLogin, tp_UsuarioBloqueado, tp_PermiteExcluirPedidos, tp_UsuarioBloqueado FROM tbd_Login " +
                "WHERE ds_NomeLogin ='" + usuario + "' " +
                "AND ds_SenhaLogin = '" + senha + "'");

        while (RSAdo.next()) {
            response.setId_Login(RSAdo.getInt("id_Login"));
            response.setDs_NomeLogin(RSAdo.getString("ds_NomeLogin"));
            response.setTp_PermiteExcluirPedidos(RSAdo.getString("tp_PermiteExcluirPedidos"));
            response.setTp_UsuarioBloqueado(RSAdo.getString("tp_UsuarioBloqueado"));
        }
        RSAdo.close();

        return response;
    }

    public LoginResponse obterDadosUsuario(String id) throws ExcecaoGlobal, SQLException {
        LoginResponse response = new LoginResponse();

        RSAdo = Select_Table("SELECT id_Login, ds_NomeLogin, tp_UsuarioBloqueado, tp_PermiteExcluirPedidos, tp_UsuarioBloqueado FROM tbd_Login " +
                "WHERE id_Login = " + Integer.parseInt(id));

        while (RSAdo.next()) {
            response.setId_Login(RSAdo.getInt("id_Login"));
            response.setDs_NomeLogin(RSAdo.getString("ds_NomeLogin"));
            response.setTp_PermiteExcluirPedidos(RSAdo.getString("tp_PermiteExcluirPedidos"));
            response.setTp_UsuarioBloqueado(RSAdo.getString("tp_UsuarioBloqueado"));
        }
        RSAdo.close();

        return response;
    }
}
