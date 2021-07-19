package com.br.unip.dados.uteis.apis.repository;

import com.br.unip.dados.uteis.apis.repository.entity.PessoaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CadastroRepository extends JpaRepository<PessoaEntity, Integer> {
}
