package br.com.esc.backend.controller;

import br.com.esc.backend.business.LancamentosFinanceirosBusiness;
import br.com.esc.backend.domain.LancamentosMensaisDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lancamentosMensais")
@RequiredArgsConstructor
@Slf4j
public class LancamentosMensaisController {

    private final LancamentosFinanceirosBusiness lancamentosFinanceirosBusiness;

    @GetMapping(path = "/consolidados/consultar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LancamentosMensaisDAO>> obterLancamentosMensais(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idConsolidacao") Integer idConsolidacao,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        var response = lancamentosFinanceirosBusiness.obterDespesasMensaisConsolidadas(idDespesa, idConsolidacao, idFuncionario);
        return ResponseEntity.ok(response);
    }
}

