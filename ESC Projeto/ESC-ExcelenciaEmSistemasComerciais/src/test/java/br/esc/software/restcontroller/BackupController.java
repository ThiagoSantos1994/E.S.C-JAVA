package br.esc.software.restcontroller;

import br.esc.software.business.BackupBusiness;
import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.configuration.ConnectionSQL;
import br.esc.software.configuration.ConnectionSQLBackup;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.anything;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.when;

@PrepareForTest({BackupBusiness.class})
public class BackupController {

    @InjectMocks
    private br.esc.software.restcontroller.internas.BackupController controller;

    @Mock
    private BackupBusiness business;

    @Mock
    private ConnectionSQL connectionSQL;

    @Mock
    private ConnectionSQLBackup connectionSQLBackup;

    @Before
    public void inicializacao() throws ExcecaoGlobal {
        MockitoAnnotations.initMocks(this);
        doNothing().when(connectionSQL).abrirConexao();
        doNothing().when(connectionSQLBackup).abrirConexao();
        doNothing().when(connectionSQL).fecharConexao();
        doNothing().when(connectionSQLBackup).fecharConexaoBackup();
    }

    @Test
    public void backupComSucesso() throws ExcecaoGlobal, SQLException {
        when(business.iniciarBackup()).thenReturn("Processamento concluido! Backup executado com sucesso!");
        assertThat(controller.iniciarBackup(), anything("Processamento concluido! Backup executado com sucesso!"));
    }

    @Test(expected = ExcecaoGlobal.class)
    public void backupComFalha() throws ExcecaoGlobal, SQLException {
        doThrow(new ExcecaoGlobal()).when(business).iniciarBackup();
        controller.iniciarBackup();
    }
}
