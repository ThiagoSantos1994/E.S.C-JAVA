package br.esc.software.rs;

import java.util.ArrayList;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;

import br.esc.software.domain.ColunasSQL;
import br.esc.software.domain.TabelasSQL;
import br.esc.software.integration.ExportadorSQL;
import br.esc.software.persistence.ExportadorDao;

public class GerarScriptApiTest {

	@InjectMocks
	ExportadorSQL exportadorSQL;
	
	@Mock
	ExportadorDao dao;
	
	@Before
	public void doInit() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void gerarScriptSQL() throws Exception {
		PowerMockito.when(dao.getListaTabelas()).thenReturn(getTabelas());
		PowerMockito.when(dao.getListaColunas("tbd_Vendas")).thenReturn(getColunas());
		
		exportadorSQL.montaScriptImplantacao();
	}
	
	@Test
	public void falhaGerarScriptSQl() throws Exception {
		/**
		 * Neste case, a falha gerada será devido a simulacao de um erro na conexao DAO
		 */
		try {
			PowerMockito.when(dao.getListaTabelas()).thenReturn(null);
			exportadorSQL.montaScriptImplantacao();
			Assert.fail();
		} catch (Exception e) {
			Assert.assertThat(e.getMessage(), CoreMatchers.containsString("Erro metodo MontaScriptImplantacao ->>>"));
		}
	}
	
	private ArrayList<TabelasSQL> getTabelas() {
		TabelasSQL tabela = new TabelasSQL();
		tabela.setNomeTabela("tbd_Vendas");
 
		ArrayList<TabelasSQL> list = new ArrayList<TabelasSQL>();
		list.add(tabela);
		return list;
	}
	
	private ArrayList<ColunasSQL> getColunas(){
		ColunasSQL colunas = new ColunasSQL();
		colunas.setNomeColuna("id");
		colunas.setTamanhoColuna("10");
		colunas.setTipoColuna("CHAR");
		
		ArrayList<ColunasSQL> arrayList = new ArrayList<ColunasSQL>();
		arrayList.add(colunas);
		return arrayList;
	}
}
