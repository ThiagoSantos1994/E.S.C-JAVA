package br.esc.software.restcontroller;

import br.esc.software.business.ExportadorBusiness;
import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.commons.utils.DataUtils;
import br.esc.software.commons.utils.GlobalUtils;
import br.esc.software.configuration.ConnectionSQL;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.hamcrest.CoreMatchers.anything;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doThrow;

@PrepareForTest({ExportadorBusiness.class})
public class ExportadorController {

    @InjectMocks
    br.esc.software.restcontroller.internas.ExportadorController controller;

    @Mock
    ExportadorBusiness business;

    @Mock
    ConnectionSQL connectionSQL;

    @Mock
    DataUtils utils;

    @Mock
    GlobalUtils global;

    @Before
    public void inicializacao() throws ExcecaoGlobal {
        MockitoAnnotations.initMocks(this);
        doNothing().when(connectionSQL).abrirConexao();
        doNothing().when(connectionSQL).fecharConexao();
    }

    @Test
    public void exportarComSucesso() throws Exception {
        PowerMockito.when(business.iniciarExportacao()).thenReturn("Processamento concluido! Arquivo disponibilizado em: C:\\BACKUP_JANEIRO-2000.SQL");
        assertThat(controller.iniciarExportacao(), anything("Processamento concluido! Arquivo disponibilizado em: C:\\BACKUP_JANEIRO-2000.SQL"));
    }

    @Test(expected = ExcecaoGlobal.class)
    public void exportarComFalha() throws ExcecaoGlobal {
        doThrow(new ExcecaoGlobal()).when(business).iniciarExportacao();
        business.iniciarExportacao();
    }
}
