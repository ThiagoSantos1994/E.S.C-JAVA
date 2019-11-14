package br.esc.software;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EscExcelenciaEmSistemasComerciaisApplicationTests {
	
	@Ignore
	@Test
	public void testeMainComArgumentos() {
		EscExcelenciaEmSistemasComerciaisApplication application = new EscExcelenciaEmSistemasComerciaisApplication();
		application.main(null);
	}
}
