package br.com.esc.back.repository;

import br.com.esc.back.domain.DadosLogin;

import java.util.List;

public interface EscAplicacaoRepository {

    List<DadosLogin> obterDadosLogin(Integer idLogin);

}
