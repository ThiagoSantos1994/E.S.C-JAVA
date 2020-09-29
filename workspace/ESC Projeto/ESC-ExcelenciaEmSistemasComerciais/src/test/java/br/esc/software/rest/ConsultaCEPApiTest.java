package br.esc.software.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import br.esc.software.business.ConsultaCEPBusiness;
import br.esc.software.commons.ExcecaoGlobal;

@PrepareForTest({ ConsultaCEPApi.class })
public class ConsultaCEPApiTest {

	@InjectMocks
	ConsultaCEPApi api;

	@Mock
	ConsultaCEPBusiness business;
	
	@Spy
	ConsultaCEPBusiness spyBusiness;
	
	@Before
	public void doInit() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void ConsultaCEPComSucesso() throws ExcecaoGlobal {
		PowerMockito.when(business.obtemDadosCepConsulta(Mockito.anyString())).thenReturn("cep_consultado_ok");
		api.consultarCep(Mockito.anyString());
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
}
