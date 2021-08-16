package br.com.esc.back.business;

import br.com.esc.back.domain.DadosLogin;
import br.com.esc.back.repository.LoginRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.Integer.parseInt;


@Component
public class LoginBusiness {

    @Autowired
    LoginRepository repository;

    private static final Logger log = LoggerFactory.getLogger(LoginBusiness.class);

    public DadosLogin obterDados(String id_Funcionario) throws Exception {
        log.info("Obtendo dados do usuario...");

        List<DadosLogin> dadosLogins = repository.obterDadosLogin(parseInt(id_Funcionario));

        return parserDados(dadosLogins);
    }

    private DadosLogin parserDados(List<DadosLogin> listaDados) {
        log.info("Realizando parser dos dados do usuario...");
        DadosLogin dadosLogin = new DadosLogin();

        for (DadosLogin dados : listaDados) {
            dadosLogin.setId_Login(dados.getId_Login());
            dadosLogin.setDs_NomeLogin(dados.getDs_NomeLogin());
            dadosLogin.setDs_SenhaLogin("*******");
            dadosLogin.setTp_PermiteExcluirPedidos(dados.getTp_PermiteExcluirPedidos());
            dadosLogin.setTp_UsuarioBloqueado(dados.getTp_UsuarioBloqueado());
            dadosLogin.setTp_FuncionarioExcluido(dados.getTp_FuncionarioExcluido());
            dadosLogin.setTp_GravaSenha(dados.getTp_GravaSenha());
        }

        return dadosLogin;
    }
}
