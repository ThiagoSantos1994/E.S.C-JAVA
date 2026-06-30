package br.com.esc.backend.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BackupRepository {

    List<String> getListaTabelasBaseDados();

    void insertDadosBaseBackup(String baseBackup, String basePrincipal);

    void deleteDadosTabela(String tabela);

    void setIdentityInsert(String tabela, boolean enable);

    List<String> getColunasTabela(String nomeTabela);

    void insertDadosComColunas(String baseBackup, String basePrincipal, String colunas);
}
