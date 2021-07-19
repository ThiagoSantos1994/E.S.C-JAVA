package com.br.unip.dados.uteis.apis.business;

import com.br.unip.dados.uteis.apis.commons.ObjectParser;
import com.br.unip.dados.uteis.apis.domain.Response;
import com.br.unip.dados.uteis.apis.domain.cadastro.CadastroPessoaRequest;
import com.br.unip.dados.uteis.apis.domain.cadastro.CadastroPessoaResponse;
import com.br.unip.dados.uteis.apis.service.CadastroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CadastroBusiness {

    @Autowired
    CadastroService service;

    private Response response = new Response();
    private ObjectParser parser = new ObjectParser();

    public String cadastrar(CadastroPessoaRequest request) {
        response.setResponse(service.salvar(request));
        return parser.parser(response);
    }

    public ArrayList<CadastroPessoaResponse> consultarTodos() {
        return this.validarDadosConsulta(service.consultar("ALL", null));
    }

    public ArrayList<CadastroPessoaResponse> consultarPorID(Integer id) {
        return this.validarDadosConsulta(service.consultar("ID", id));
    }

    public String excluirPorID(Integer id) {
        response.setResponse(service.excluir("ID", id));
        return parser.parser(response);
    }

    public String excluirTodos() {
        response.setResponse(service.excluir("ALL", null));
        return parser.parser(response);
    }

    public String alterarPorID(CadastroPessoaRequest request) {
        response.setResponse(service.alterar(request));
        return parser.parser(response);
    }

    private ArrayList<CadastroPessoaResponse> validarDadosConsulta(ArrayList<CadastroPessoaResponse> pessoaResponse) {
        ArrayList<CadastroPessoaResponse> responses = pessoaResponse;
        if (responses.size() > 0) {
            return responses;
        }

        CadastroPessoaResponse cadastro = new CadastroPessoaResponse();
        cadastro.setObservacoes("Nenhum registro localizado na base de dados :(");

        responses.add(cadastro);
        return responses;
    }
}
