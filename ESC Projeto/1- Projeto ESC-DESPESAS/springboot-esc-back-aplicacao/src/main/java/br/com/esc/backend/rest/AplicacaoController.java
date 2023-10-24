package br.com.esc.backend.rest;

import br.com.esc.backend.domain.DespesasFixasMensaisRequest;
import br.com.esc.backend.domain.DetalheDespesasMensaisDTO;
import br.com.esc.backend.domain.LancamentosFinanceirosDTO;
import br.com.esc.backend.service.LancamentosFinanceirosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AplicacaoController {

    private final LancamentosFinanceirosService service;

    @GetMapping(path = "/lancamentosFinanceiros/consultar/{dsMes}/{dsAno}/{idFuncionario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LancamentosFinanceirosDTO> obterLancamentosFinanceiros(@PathVariable("dsMes") String dsMes, @PathVariable("dsAno") String dsAno, @PathVariable("idFuncionario") Integer idFuncionario) {
        var response = service.obterLancamentosFinanceiros(dsMes, dsAno, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/detalhes/consultar/{idDespesa}/{idDetalheDespesa}/{idFuncionario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DetalheDespesasMensaisDTO> obterDetalheDespesasMensais(@PathVariable("idDespesa") Integer idDespesa, @PathVariable("idDetalheDespesa") Integer idDetalheDespesa, @PathVariable("idFuncionario") Integer idFuncionario) {
        var response = service.obterDetalheDespesaMensal(idDespesa, idDetalheDespesa, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/lancamentosFinanceiros/despesasFixasMensais/gravar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> gravarDespesasFixasMensais(@RequestBody DespesasFixasMensaisRequest request) {
        service.gravarDespesasFixasMensais(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/despesasFixasMensais/atualizar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> atualizarDespesasFixasMensais(@RequestBody DespesasFixasMensaisRequest request) {
        service.updateDespesasFixasMensais(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/despesasFixasMensais/excluir/{idDespesa}/{idOrdem}/{idFuncionario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteDespesaFixaMensal(@PathVariable("idDespesa") Integer idDespesa, @PathVariable("idOrdem") Integer idOrdem, @PathVariable("idFuncionario") Integer idFuncionario) {
        service.deleteDespesaFixaMensal(idDespesa, idOrdem, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/despesasMensais/excluir/{idDespesa}/{idDetalheDespesa}/{idOrdem}/{idFuncionario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteDespesMensal(@PathVariable("idDespesa") Integer idDespesa, @PathVariable("idDetalheDespesa") Integer idDetalheDespesa, @PathVariable("idOrdem") Integer idOrdem, @PathVariable("idFuncionario") Integer idFuncionario) {
        service.deleteDespesasMensais(idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais/excluir/{idDespesa}/{idDetalheDespesa}/{idOrdem}/{idFuncionario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteDetalheDespesasMensais(@PathVariable("idDespesa") Integer idDespesa, @PathVariable("idDetalheDespesa") Integer idDetalheDespesa, @PathVariable("idOrdem") Integer idOrdem, @PathVariable("idFuncionario") Integer idFuncionario) {
        service.deleteDetalheDespesasMensais(idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/excluir/{idDespesa}/{idFuncionario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteTodosLancamentosMensais(@PathVariable("idDespesa") Integer idDespesa, @PathVariable("idFuncionario") Integer idFuncionario) {
        service.deleteTodosLancamentosMensais(idDespesa, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/importacao/processamento/{idDespesa}/{idFuncionario}/{dsMes}/{dsAno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> processarImportacaoDespesasMensais(@PathVariable("idDespesa") Integer idDespesa, @PathVariable("idFuncionario") Integer idFuncionario, @PathVariable("dsMes") String dsMes, @PathVariable("dsAno") String dsAno) throws Exception {
        service.processarImportacaoDespesasMensais(idDespesa, idFuncionario, dsMes, dsAno);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/importacao/detalheDespesasMensais/{idDespesa}/{idDetalheDespesa}/{idFuncionario}/{dsMes}/{dsAno}/{bReprocessarTodosValores}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> processarImportacaoDetalheDespesasMensais(@PathVariable("idDespesa") Integer idDespesa, @PathVariable("idDetalheDespesa") Integer idDetalheDespesa, @PathVariable("idFuncionario") Integer idFuncionario, @PathVariable("dsMes") String dsMes, @PathVariable("dsAno") String dsAno, @PathVariable("bReprocessarTodosValores") Boolean bReprocessarTodosValores) throws Exception {
        service.processarImportacaoDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario, dsMes, dsAno, bReprocessarTodosValores);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
