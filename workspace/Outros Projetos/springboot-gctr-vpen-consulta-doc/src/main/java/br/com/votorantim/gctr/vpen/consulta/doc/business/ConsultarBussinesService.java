package br.com.votorantim.gctr.vpen.consulta.doc.business;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.com.votorantim.gctr.vpen.consulta.doc.domain.Documentos;
import br.com.votorantim.gctr.vpen.consulta.doc.repository.ConsultarRepository;

@Component
public class ConsultarBussinesService {

	private final static Logger log = LoggerFactory.getLogger(ConsultarBussinesService.class);

	ConsultarRepository consultarRepository;

	public ConsultarBussinesService(ConsultarRepository consultarRepository) {
		this.consultarRepository = consultarRepository;
	}

	public List<Documentos> consultarDocumentoObrigatorio(Integer codigoProduto, Integer codigoParceiroComercial, Integer codigoModalidade, String codigoTipoPessoa, String dataProcessamentoProposta) {

		log.info("ConsultarBussinesService >>> consultarDocumentoObrigatorio");

		return consultarRepository.consultarDocumentoObrigatorio(codigoProduto, codigoParceiroComercial, codigoModalidade, codigoTipoPessoa, dataProcessamentoProposta);
	}

}