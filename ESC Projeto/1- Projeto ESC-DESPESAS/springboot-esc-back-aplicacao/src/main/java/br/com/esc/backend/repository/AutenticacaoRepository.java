package br.com.esc.backend.repository;

import br.com.esc.backend.domain.LoginDAO;
import br.com.esc.backend.domain.SessaoDAO;
import br.com.esc.backend.mapper.UsuarioLoginRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutenticacaoRepository {

    List<LoginDAO> getLoginUsuario();

    SessaoDAO getHorarioLoginAuditoriaAcesso(Integer idFuncionario);

    void insertAuditoriaAcesso(Integer idFuncionario, String dataHoraLogin, String idMaquina);
}
