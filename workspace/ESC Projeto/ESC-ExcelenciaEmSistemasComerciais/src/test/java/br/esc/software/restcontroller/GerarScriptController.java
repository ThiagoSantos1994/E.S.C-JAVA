package br.esc.software.restcontroller;

import br.esc.software.business.ExportadorBusiness;
import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.configuration.ConnectionSQL;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.when;

public class GerarScriptController {

    @InjectMocks
    br.esc.software.restcontroller.internas.GerarScriptController controller;

    @Mock
    ExportadorBusiness business;

    @Mock
    ConnectionSQL connectionSQL;

    @Before
    public void doInit() throws ExcecaoGlobal {
        MockitoAnnotations.initMocks(this);
        doNothing().when(connectionSQL).abrirConexao();
        doNothing().when(connectionSQL).fecharConexao();
    }

    @Test
    public void gerarScriptSQL() throws Exception {
        when(business.iniciarExportacao()).thenReturn("OK ScriptGerado");
    }

    @Test(expected = Exception.class)
    public void falhaGerarScriptSQL() throws Exception {
        doThrow(new Exception()).when(business).gerarScriptImplantacao();
        controller.gerarScriptCreate();
    }
}
