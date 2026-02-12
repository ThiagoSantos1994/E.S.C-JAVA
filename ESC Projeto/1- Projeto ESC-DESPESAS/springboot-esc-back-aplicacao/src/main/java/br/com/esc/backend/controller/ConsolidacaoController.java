package br.com.esc.backend.controller;

import br.com.esc.backend.business.ConsolidacaoBusiness;
import br.com.esc.backend.domain.ConsolidacaoDAO;
import br.com.esc.backend.domain.ConsolidacaoDespesasRequest;
import br.com.esc.backend.domain.TituloConsolidacao;
import br.com.esc.backend.domain.TituloDespesaResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consolidacao")
@RequiredArgsConstructor
@Slf4j
public class ConsolidacaoController {

    private final ConsolidacaoBusiness service;

    @GetMapping(path = "/importacao/consultarConsolidacoes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TituloDespesaResponse> consultarConsolidacoesParaAssociacao(
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("tipo") String tipo) {
        var response = service.consultarConsolidacoesParaAssociacao(idFuncionario, idDespesa, idDetalheDespesa, tipo);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/obterTituloConsolidacoes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TituloConsolidacao>> obterTituloConsolidacoes(
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("tpBaixado") Boolean tpBaixado) {
        var response = service.obterTituloConsolidacoes(idFuncionario, tpBaixado);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/consultar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConsolidacaoDAO> obterDetalhesConsolidacao(
            @RequestParam("idConsolidacao") Integer idConsolidacao,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = service.obterDetalheConsolidacao(idConsolidacao, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/gravar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> gravarConsolidacao(@RequestBody ConsolidacaoDAO request) {
        service.gravarConsolidacao(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/excluir", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> excluirConsolidacao(@RequestBody ConsolidacaoDAO request) {
        service.excluirConsolidacao(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/despesas/desassociar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> desassociarDespesaConsolidacao(@RequestBody List<ConsolidacaoDespesasRequest> request) {
        service.desassociarDespesasConsolidacao(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

