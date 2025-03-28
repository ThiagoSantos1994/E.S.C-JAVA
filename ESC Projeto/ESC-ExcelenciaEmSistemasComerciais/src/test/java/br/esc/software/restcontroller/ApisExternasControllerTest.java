package br.esc.software.restcontroller;

import br.esc.software.business.ApisExternasBusiness;
import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.restcontroller.externas.ApisExternasController;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.http.ResponseEntity;

@PrepareForTest({ ApisExternasController.class })
public class ApisExternasControllerTest {

	@InjectMocks
    ApisExternasController api;

	@Mock
	ApisExternasBusiness business;

	@Spy
	ApisExternasBusiness spyBusiness;

	@Before
	public void doInit() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void ConsultaCEPComSucesso() throws ExcecaoGlobal {
		PowerMockito.when(business.obtemDadosCepConsulta(Mockito.anyString())).thenReturn("cep_consultado_ok");
		ResponseEntity<String> response = api.consultarCep(Mockito.anyString());
		Assert.assertThat(response.getBody(), CoreMatchers.is("cep_consultado_ok"));
	}
	
	@Test
	public void ConsultaCEPSemParametros() {
		try {
			PowerMockito.when(spyBusiness.obtemDadosCepConsulta(null)).thenCallRealMethod();
			Assert.fail();
		} catch (ExcecaoGlobal e) {
			System.out.println("Teste Falha OK");
		}
	}
	
	@Test
	public void ConsultaPrevisao() throws ExcecaoGlobal {
		PowerMockito.when(business.obtemPrevisaoTempo()).thenReturn("previsao_ok");
		ResponseEntity<String> response = api.consultarPrevisao();
		Assert.assertThat(response.getBody(), CoreMatchers.is("previsao_ok"));
	}
	
//	@Test
//	public void ConsultaPrevisaoErro() throws Exception {
//		ApisExternasImpl impl = PowerMockito.mock(ApisExternasImpl.class);
//		PowerMockito.when("impl.consultaPrevisaoTempo().URL_PREVISAO").thenReturn("ERRO");
//		spyBusiness.obtemPrevisaoTempo();
//	}
}
