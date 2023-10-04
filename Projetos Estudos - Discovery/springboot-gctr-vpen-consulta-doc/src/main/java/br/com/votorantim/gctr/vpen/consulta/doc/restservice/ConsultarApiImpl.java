/**
 * 
 */
package br.com.votorantim.gctr.vpen.consulta.doc.restservice;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.votorantim.gctr.vpen.consulta.doc.api.ConsultarApi;
import br.com.votorantim.gctr.vpen.consulta.doc.business.ConsultarBussinesService;
import br.com.votorantim.gctr.vpen.consulta.doc.domain.Documentos;
import br.com.votorantim.gctr.vpen.consulta.doc.representation.ConsultarDocumentoObrigatorioRequestRepresentation;
import br.com.votorantim.gctr.vpen.consulta.doc.representation.ConsultarDocumentoObrigatorioResponseRepresentation;
import br.com.votorantim.gctr.vpen.consulta.doc.representation.DocumentosRepresentation;

/**
 * @author resource.mlima
 *
 */

@RestController
@RequestMapping("/v1")
public class ConsultarApiImpl implements ConsultarApi {

	private final static Logger log = LoggerFactory.getLogger(ConsultarApiImpl.class);

	@Autowired
	ConsultarBussinesService consultarBussinesService;

	public ResponseEntity<ConsultarDocumentoObrigatorioResponseRepresentation> consultarDocumentoObrigatorio(ConsultarDocumentoObrigatorioRequestRepresentation body) {

		log.info("ConsultarApiImpl >>> consultarDocumentoObrigatorio");

		ConsultarDocumentoObrigatorioResponseRepresentation response = new ConsultarDocumentoObrigatorioResponseRepresentation();

		try {

			List<Documentos> documentos = consultarBussinesService.consultarDocumentoObrigatorio(body.getCodigoProduto(), body.getCodigoParceiroComercial(), body.getCodigoModalidade(), body.getCodigoTipoPessoa(), body.getDataProcessamentoProposta());

			response = parseListaDeDocumentos(documentos);

			return new ResponseEntity<>(response, HttpStatus.ACCEPTED);

		} catch (Exception e) {

			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		}
	}

	private ConsultarDocumentoObrigatorioResponseRepresentation parseListaDeDocumentos(List<Documentos> documentos) {

		log.info("ConsultarApiImpl >>> parseListaDeDocumentos");

		ConsultarDocumentoObrigatorioResponseRepresentation response = new ConsultarDocumentoObrigatorioResponseRepresentation();

		List<DocumentosRepresentation> listaDocumento = new ArrayList<DocumentosRepresentation>();

		for (Documentos documento : documentos) {

			DocumentosRepresentation documentosRepresentation = new DocumentosRepresentation();

			documentosRepresentation.setCodigoDocumento(documento.getCodigoDocumento());
//			documentosRepresentation.setIndicadorAtivo(documento.getIndicadorAtivo());
//			documentosRepresentation.setIndicadorObrigatoriedade(documento.getIndicadorObrigatoriedade());
//			documentosRepresentation.setNomeDocumento(documento.getNomeDocumento());

			listaDocumento.add(documentosRepresentation);
		}

		response.setListaDocumento(new ArrayList<DocumentosRepresentation>());
		response.setListaDocumento(listaDocumento);

		return response;
	}
}
