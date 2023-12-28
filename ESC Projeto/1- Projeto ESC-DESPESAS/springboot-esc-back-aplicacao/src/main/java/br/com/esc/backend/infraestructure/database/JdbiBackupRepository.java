package br.com.esc.backend.infraestructure.database;

import br.com.esc.backend.repository.BackupRepository;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@UseClasspathSqlLocator
public interface JdbiBackupRepository extends BackupRepository {

    @Override
    @SqlQuery
    List<String> getListaTabelasBaseDados();

    @Override
    @SqlUpdate
    void insertDadosBaseBackup(@Define("baseBackup") String baseBackup, @Define("basePrincipal") String basePrincipal);

    @Override
    @SqlUpdate
    void deleteDadosTabela(@Define("tabela") String tabela);
}
