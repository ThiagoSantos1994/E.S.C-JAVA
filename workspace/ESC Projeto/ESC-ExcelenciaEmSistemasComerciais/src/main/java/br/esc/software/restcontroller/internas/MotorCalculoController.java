package br.esc.software.restcontroller.internas;

import br.esc.software.business.MotorCalculoBusiness;
import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.configuration.ConnectionSQL;
import br.esc.software.domain.motorcalculo.MotorCalculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.esc.software.commons.utils.GlobalUtils.LogInfo;

@RestController
@RequestMapping("/api")
public class MotorCalculoController {

    @Autowired
    private ConnectionSQL connection;
    @Autowired
    private MotorCalculoBusiness business;

    @GetMapping(path = "/motor-calculo/relatorio/ano/{ano}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MotorCalculo> excluirBaseDados(@PathVariable("ano") Integer ano)
            throws ExcecaoGlobal {

        LogInfo("<<INICIO>> Inicializando API motor-calculo/relatorio/ano/" + ano);

        connection.abrirConexao();

        MotorCalculo response = business.calcular(ano);

        connection.fecharConexao();

        LogInfo("<<FIM>> Calculo realizado com sucesso!");

        return new ResponseEntity<MotorCalculo>(response, HttpStatus.OK);
    }
}
