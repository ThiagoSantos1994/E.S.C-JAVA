package br.com.esc.backend.repository;

import br.com.esc.backend.domain.DadosLogin;

import java.util.List;

public interface AplicacaoRepository {

    List<DadosLogin> obterDadosLogin(Integer idLogin);

}
