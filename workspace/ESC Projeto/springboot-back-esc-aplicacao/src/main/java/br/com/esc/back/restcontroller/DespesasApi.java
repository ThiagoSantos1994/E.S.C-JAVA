package br.com.esc.back.restcontroller;

import br.com.esc.back.business.DespesasBusiness;
import br.com.esc.back.domain.DespesasFixasMensaisResponse;
import br.com.esc.back.domain.DespesasMensaisResponse;
import br.com.esc.back.domain.DetalheDespesasMensais;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DespesasApi {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    DespesasBusiness business;

    @GetMapping(path = "/obterListaDespesasFixasMensais/{ds_Mes}/{ds_Ano}/{id_Usuario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DespesasFixasMensaisResponse> obterListaDespesasFixasMensais(@PathVariable("ds_Mes") String ds_Mes, @PathVariable("ds_Ano") String ds_Ano, @PathVariable("id_Usuario") String id_Usuario) {
        logger.info("Executando operacao obterListaDespesasFixasMensais");
        DespesasFixasMensaisResponse response = business.getListaDespesasFixasMensais(ds_Mes, ds_Ano, id_Usuario);
        return new ResponseEntity<DespesasFixasMensaisResponse>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/obterListaDespesasMensais/{id_Usuario}/{id_Despesa}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DespesasMensaisResponse> obterListaDespesasMensais(@PathVariable("id_Usuario") String id_Usuario, @PathVariable("id_Despesa") String id_Despesa) throws Exception {
        logger.info("Executando operacao obterListaDespesasMensais");
        DespesasMensaisResponse response = business.getListaDespesasMensais(id_Usuario, id_Despesa);
        return new ResponseEntity<DespesasMensaisResponse>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/obterDetalheDespesasMensais/{id_Despesa}/{id_DetalheDespesa}/{id_Usuario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DetalheDespesasMensais> obterDetalheDespesasMensais(@PathVariable("id_Despesa") String id_Despesa, @PathVariable("id_DetalheDespesa") String id_DetalheDespesa, @PathVariable("id_Usuario") String id_Usuario) throws Exception {
        logger.info("Executando operacao obterDetalheDespesasMensais");
        DetalheDespesasMensais response = business.getDetalheDespesasMensais(id_Despesa, id_DetalheDespesa, id_Usuario);
        return new ResponseEntity<DetalheDespesasMensais>(response, HttpStatus.OK);
    }

}
