/**
 * 
 */
package br.com.votorantim.gctr.vpen.consulta.doc.repository;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import br.com.votorantim.gctr.vpen.consulta.doc.domain.Documentos;

/**
 * @author resource.mlima
 *
 */

@Repository
public class ConsultarRepository {

	private final static Logger log = LoggerFactory.getLogger(ConsultarRepository.class);

	private final NamedParameterJdbcTemplate jdbcTemplate;
	private DocumentosMapper documentosMapper;

	@Autowired
	private ObterQuerys querys;

	@Autowired
	public ConsultarRepository(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		documentosMapper = new DocumentosMapper();

	}

	public List<Documentos> consultarDocumentoObrigatorio(Integer codigoProduto, Integer codigoParceiroComercial, Integer codigoModalidade, String codigoTipoPessoa, String dataProcessamentoProposta) {

		log.info("ConsultarRepository >>> consultarDocumentoObrigatorio");

		String sql = querys.getQueryFromKey("queryListarDocumentacao");

		final SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("CdProduto", codigoProduto);

		try {

			List<Documentos> resultado = jdbcTemplate.query(sql, namedParameters, documentosMapper);

			return resultado;

		} catch (Exception e) {
			
			log.error("Erro >>> ConsultarRepository >>> consultarDocumentoObrigatorio");
			
		}
		return new ArrayList<Documentos>();

	}

}
