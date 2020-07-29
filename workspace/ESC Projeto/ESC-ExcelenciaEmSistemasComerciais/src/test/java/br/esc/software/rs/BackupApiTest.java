package br.esc.software.rs;

import java.sql.SQLException;
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

import br.esc.software.business.BackupSQLBusiness;
import br.esc.software.commons.ConnectionSQLBackup;
import br.esc.software.domain.TabelasSQL;
import br.esc.software.exceptions.ExcecaoGlobal;
import br.esc.software.persistence.BackupDao;
import br.esc.software.persistence.ExportadorDao;

@PrepareForTest({BackupSQLBusiness.class})
public class BackupApiTest {

	@InjectMocks
	private BackupSQLBusiness servico;
	
	@Mock
	private ExportadorDao exportadorDao;
	
	@Mock
	private BackupDao dao;
	
	@Mock
	private ConnectionSQLBackup connectionSQLBackup;
	
	@Before
	public void inicializacao() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void backupComSucesso() throws ExcecaoGlobal, SQLException {
		PowerMockito.when(exportadorDao.getListaTabelas()).thenReturn(getTabelas());
		PowerMockito.when(dao.excluirDadosTabelas(Mockito.anyString())).thenReturn(true);
		PowerMockito.when(dao.inserirDadosTabelas(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		
		Mockito.doNothing().when(connectionSQLBackup).fecharConexaoBackup();
		
		Assert.assertThat(servico.iniciarBackup(), CoreMatchers.is("Processamento concluido! Backup executado com sucesso!"));
	}

	@Test
	public void backupComFalhaExcluirDados() throws ExcecaoGlobal, SQLException {
		Mockito.when(exportadorDao.getListaTabelas()).thenReturn(getTabelas());
		Mockito.doReturn(false).when(dao).excluirDadosTabelas(Mockito.anyString());
		Mockito.doReturn(true).when(dao).inserirDadosTabelas(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		Mockito.doNothing().when(connectionSQLBackup).fecharConexaoBackup();
		Assert.assertThat(servico.iniciarBackup(), CoreMatchers.is("[WARN] Processamento concluido com FALHA! Backup parcial executado com sucesso!"));
	}

	@Test
	public void backupComFalhaInserirDados() throws ExcecaoGlobal, SQLException {
		Mockito.when(exportadorDao.getListaTabelas()).thenReturn(getTabelas());
		Mockito.doReturn(true).when(dao).excluirDadosTabelas(Mockito.anyString());
		Mockito.doReturn(false).when(dao).inserirDadosTabelas(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		Mockito.doNothing().when(connectionSQLBackup).fecharConexaoBackup();
		
		Assert.assertThat(servico.iniciarBackup(), CoreMatchers.is("[WARN] Processamento concluido com FALHA! Backup parcial executado com sucesso!"));
	}

	@Test(expected = ExcecaoGlobal.class)
	public void backupComErro() throws SQLException, ExcecaoGlobal {
		Mockito.when(exportadorDao.getListaTabelas()).thenReturn(null);
		servico.iniciarBackup();
	}
	
	private ArrayList<TabelasSQL> getTabelas() {
		TabelasSQL tabela = new TabelasSQL();
		tabela.setNomeTabela("tbd_Vendas");
 
		ArrayList<TabelasSQL> list = new ArrayList<TabelasSQL>();
		list.add(tabela);
		return list;
	}
}
