package br.com.esc.backend.controller;

import br.com.esc.backend.business.LancamentosBusinessService;
import br.com.esc.backend.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AplicacaoController {

    private final LancamentosBusinessService service;

    @GetMapping(path = "/lancamentosFinanceiros/consultar/{dsMes}/{dsAno}/{idFuncionario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LancamentosFinanceirosDTO> obterLancamentosFinanceiros(@PathVariable("dsMes") String dsMes, @PathVariable("dsAno") String dsAno, @PathVariable("idFuncionario") Integer idFuncionario) {
        var response = service.obterLancamentosFinanceiros(dsMes, dsAno, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais/consultar/{idDespesa}/{idDetalheDespesa}/{idFuncionario}/{ordem}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DetalheDespesasMensaisDTO> obterDetalheDespesasMensais(@PathVariable("idDespesa") Integer idDespesa, @PathVariable("idDetalheDespesa") Integer idDetalheDespesa, @PathVariable("idFuncionario") Integer idFuncionario, @PathVariable("ordem") String ordem) {
        var response = service.obterDetalheDespesaMensal(idDespesa, idDetalheDespesa, idFuncionario, ordem);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/obterNovaChaveKey/{tipoChave}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChaveKeyDAO> obterNovaChaveKey(@PathVariable("tipoChave") String tipoChave) {
        var response = service.retornaNovaChaveKey(tipoChave);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/obterMesAnoPorID/{idDespesa}/{idFuncionario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> obterMesAnoPorID(@PathVariable("idDespesa") Integer idDespesa, @PathVariable("idFuncionario") Integer idFuncionario) {
        var response = service.obterMesAnoPorID(idDespesa, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/obterSubTotalDespesa/{idDespesa}/{idDetalheDespesa}/{idFuncionario}/{ordem}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> obterSubTotalDespesa(@PathVariable("idDespesa") Integer idDespesa, @PathVariable("idDetalheDespesa") Integer idDetalheDespesa, @PathVariable("idFuncionario") Integer idFuncionario, @PathVariable("ordem") String ordem) {
        var response = service.obterSubTotalDespesa(idDespesa, idDetalheDespesa, idFuncionario, ordem);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais/obterExtratoDespesasMes/{idDespesa}/{idDetalheDespesa}/{idFuncionario}/{tipo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExtratoDespesasDAO> obterExtratoDespesasMes(@PathVariable("idDespesa") Integer idDespesa, @PathVariable("idDetalheDespesa") Integer idDetalheDespesa, @PathVariable("idFuncionario") Integer idFuncionario, @PathVariable("tipo") String tipo) {
        var response = service.obterExtratoDespesasMes(idDespesa, idDetalheDespesa, idFuncionario, tipo);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/obterTitulosDespesas/{idFuncionario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TituloDespesaResponse> obterTitulosDespesas(@PathVariable("idFuncionario") Integer idFuncionario) {
        var response = service.obterTitulosDespesas(idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/obterTitulosEmprestimos/{idFuncionario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TituloDespesaResponse> obterTitulosEmprestimos(@PathVariable("idFuncionario") Integer idFuncionario) {
        var response = service.obterTitulosEmprestimos(idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/despesasParceladas/obterListaDespesas/{idFuncionario}/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DespesasParceladasResponse> obterDespesasParceladas(@PathVariable("idFuncionario") Integer idFuncionario, @PathVariable("status") String status) {
        var response = service.obterDespesasParceladas(idFuncionario, status);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/despesasParceladas/obterValorDespesa/{idDespesaParcelada}/{idParcela}/{mesAnoReferencia}/{idFuncionario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> obterValorDespesaParcelada(@PathVariable("idDespesaParcelada") Integer idDespesaParcelada, @PathVariable("idParcela") Integer idParcela, @PathVariable("mesAnoReferencia") String mesAnoReferencia, @PathVariable("idFuncionario") Integer idFuncionario) {
        var response = service.obterValorDespesaParcelada(idDespesaParcelada, idParcela, mesAnoReferencia, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/despesasParceladas/consultar/{nomeDespesaParcelada}/{idFuncionario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DetalheDespesasParceladasResponse> obterDespesaParceladaPorNome(@PathVariable("nomeDespesaParcelada") String nomeDespesaParcelada, @PathVariable("idFuncionario") Integer idFuncionario) {
        var response = service.obterDespesaParceladaPorNome(nomeDespesaParcelada, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/despesasParceladas/gerarFluxoParcelas/{idDespesaParcelada}/{valorParcela}/{qtdeParcelas}/{dataReferencia}/{idFuncionario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExplodirFluxoParcelasResponse> gerarFluxoParcelas(@PathVariable("idDespesaParcelada") Integer idDespesaParcelada, @PathVariable("valorParcela") String valorParcela, @PathVariable("qtdeParcelas") Integer qtdeParcelas, @PathVariable("dataReferencia") String dataReferencia, @PathVariable("idFuncionario") Integer idFuncionario) {
        var response = service.gerarFluxoParcelas(idDespesaParcelada, valorParcela, qtdeParcelas, dataReferencia, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/despesasParceladas/consultarNomeDespesa/{idDespesaParcelada}/{idFuncionario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> consultarNomeDespesaParceladaPorFiltro(@PathVariable("idDespesaParcelada") Integer idDespesaParcelada, @PathVariable("idFuncionario") Integer idFuncionario) {
        var response = service.consultarNomeDespesaParceladaPorFiltro(idDespesaParcelada, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/despesasParceladas/importacao/consultarDespesasParceladas/{idFuncionario}/{tipo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TituloDespesaResponse> consultarDespesasParceladasParaImportacao(@PathVariable("idFuncionario") Integer idFuncionario, @PathVariable("tipo") String tipo) {
        var response = service.consultarDespesasParceladasParaImportacao(idFuncionario, tipo);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/despesasParceladas/validarTituloDespesaParceladaExistente/{dsTituloDespesaParcelada}/{idDespesaParcelada}/{idFuncionario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> validarTituloDespesaParceladaExistente(@PathVariable("dsTituloDespesaParcelada") String dsTituloDespesaParcelada, @PathVariable("idDespesaParcelada") Integer idDespesaParcelada, @PathVariable("idFuncionario") Integer idFuncionario) {
        var response = service.validarTituloDespesaParceladaExistente(dsTituloDespesaParcelada, idDespesaParcelada, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/despesasParceladas/gravar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> gravarDespesasParceladas(@RequestBody DespesaParceladaDAO request) {
        service.gravarDespesaParcelada(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/despesasParceladas/parcelas/gravar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> gravarParcela(@RequestBody ParcelasDAO request) {
        service.gravarParcela(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/despesasParceladas/parcelas/excluir/{idDespesaParcelada}/{idParcela}/{idFuncionario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> excluirParcela(@PathVariable("idDespesaParcelada") Integer idDespesaParcelada, @PathVariable("idParcela") Integer idParcela, @PathVariable("idFuncionario") Integer idFuncionario) {
        service.deleteParcela(idDespesaParcelada, idParcela, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/despesasParceladas/excluir/{idDespesaParcelada}/{idFuncionario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> excluirParcela(@PathVariable("idDespesaParcelada") Integer idDespesaParcelada, @PathVariable("idFuncionario") Integer idFuncionario) {
        service.deleteDespesaParcelada(idDespesaParcelada, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/despesasFixasMensais/gravar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> gravarDespesasFixasMensais(@RequestBody DespesasFixasMensaisRequest request) {
        service.gravarDespesasFixasMensais(request);
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

    @PostMapping(path = "/lancamentosFinanceiros/despesasMensais/incluir", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> gravarDespesaMensal(@RequestBody DespesasMensaisRequest request) throws InvocationTargetException, IllegalAccessException {
        service.gravarDespesaMensal(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais/incluir", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> gravarDetalheDespesasMensais(@RequestBody DetalheDespesasMensaisRequest request) {
        service.gravarDetalheDespesasMensais(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais/ordenarListaDespesas/{idDespesa}/{idDetalheDespesa}/{idFuncionario}/{ordem}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> ordenarListaDetalheDespesasMensais(@PathVariable("idDespesa") Integer idDespesa, @PathVariable("idDetalheDespesa") Integer idDetalheDespesa, @PathVariable("idFuncionario") Integer idFuncionario, @PathVariable("ordem") String ordem) {
        service.ordenarListaDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario, ordem);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/ordenarListaDespesas/{idDespesa}/{idFuncionario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> ordenarListaDespesasMensais(@PathVariable("idDespesa") Integer idDespesa, @PathVariable("idFuncionario") Integer idFuncionario) {
        service.ordenarListaDespesasMensais(idDespesa, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais/baixarPagamentoDespesa", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> processarPagamentoDetalheDespesas(@RequestBody PagamentoDespesasRequest request) {
        service.processarPagamentoDetalheDespesas(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/excluir/{idDespesa}/{idFuncionario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteTodosLancamentosMensais(@PathVariable("idDespesa") Integer idDespesa, @PathVariable("idFuncionario") Integer idFuncionario) {
        service.deleteTodosLancamentosMensais(idDespesa, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/importacao/processamento/{idDespesa}/{idFuncionario}/{dsMes}/{dsAno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> processarImportacaoDespesasMensais(@PathVariable("idDespesa") Integer idDespesa, @PathVariable("idFuncionario") Integer idFuncionario, @PathVariable("dsMes") String dsMes, @PathVariable("dsAno") String dsAno) {
        service.processarImportacaoDespesasMensais(idDespesa, idFuncionario, dsMes, dsAno);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/importacao/detalheDespesasMensais/{idDespesa}/{idDetalheDespesa}/{idFuncionario}/{dsMes}/{dsAno}/{bReprocessarTodosValores}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> processarImportacaoDetalheDespesasMensais(@PathVariable("idDespesa") Integer idDespesa, @PathVariable("idDetalheDespesa") Integer idDetalheDespesa, @PathVariable("idFuncionario") Integer idFuncionario, @PathVariable("dsMes") String dsMes, @PathVariable("dsAno") String dsAno, @PathVariable("bReprocessarTodosValores") Boolean bReprocessarTodosValores) {
        service.processarImportacaoDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario, dsMes, dsAno, bReprocessarTodosValores);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/parcelas/adiantarFluxoParcelas/{idDespesa}/{idDetalheDespesa}/{idDespesaParcelada}/{idParcela}/{idFuncionario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> adiantarFluxoParcelas(@PathVariable("idDespesa") Integer idDespesa, @PathVariable("idDetalheDespesa") Integer idDetalheDespesa, @PathVariable("idDespesaParcelada") Integer idDespesaParcelada, @PathVariable("idParcela") Integer idParcela, @PathVariable("idFuncionario") Integer idFuncionario) {
        service.adiantarFluxoParcelas(idDespesa, idDetalheDespesa, idDespesaParcelada, idParcela, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/parcelas/desfazerAdiantamentoFluxoParcelas/{idDespesa}/{idDetalheDespesa}/{idDespesaParcelada}/{idParcela}/{idFuncionario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> desfazerAdiantamentoFluxoParcelas(@PathVariable("idDespesa") Integer idDespesa, @PathVariable("idDetalheDespesa") Integer idDetalheDespesa, @PathVariable("idDespesaParcelada") Integer idDespesaParcelada, @PathVariable("idParcela") Integer idParcela, @PathVariable("idFuncionario") Integer idFuncionario) {
        service.desfazerAdiantamentoFluxoParcelas(idDespesa, idDetalheDespesa, idDespesaParcelada, idParcela, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/validaDespesaExistenteDebitoCartao/{idDespesa}/{idDetalheDespesa}/{idFuncionario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> validaDespesaExistenteDebitoCartao(@PathVariable("idDespesa") Integer idDespesa, @PathVariable("idDetalheDespesa") Integer idDetalheDespesa, @PathVariable("idFuncionario") Integer idFuncionario) {
        var response = service.validaDespesaExistenteDebitoCartao(idDespesa, idDetalheDespesa, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/lancamentosFinanceiros/validaTituloDespesaDuplicado/{idDespesa}/{idDetalheDespesa}/{idFuncionario}/{tituloDespesa}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> validaTituloDespesaDuplicado(@PathVariable("idDespesa") Integer idDespesa, @PathVariable("idDetalheDespesa") Integer idDetalheDespesa, @PathVariable("idFuncionario") Integer idFuncionario, @PathVariable("tituloDespesa") String tituloDespesa) {
        var response = service.validaTituloDespesaDuplicado(idDespesa, idDetalheDespesa, idFuncionario, tituloDespesa);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/lancamentosFinanceiros/alterarTituloDespesaReuso/{idDespesa}/{idDetalheDespesa}/{idFuncionario}/{novoTituloDespesa}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> alterarTituloDespesaReuso(@PathVariable("idDespesa") Integer idDespesa, @PathVariable("idDetalheDespesa") Integer idDetalheDespesa, @PathVariable("idFuncionario") Integer idFuncionario, @PathVariable("novoTituloDespesa") String novoTituloDespesa) {
        var response = service.alterarTituloDespesaReuso(idDespesa, idDetalheDespesa, idFuncionario, novoTituloDespesa);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/lancamentosFinanceiros/alterarTituloDespesa/{idDetalheDespesa}/{idFuncionario}/{novoTituloDespesa}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> alterarTituloDespesa(@PathVariable("idDetalheDespesa") Integer idDetalheDespesa, @PathVariable("idFuncionario") Integer idFuncionario, @PathVariable("novoTituloDespesa") String novoTituloDespesa) {
        service.alterarTituloDespesa(idDetalheDespesa, idFuncionario, novoTituloDespesa);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/alterarOrdemRegistroDetalheDespesas/{idDespesa}/{idDetalheDespesa}/{iOrdemAtual}/{iOrdemNova}/{idFuncionario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> alterarOrdemRegistroDetalheDespesas(@PathVariable("idDespesa") Integer idDespesa, @PathVariable("idDetalheDespesa") Integer idDetalheDespesa, @PathVariable("iOrdemAtual") Integer iOrdemAtual, @PathVariable("iOrdemNova") Integer iOrdemNova, @PathVariable("idFuncionario") Integer idFuncionario) {
        service.alterarOrdemRegistroDetalheDespesas(idDespesa, idDetalheDespesa, iOrdemAtual, iOrdemNova, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/alterarOrdemRegistroDespesas/{idDespesa}/{iOrdemAtual}/{iOrdemNova}/{idFuncionario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> alterarOrdemRegistroDespesas(@PathVariable("idDespesa") Integer idDespesa, @PathVariable("iOrdemAtual") Integer iOrdemAtual, @PathVariable("iOrdemNova") Integer iOrdemNova, @PathVariable("idFuncionario") Integer idFuncionario) {
        service.alterarOrdemRegistroDespesas(idDespesa, iOrdemAtual, iOrdemNova, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/alterarOrdemRegistroDespesasFixas/{idDespesa}/{iOrdemAtual}/{iOrdemNova}/{idFuncionario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> alterarOrdemRegistroDespesasFixas(@PathVariable("idDespesa") Integer idDespesa, @PathVariable("iOrdemAtual") Integer iOrdemAtual, @PathVariable("iOrdemNova") Integer iOrdemNova, @PathVariable("idFuncionario") Integer idFuncionario) {
        service.alterarOrdemRegistroDespesasFixas(idDespesa, iOrdemAtual, iOrdemNova, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/gerarDespesasFuturas/{dsMes}/{dsAno}/{idFuncionario}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DespesaFixaTemporariaResponse> gerarDespesasFuturas(@PathVariable("dsMes") Integer dsMes, @PathVariable("dsAno") Integer dsAno, @PathVariable("idFuncionario") Integer idFuncionario) {
        var response = service.gerarTemporariamenteDespesasMensais(dsMes, dsAno, idFuncionario);
        return ResponseEntity.ok(response);
    }
}
