package br.esc.software.integration;

import java.sql.SQLException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ExportadorSQLTest {

	private ExportadorSQL exportadorSQL;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void doSetup() {
		exportadorSQL = new ExportadorSQL();
	}
	
	@Ignore
	@Test
	public void iniciarProcessamentoComSucesso() throws SQLException, Exception {
		exportadorSQL.IniciarProcessamento("2499", "2", "1813268802", "ExportacaoSQL 2499 2 1813268802");
	}

	@Ignore
	@Test
	public void iniciarProcessamentoComFalha() throws SQLException, Exception {
		exception.expect(Exception.class);
		exportadorSQL.IniciarProcessamento(null, null, null, null);
	}
}
