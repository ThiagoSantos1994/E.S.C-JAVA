package br.esc.software.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.esc.software.commons.ExcecaoGlobal;
import br.esc.software.integration.ApisExternasImpl;

@Component
public class ApisExternasBusiness {
	
	@Autowired
	ApisExternasImpl service;

	public String obtemDadosCepConsulta(String cep) throws ExcecaoGlobal {
		if (null == cep) {
			throw new ExcecaoGlobal("Parametro [CEP] nulo ou vazio");
		}

		return service.consultaCep(cep);
	}

	public String obtemPrevisaoTempo() {
		return service.consultaPrevisaoTempo();
	}

}
