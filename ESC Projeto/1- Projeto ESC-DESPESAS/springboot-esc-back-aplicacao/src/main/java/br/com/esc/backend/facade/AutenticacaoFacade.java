package br.com.esc.backend.facade;

import br.com.esc.backend.domain.AutenticacaoResponse;
import br.com.esc.backend.domain.BooleanResponse;
import br.com.esc.backend.domain.LoginRequest;
import br.com.esc.backend.domain.StringResponse;
import br.com.esc.backend.service.AutenticacaoServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AutenticacaoFacade {

    private final AutenticacaoServices autenticacaoServices;

    public AutenticacaoResponse autenticarUsuario(LoginRequest request) {
        return autenticacaoServices.autenticarUsuario(request);
    }

    public BooleanResponse validaSessaoUsuario(Integer idFuncionario) {
        return autenticacaoServices.validarSessaoUsuario(idFuncionario);
    }
}
