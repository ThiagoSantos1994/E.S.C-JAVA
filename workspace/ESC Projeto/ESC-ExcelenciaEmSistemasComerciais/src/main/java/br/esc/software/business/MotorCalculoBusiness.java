package br.esc.software.business;


import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.commons.utils.ObjectParser;
import br.esc.software.domain.Response;
import br.esc.software.domain.motorcalculo.MotorCalculo;
import br.esc.software.domain.motorcalculo.VariacaoPercentual;
import br.esc.software.service.MotorCalculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MotorCalculoBusiness {

    @Autowired
    private MotorCalculoService service;
    private Response response = new Response();
    private ObjectParser parser = new ObjectParser();

    public MotorCalculo calcular(Integer ano) throws ExcecaoGlobal {
        return service.obterValoresMotorCalculo(ano);
    }

    public VariacaoPercentual calcularVariacao(Integer ano) throws ExcecaoGlobal {
        return service.calcularVariacao(ano);
    }

    public String calcularVariacao(Double valorAtual, Double valorAnterior) {
        response.setResponse(service.obterVariacaoCalculada(valorAtual, valorAnterior));
        return parser.parser(response);
    }
}
