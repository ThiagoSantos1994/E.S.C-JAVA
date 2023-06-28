package br.esc.software.restcontroller.internas;

import br.esc.software.business.RelatorioDespesasMensaisBusiness;
import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.domain.motorcalculo.MotorCalculo;
import br.esc.software.domain.relatorio.DespesasFixasMensais;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

import static br.esc.software.commons.utils.GlobalUtils.LogInfo;

@RestController
@RequestMapping("/api")
public class RelatorioDespesasMensaisController {

    @Autowired
    RelatorioDespesasMensaisBusiness business;

    @GetMapping(path = "/geradorRelatorio/obterListaResumidaDespesas/ano/{ano}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DespesasFixasMensais> obterListaResumidaDespesas(@PathVariable("ano") Integer ano) throws ExcecaoGlobal, SQLException {

        LogInfo("<<INICIO>> Inicializando API motor-calculo/relatorio/ano/" + ano);

        DespesasFixasMensais response = business.obterDespesasFixasMensais(ano);

        LogInfo("<<FIM>> Calculo realizado com sucesso!");

        return new ResponseEntity<DespesasFixasMensais>(response, HttpStatus.OK);
    }
}
