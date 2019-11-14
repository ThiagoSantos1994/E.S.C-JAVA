package br.com.votorantim.gctr.vpen.consulta.doc.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import br.com.votorantim.gctr.vpen.consulta.doc.domain.Documentos;

public class DocumentosMapper implements RowMapper<Documentos> {
	
	private final static Logger log = LoggerFactory.getLogger(DocumentosMapper.class);

	@Override
	public Documentos mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		log.info("DocumentosMapper >> mapRow");
		
		Documentos documentos = new Documentos();
		
		documentos.setCodigoDocumento(rs.getInt("CdEstruturaDocumento"));
		
		return documentos;
	}
}
