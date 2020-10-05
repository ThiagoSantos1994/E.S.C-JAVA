package br.esc.software.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.esc.software.commons.ExcecaoGlobal;
import br.esc.software.commons.ObjectParser;
import br.esc.software.domain.PrevisaoTempoMapper;
import br.esc.software.domain.Response;
import br.esc.software.integration.ApisExternasImpl;

@Component
public class ApisExternasBusiness {
	
	private ObjectParser parser = new ObjectParser();
	
	@Autowired
	ApisExternasImpl service;

	public String obtemDadosCepConsulta(String cep) throws ExcecaoGlobal {
		if (null == cep) {
			throw new ExcecaoGlobal("Parametro [CEP] nulo ou vazio");
		}

		return service.consultaCep(cep);
	}

	public String obtemPrevisaoTempo() {
		
		PrevisaoTempoMapper response = service.consultaPrevisaoTempo();
		
		String parserResponse = response.getResults().getCity_name() + " - Máx: "
				+ response.getResults().getForecast().get(0).getMax() + "º - Min: "
				+ response.getResults().getForecast().get(0).getMin() + "º";

		Response resp = new Response();
		resp.setResponse(parserResponse);
		
		return parser.parser(resp);
	}

}
