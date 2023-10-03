package br.com.esc.backend.service;

import br.com.esc.backend.domain.DadosLogin;
import br.com.esc.backend.repository.AplicacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class AplicacaoService {

    private final AplicacaoRepository repository;

    public List<DadosLogin> obterDadosLogin(Integer idLogin) {
        log.info("Consultado dados login >> filtro: id_Login: {}", idLogin);

        var result = repository.obterDadosLogin(idLogin);

        log.info("Consultado dados login >> response: {}", result);
        
        return result;
    }
}
