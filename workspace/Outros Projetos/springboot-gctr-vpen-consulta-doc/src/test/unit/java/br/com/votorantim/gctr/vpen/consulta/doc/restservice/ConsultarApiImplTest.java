/**
 * 
 */
package br.com.votorantim.gctr.vpen.consulta.doc.restservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.votorantim.gctr.vpen.consulta.doc.representation.ConsultarDocumentoObrigatorioRequestRepresentation;

/**
 * @author resource.mlima
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConsultarApiImplTest {

	@Autowired
	ConsultarApiImpl consultarApiImpl;

	@Test
	public void consultarDocumentoObrigatorioTeste() {

		ConsultarDocumentoObrigatorioRequestRepresentation body = new ConsultarDocumentoObrigatorioRequestRepresentation();

		body.setCodigoProduto(10);

		consultarApiImpl.consultarDocumentoObrigatorio(body);

	}

}