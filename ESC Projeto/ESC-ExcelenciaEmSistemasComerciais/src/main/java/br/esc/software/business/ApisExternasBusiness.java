package br.esc.software.business;

import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.service.ApisExternasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApisExternasBusiness {

    @Autowired
    private ApisExternasService service;

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
