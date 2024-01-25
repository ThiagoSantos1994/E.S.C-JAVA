package br.com.esc.backend.service;

import br.com.esc.backend.domain.AutenticacaoResponse;
import br.com.esc.backend.domain.LoginDAO;
import br.com.esc.backend.domain.LoginRequest;
import br.com.esc.backend.repository.AutenticacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutenticacaoServices {

    private final AutenticacaoRepository repository;

    public AutenticacaoResponse autenticarUsuario(LoginRequest login) {
        var idLogin = -1;
        var nomeUsuario = "";
        var status = "";

        log.info("Realizando a autenticacao do LOGIN: >> Usuario: {} - Senha: {}", login.getUsuario(), login.getSenha());

        for (LoginDAO dao : repository.getLoginUsuario()) {
            var user = dao.getDsLogin();
            var password = dao.getDsSenha();
            idLogin = dao.getIdLogin();
            nomeUsuario = dao.getDsLogin();

            if (login.getUsuario().equalsIgnoreCase(user) && login.getSenha().equalsIgnoreCase(password)) {
                if (dao.getIsUsuarioBloqueado().equalsIgnoreCase("S") || dao.getIsUsuarioExcluido().equalsIgnoreCase("S")) {
                    status = "Usuario com status bloqueado ou excluido da base de dados.";
                    break;
                }
                status = "Usuario autenticado com sucesso!";
                break;
            }

            idLogin = -1;
        }

        return AutenticacaoResponse.builder()
                .idLogin(idLogin)
                .mensagem(idLogin != -1 ? status : "Usuario e\\ou senha invalidos.")
                .nomeUsuario(nomeUsuario)
                .autenticacao("Bearer -")
                .autorizado(idLogin != -1 ? true : false)
                .build();
    }
}
