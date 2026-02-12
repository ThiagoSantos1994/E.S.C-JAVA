package br.com.esc.backend.controller;

import br.com.esc.backend.business.DespesasParceladasBusiness;
import br.com.esc.backend.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class DespesasParceladasController {

    private final DespesasParceladasBusiness service;

    @GetMapping(path = "/despesasParceladas/obterListaDespesas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DespesasParceladasResponse> obterDespesasParceladas(
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("status") String status) {
        var response = service.obterDespesasParceladas(idFuncionario, status);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/despesasParceladas/obterValorDespesa", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> obterValorDespesaParcelada(
            @RequestParam("idDespesaParcelada") Integer idDespesaParcelada,
            @RequestParam("idParcela") Integer idParcela,
            @RequestParam("mesAnoReferencia") String mesAnoReferencia,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = service.obterValorDespesaParcelada(idDespesaParcelada, idParcela, mesAnoReferencia, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/despesasParceladas/consultar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DetalheDespesasParceladasResponse> obterDespesaParceladaPorNome(
            @RequestParam("nomeDespesaParcelada") String nomeDespesaParcelada,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = service.obterDespesaParceladaPorNome(nomeDespesaParcelada, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/despesasParceladas/gerarFluxoParcelas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExplodirFluxoParcelasResponse> gerarFluxoParcelas(
            @RequestParam("idDespesaParcelada") Integer idDespesaParcelada,
            @RequestParam("valorParcela") String valorParcela,
            @RequestParam("qtdeParcelas") Integer qtdeParcelas,
            @RequestParam("dataReferencia") String dataReferencia,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = service.gerarFluxoParcelas(idDespesaParcelada, valorParcela, qtdeParcelas, dataReferencia, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/despesasParceladas/consultarNomeDespesa", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> consultarNomeDespesaParceladaPorFiltro(
            @RequestParam("idDespesaParcelada") Integer idDespesaParcelada,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = service.consultarNomeDespesaParceladaPorFiltro(idDespesaParcelada, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/despesasParceladas/importacao/consultarDespesasParceladas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TituloDespesaResponse> consultarDespesasParceladasParaImportacao(
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("tipo") String tipo) {
        var response = service.consultarDespesasParceladasParaImportacao(idFuncionario, tipo);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/despesasParceladas/validarTituloDespesaParceladaExistente", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> validarTituloDespesaParceladaExistente(
            @RequestParam("dsTituloDespesaParcelada") String dsTituloDespesaParcelada,
            @RequestParam("idDespesaParcelada") Integer idDespesaParcelada,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = service.validarTituloDespesaParceladaExistente(dsTituloDespesaParcelada, idDespesaParcelada, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/despesasParceladas/validaDespesaExistente", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> validaDespesaParceladaExistente(
            @RequestParam("dsTituloDespesaParcelada") String dsTituloDespesaParcelada,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = service.validaDespesaParceladaExistente(dsTituloDespesaParcelada, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/despesasParceladas/obterCalculoValorTotalPendente", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> obterCalculoValorTotalDespesaParceladaPendente(
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = service.obterCalculoValorTotalDespesaParceladaPendente(idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/despesasParceladas/obterRelatorioDespesasParceladasQuitacao", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> obterRelatorioDespesasParceladasQuitacao(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = service.obterRelatorioDespesasParceladasQuitacao(idDespesa, null, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/despesasParceladas/obterParcelasParaAmortizacao", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ParcelasDAO>> obterParcelasParaAmortizacao(
            @RequestParam("idDespesaParcelada") Integer idDespesaParcelada,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = service.obterParcelasParaAmortizacao(idDespesaParcelada, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/despesasParceladas/gravar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> gravarDespesasParceladas(@RequestBody DespesaParceladaDAO request) {
        service.gravarDespesaParcelada(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/despesasParceladas/parcelas/gravar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> gravarParcela(@RequestBody List<ParcelasDAO> request) {
        service.gravarParcela(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/despesasParceladas/parcelas/excluir", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> excluirParcela(@RequestBody List<ParcelasDAO> request) {
        service.deleteParcela(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/despesasParceladas/excluir", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> excluirDespesaParcelada(
            @RequestParam("idDespesaParcelada") Integer idDespesaParcelada,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        service.deleteDespesaParcelada(idDespesaParcelada, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/despesasParceladas/quitar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> quitarDespesaParcelada(
            @RequestParam("idDespesaParcelada") Integer idDespesaParcelada,
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("valorQuitacao") String valorQuitacao) {

        service.quitarDespesaParcelada(idDespesaParcelada, idFuncionario, valorQuitacao);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/v2/despesasParceladas/consultar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DetalheDespesasParceladasResponse> obterDespesaParceladaPorID(
            @RequestParam("idDespesaParcelada") Integer idDespesaParcelada,
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("isPendentes") Boolean isPendentes) {
        var response = service.obterDespesaParceladaPorID(idDespesaParcelada, idFuncionario, isPendentes);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/v2/despesasParceladas/gerarFluxoParcelas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DetalheDespesasParceladasResponse> gerarFluxoParcelasV2(
            @RequestParam("idDespesaParcelada") Integer idDespesaParcelada,
            @RequestParam("valorParcela") String valorParcela,
            @RequestParam("qtdeParcelas") Integer qtdeParcelas,
            @RequestParam("dataReferencia") String dataReferencia,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = service.gerarFluxoParcelasV2(idDespesaParcelada, valorParcela, qtdeParcelas, dataReferencia, idFuncionario);
        return ResponseEntity.ok(response);
    }
}

