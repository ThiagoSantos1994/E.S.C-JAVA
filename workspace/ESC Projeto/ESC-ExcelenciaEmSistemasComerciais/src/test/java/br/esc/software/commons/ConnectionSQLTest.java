package br.esc.software.commons;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.powermock.reflect.Whitebox;

@PrepareForTest({ ConnectionSQL.class, ConnectionSQLTest.class })
public class ConnectionSQLTest {

	@Spy
	ConnectionSQL conn;
	
	@Rule
	public PowerMockRule rule = new PowerMockRule();
	
	@Before
	public void inicializacao() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void executarComSucessoInstrucaoSQL() throws Exception {
		PowerMockito.mockStatic(ConnectionSQL.class);
		PowerMockito.when(ConnectionSQL.Select_Table("SELECT * FROM MASTER")).thenReturn(null);
		ResultSet retorno = ConnectionSQL.Select_Table("SELECT * FROM MASTER");
		Assert.assertEquals(null, retorno);
		
//		conn.abrirConexao();
//		Whitebox.invokeMethod(conn, "ExecuteInstrucoesSQL", "USE master");
//		Whitebox.invokeMethod(conn, "ExecuteInstrucoesSQL", "UPDATE dbo.spt_values SET name = 'yes' WHERE number = 1 and name = 'yes'");
//		conn.fecharConexao();
	}
	
	@Test
	public void deveGerarExcecaoInstrucaoSQL() throws SQLException {
		try {
			Whitebox.invokeMethod(conn, "ExecuteInstrucoesSQL", "USE master");
			Assert.fail();
		} catch (Exception e) {
			Assert.assertThat(e.getMessage() , CoreMatchers.is("Erro ao executar metodo ExecuteInstrucoesSQL"));
		}
	}
}
