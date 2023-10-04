package br.com.votorantim.gctr.vpen.consulta.doc.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ObterQuerys {

	@Value("classpath:querys/query.xml")
	private Resource queryResource;
	
	private Logger log = LoggerFactory.getLogger(ObterQuerys.class);
	
	private Map<String, String> queries = null;
	
	private XMLQueries xmlQueries;

	private void loadQuerys() {
		
		log.info("ObterQuerys >>> loadQuerys");
		
		try (InputStream stream = queryResource.getInputStream()) {
			
			JAXBContext context = JAXBContext.newInstance(XMLQueries.class);
			
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			if (xmlQueries == null) {
			
				this.xmlQueries = (XMLQueries) unmarshaller.unmarshal(stream);
			
			}
			
			log.info("ObterQuerys >>> loadQuerys >> OK");
			
		} catch (JAXBException e) {
			
			log.error("Erro >>> ObterQuerys >>> loadQuerys >> JAXBException");
			
			throw new RuntimeException("Não foi deserializar o arquivo de querys" + e);
		
		} catch (FileNotFoundException e) {
			
			log.error("Erro >>> ObterQuerys >>> loadQuerys >> FileNotFoundException");
			
			e.printStackTrace();
		
		} catch (IOException e) {
			
			log.error("Erro >>> ObterQuerys >>> loadQuerys >> IOException");
			
			e.printStackTrace();
		
		}
	
	}

	public String getQueryFromKey(String key) {
		
		log.info("ObterQuerys >>> getQueryFromKey");
		
		try {
			
			loadQuerys();
			
			queries = new HashMap<>();
			
			for (XMLQuery xmlQuery : xmlQueries.queries) {
				
				if (key.equals(xmlQuery.name)) {
					
					log.info("ObterQuerys >>> getQueryFromKey >> OK");
					
					return xmlQuery.query;
				
				}
			
			}
			
			log.error("Erro >>> ObterQuerys >>> getQueryFromKey >> Query não encontrada, verifique o arquivo de querys");
			
			throw new RuntimeException("Query não encontrada, verifique o arquivo de querys");
		
		} catch (RuntimeException e) {
			
			log.error("Erro >>> ObterQuerys >>> getQueryFromKey >> RuntimeException");
			
			throw new RuntimeException(e);
		
		}

	}

	@XmlRootElement(name = "queries")
	@XmlAccessorType(XmlAccessType.FIELD)
	private static class XMLQueries {
		
		@XmlElement(name = "query")
		private List<XMLQuery> queries = null;
	
	}

	@XmlRootElement(name = "query")
	@XmlAccessorType(XmlAccessType.FIELD)
	private static class XMLQuery {
		
		@XmlAttribute
		private String name;
		
		@XmlValue
		private String query;
	
	}

}
