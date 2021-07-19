package br.com.unip.repository;
 
 
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.unip.repository.entity.PessoaEntity;

public interface PessoaRepository extends JpaRepository<PessoaEntity, Integer>{
 
}