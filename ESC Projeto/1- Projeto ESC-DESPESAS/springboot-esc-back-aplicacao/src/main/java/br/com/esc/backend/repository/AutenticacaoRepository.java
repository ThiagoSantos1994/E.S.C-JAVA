package br.com.esc.backend.repository;

import br.com.esc.backend.domain.LoginDAO;
import br.com.esc.backend.domain.SessaoDAO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutenticacaoRepository {

    List<LoginDAO> getLoginUsuario();

    Optional<LoginDAO> buscarPorUsuario(String usuario);

    SessaoDAO getHorarioLoginAuditoriaAcesso(Integer idFuncionario);

    void insertAuditoriaAcesso(Integer idFuncionario, String dataHoraLogin, String idMaquina);
}
