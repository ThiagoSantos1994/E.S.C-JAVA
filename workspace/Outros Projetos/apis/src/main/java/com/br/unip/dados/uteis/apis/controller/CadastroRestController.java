package com.br.unip.dados.uteis.apis.controller;

import com.br.unip.dados.uteis.apis.business.CadastroBusiness;
import com.br.unip.dados.uteis.apis.domain.cadastro.CadastroPessoaRequest;
import com.br.unip.dados.uteis.apis.domain.cadastro.CadastroPessoaResponse;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebService;
import java.util.ArrayList;

@RestController
@WebService
@RequestMapping("/api/formulario")
public class CadastroRestController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CadastroBusiness business;

    private String response;

    @PostMapping(path = "/cadastrar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> cadastroPessoa(@RequestBody CadastroPessoaRequest request) throws Exception {

        logger.info("<<INICIO>> Realizando cadastro");

        response = business.cadastrar(request);

        logger.info("<<FIM>> Cadastro realizado, stts >> " + response);

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @GetMapping(path = "consultar/todos", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ArrayList<CadastroPessoaResponse>> consultarTodos() throws Exception {

        logger.info("<<INICIO>> Realizando consulta de todos os dados");

        ArrayList<CadastroPessoaResponse> response = business.consultarTodos();

        logger.info("<<FIM>> Consulta, stts >> " + response.toString());

        return new ResponseEntity<ArrayList<CadastroPessoaResponse>>(response, HttpStatus.OK);
    }

    @GetMapping(path = "consultar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ArrayList<CadastroPessoaResponse>> consultarPorId(@RequestParam("codigo") Integer codigo) throws Exception {

        logger.info("<<INICIO>> Realizando consulta no H2 pelo ID >> " + codigo);

        ArrayList<CadastroPessoaResponse> response = business.consultarPorID(codigo);

        logger.info("<<FIM>> Consulta, stts >> " + response.toString());

        return new ResponseEntity<ArrayList<CadastroPessoaResponse>>(response, HttpStatus.OK);
    }

    @GetMapping(path = "consultar/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String consultarPorIdXml(@RequestParam("codigo") Integer codigo) throws Exception {
        ArrayList<CadastroPessoaResponse> response = business.consultarPorID(codigo);
        JSONObject json = new JSONObject(response.get(0));
        return XML.toString(json);
    }

    @DeleteMapping(path = "excluir", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> excluirPorId(@RequestParam("codigo") Integer codigo) throws Exception {

        logger.info("<<INICIO>> Deletando cadastro pelo ID >> " + codigo);

        response = business.excluirPorID(codigo);

        logger.info("<<FIM>> Delete realizado, stts >> " + response);

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @DeleteMapping(path = "excluir/todos", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> excluirTodos() throws Exception {

        logger.info("<<INICIO>> Delete geral da base de dados H2");

        response = business.excluirTodos();

        logger.info("<<FIM>> Delete realizado, stts >> " + response);

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @PutMapping(path = "alterar", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> alterar(@RequestBody CadastroPessoaRequest request) throws Exception {

        logger.info("<<INICIO>> Atualizando cadastro");

        response = business.alterarPorID(request);

        logger.info("<<FIM>> Cadastro atualizado, stts >> " + response);

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

}
