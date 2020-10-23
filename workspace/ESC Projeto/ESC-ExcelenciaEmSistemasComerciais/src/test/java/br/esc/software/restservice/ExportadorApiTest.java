package br.esc.software.restservice;

import java.util.ArrayList;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import br.esc.software.business.ExportadorSQLBusiness;
import br.esc.software.commons.utils.DataUtils;
import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.commons.utils.GlobalUtils;
import br.esc.software.domain.exportador.TabelasSQL;
import br.esc.software.integration.ExportadorSQLImpl;
import br.esc.software.repository.exportador.ExportadorDao;

@PrepareForTest({ ExportadorSQLBusiness.class})
public class ExportadorApiTest {

	@InjectMocks
	ExportadorSQLBusiness servico;
	
	@Mock
	ExportadorDao dao;

	@Mock
	ExportadorSQLImpl exportador;
	
	@Mock
	DataUtils utils;
	
	@Mock
	GlobalUtils global;
	
	@Before
	public void inicializacao() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void exportarComSucesso() throws Exception {
		PowerMockito.when(utils.DataAtual()).thenReturn("01/01/2000 00:00:00");
		PowerMockito.when(utils.MesAtual()).thenReturn("01");
		PowerMockito.when(utils.AnoAtual()).thenReturn("2000");
		PowerMockito.when(utils.MesNomeAtual()).thenReturn("JANEIRO");

//		PowerMockito.when(dao.getDiretorioDestinoArquivo()).thenReturn("C:\\");
		PowerMockito.when(dao.getListaTabelas()).thenReturn(getNomeTabela());
		PowerMockito.when(dao.montaColunaTabela("tbd_Vendas")).thenReturn("id_Pedido");
		
		PowerMockito.when(exportador.montaScriptImplantacao()).thenReturn(Mockito.any());
		PowerMockito.when(global.EscreverArquivoTexto(Mockito.any(), "C:\\BACKUP_JANEIRO-2000.SQL")).thenReturn(true);
		
		PowerMockito.when(exportador.gerarNomeArquivo()).thenReturn("C:\\");
		
		Assert.assertThat(servico.iniciarExportacao(), CoreMatchers.anything("Processamento concluido! Arquivo disponibilizado em: C:\\BACKUP_JANEIRO-2000.SQL"));
	}
	
	@Test(expected = ExcecaoGlobal.class)
	public void exportarComFalha() throws ExcecaoGlobal {
		// Forma elegante
		ExportadorSQLBusiness sqlBusiness = new ExportadorSQLBusiness();
		sqlBusiness.iniciarExportacao();
	}
	
	@Test
	public void exportarComFalhaConexaoDAO() {
		//Forma robusta
		ExportadorSQLBusiness sqlBusiness = new ExportadorSQLBusiness();
		
		try {
			sqlBusiness.iniciarExportacao();
			Assert.fail();
		} catch (ExcecaoGlobal e) {
			Assert.assertThat(e.getMessage(),CoreMatchers.is("Ocorreu um erro inesperado na classe ExportadorSQL, processamento interrompido -> java.lang.NullPointerException"));
		}
	}
	
	private ArrayList<TabelasSQL> getNomeTabela() {
		TabelasSQL tabela = new TabelasSQL();
		tabela.setNomeTabela("tbd_Vendas");
 
		ArrayList<TabelasSQL> list = new ArrayList<TabelasSQL>();
		list.add(tabela);
		return list;
	}
}
