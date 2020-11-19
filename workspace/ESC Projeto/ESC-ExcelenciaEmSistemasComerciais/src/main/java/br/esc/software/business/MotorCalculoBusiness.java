package br.esc.software.business;


import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.domain.motorcalculo.MotorCalculo;
import br.esc.software.integration.MotorCalculoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MotorCalculoBusiness {

    @Autowired
    MotorCalculoImpl service;

    public MotorCalculo calcular(Integer ano) throws ExcecaoGlobal {
        return service.obterValoresMotorCalculo(ano);
    }

}
