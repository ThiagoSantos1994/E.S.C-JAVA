package br.esc.software.business;

import br.esc.software.commons.ExcecaoGlobal;
import br.esc.software.integration.ApisExternasImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
