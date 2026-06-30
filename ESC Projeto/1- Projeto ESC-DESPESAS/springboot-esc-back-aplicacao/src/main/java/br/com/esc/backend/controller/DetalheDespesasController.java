package br.com.esc.backend.controller;

import br.com.esc.backend.business.ConsolidacaoBusiness;
import br.com.esc.backend.business.DespesasParceladasBusiness;
import br.com.esc.backend.business.DetalheDespesasBusiness;
import br.com.esc.backend.domain.StringResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/detalheDespesas")
@RequiredArgsConstructor
@Slf4j
public class DetalheDespesasController {

    private final DespesasParceladasBusiness despesasParceladasBusiness;
    private final ConsolidacaoBusiness consolidacaoBusiness;

    @GetMapping(path = "/despesasParceladas/obterRelatorioDespesasParceladasQuitacao", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> obterRelatorioDespesasParceladasQuitacao(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = despesasParceladasBusiness.obterRelatorioDespesasParceladasQuitacao(idDespesa, idDetalheDespesa, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/consolidacao/obterRelatorioDespesasParceladas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> obterRelatorioDespesasParceladasConsolidadas(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idConsolidacao") Integer idConsolidacao,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = consolidacaoBusiness.obterRelatorioDespesasParceladasConsolidadas(idDespesa, idDetalheDespesa, idConsolidacao, idFuncionario);
        return ResponseEntity.ok(response);
    }
}
