package br.com.esc.backend.controller;

import br.com.esc.backend.business.LembretesBusiness;
import br.com.esc.backend.domain.LembretesDAO;
import br.com.esc.backend.domain.TituloLembretesDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lembretes")
@RequiredArgsConstructor
@Slf4j
public class LembretesController {

    private final LembretesBusiness lembretesBusiness;

    @GetMapping(path = "/detalhe", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LembretesDAO> obterDetalhesLembrete(
            @RequestParam("idLembrete") Integer idLembrete,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = lembretesBusiness.obterDetalheLembrete(idLembrete, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/monitor", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TituloLembretesDAO>> obterMonitorLembretesEmAberto(
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = lembretesBusiness.obterListaMonitorLembretes(idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/obterTituloLembretes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TituloLembretesDAO>> obterTituloLembretes(
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("tpBaixado") Boolean tpBaixado) {
        var response = lembretesBusiness.obterTituloLembretes(idFuncionario, tpBaixado);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/monitor/baixar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> baixarLembreteMonitor(
            @RequestBody List<TituloLembretesDAO> request,
            @RequestParam("tipoBaixa") String tipoBaixa) {

        lembretesBusiness.baixarLembretesMonitor(request, tipoBaixa);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/detalhe/gravar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> gravarLembrete(@RequestBody LembretesDAO request) {
        lembretesBusiness.gravarLembrete(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/detalhe/excluir", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> exluirLembrete(@RequestBody LembretesDAO request) {
        lembretesBusiness.excluirLembrete(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

