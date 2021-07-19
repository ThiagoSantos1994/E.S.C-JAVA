package br.esc.software.restcontroller.internas;

import br.esc.software.business.MotorCalculoBusiness;
import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.configuration.ConnectionSQL;
import br.esc.software.domain.motorcalculo.MotorCalculo;
import br.esc.software.domain.motorcalculo.VariacaoPercentual;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static br.esc.software.commons.utils.GlobalUtils.LogInfo;

@RestController
@RequestMapping("/api")
public class MotorCalculoController {

    @Autowired
    private MotorCalculoBusiness business;

    @GetMapping(path = "/motor-calculo/relatorio/ano/{ano}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MotorCalculo> excluirBaseDados(@PathVariable("ano") Integer ano)
            throws ExcecaoGlobal {

        LogInfo("<<INICIO>> Inicializando API motor-calculo/relatorio/ano/" + ano);

        MotorCalculo response = business.calcular(ano);

        LogInfo("<<FIM>> Calculo realizado com sucesso!");

        return new ResponseEntity<MotorCalculo>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/motor-calculo/variacaopercentual/ano/{ano}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VariacaoPercentual> variacaoPercentual(@PathVariable("ano") Integer ano)
            throws ExcecaoGlobal {

        LogInfo("<<INICIO>> Inicializando API /motor-calculo/variacaopercentual/ano/" + ano);

        VariacaoPercentual response = business.calcularVariacao(ano);

        LogInfo("<<FIM>> Calculo realizado com sucesso!");

        return new ResponseEntity<VariacaoPercentual>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/motor-calculo/calcularvariacao", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> variacaoPercentual(@RequestParam Double vlAtual, @RequestParam Double vlAnterior) {

        LogInfo("Chamado API /motor-calculo/calcularvariacao");

        return new ResponseEntity<String>(business.calcularVariacao(vlAtual, vlAnterior), HttpStatus.OK);
    }
}
