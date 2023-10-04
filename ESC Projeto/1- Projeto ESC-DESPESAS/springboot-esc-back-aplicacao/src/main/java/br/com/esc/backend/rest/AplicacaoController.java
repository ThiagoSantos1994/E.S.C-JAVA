package br.com.esc.backend.rest;

import br.com.esc.backend.domain.LancamentosFinanceirosDTO;
import br.com.esc.backend.service.LancamentosFinanceirosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AplicacaoController {

    private final LancamentosFinanceirosService service;

    @GetMapping(path = "/despesasMensais/obterDados/{dsMes}/{dsAno}/{idFuncionario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LancamentosFinanceirosDTO> obterLancamentosFinanceiros(@PathVariable("dsMes") String dsMes, @PathVariable("dsAno") String dsAno, @PathVariable("idFuncionario") Integer idFuncionario) {
        var response = service.obterLancamentosFinanceiros(dsMes, dsAno, idFuncionario);
        return ResponseEntity.ok(response);
    }
}
