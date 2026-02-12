package br.com.esc.backend.controller;

import br.com.esc.backend.business.*;
import br.com.esc.backend.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class LancamentosFinanceirosController {

    private final LancamentosFinanceirosBusiness lancamentosFinanceirosBusiness;
    private final DetalheDespesasBusiness detalheDespesasBusiness;
    private final ChaveKeyBusiness chaveKeyBusiness;
    private final ConsolidacaoBusiness consolidacaoBusiness;
    private final ImportacaoBusiness importacaoBusiness;
    private final DespesasParceladasBusiness despesasParceladasBusiness;

    @GetMapping(path = "/lancamentosFinanceiros/consultar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LancamentosFinanceirosDTO> obterLancamentosFinanceiros(
            @RequestParam("dsMes") String dsMes,
            @RequestParam("dsAno") String dsAno,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        var response = lancamentosFinanceirosBusiness.obterLancamentosFinanceiros(dsMes, dsAno, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais/consultar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DetalheDespesasMensaisDTO> obterDetalheDespesasMensais(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("ordem") String ordem,
            @RequestParam("visualizarConsolidacao") Boolean visualizarConsolidacao) {
        var response = detalheDespesasBusiness.obterDetalheDespesaMensal(idDespesa, idDetalheDespesa, idFuncionario, ordem, visualizarConsolidacao);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/categoriaDespesa/subTotal", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoriaDespesasResponse> obterSubTotalCategoriaDespesa(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = detalheDespesasBusiness.obterSubTotalCategoriaDespesa(idDespesa, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/categoriaDespesa/subTotal/anual", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoriaDespesasResponse> obterSubTotalCategoriaDespesaAnual(
            @RequestParam("dsAno") Integer dsAno,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = detalheDespesasBusiness.obterSubTotalCategoriaDespesaAno(dsAno, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/obterNovaChaveKey", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChaveKeyDAO> obterNovaChaveKey(
            @RequestParam("tipoChave") String tipoChave) {
        var response = chaveKeyBusiness.retornaNovaChaveKey(tipoChave);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/obterMesAnoPorID", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> obterMesAnoPorID(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = lancamentosFinanceirosBusiness.obterMesAnoPorID(idDespesa, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/obterSubTotalDespesa", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> obterSubTotalDespesa(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("ordem") String ordem) {
        var response = detalheDespesasBusiness.obterSubTotalDespesa(idDespesa, idDetalheDespesa, idFuncionario, ordem);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais/obterExtratoDespesasMes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExtratoDespesasDAO> obterExtratoDespesasMes(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("tipo") String tipo) {
        var response = detalheDespesasBusiness.obterExtratoDespesasMes(idDespesa, idDetalheDespesa, idFuncionario, tipo);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais/obterDespesasMensaisParaAssociacao", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TituloDespesaResponse> consultarDespesasMensaisParaAssociacao(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("anoReferencia") Integer anoReferencia) {
        var response = detalheDespesasBusiness.consultarDespesasMensaisParaAssociacao(idDespesa, idFuncionario, anoReferencia);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/obterTitulosDespesas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TituloDespesaResponse> obterTitulosDespesas(
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = lancamentosFinanceirosBusiness.obterTitulosDespesas(idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/obterTitulosEmprestimos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TituloDespesaResponse> obterTitulosEmprestimos(
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = lancamentosFinanceirosBusiness.obterTitulosEmprestimos(idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais/observacoes/consultar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> obterObservacoesDetalheDespesasMensais(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idObservacao") Integer idObservacao,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = detalheDespesasBusiness.obterObservacoesDetalheDespesa(idDespesa, idDetalheDespesa, idObservacao, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/obterTitulosDespesasRelatorio", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TituloDespesaResponse> obterTitulosDespesasRelatorio(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = lancamentosFinanceirosBusiness.obterTitulosDespesasRelatorio(idDespesa, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais/historico/consultar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> obterHistoricoDetalheDespesasMensais(
            @RequestParam("idDetalheDespesaLog") Integer idDetalheDespesaLog,
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = detalheDespesasBusiness.obterHistoricoDetalheDespesa(idDetalheDespesaLog, idDespesa, idDetalheDespesa, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais/consolidacao/associar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> associarDetalheDespesasConsolidacao(
            @RequestParam("idConsolidacao") Integer idConsolidacao,
            @RequestBody List<DetalheDespesasMensaisRequest> request) {

        consolidacaoBusiness.associarDespesaDetalheDespesasConsolidacao(idConsolidacao, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/despesasMensais/consolidacao/associar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> associarDespesasMensaisConsolidacao(@RequestParam("idConsolidacao") Integer idConsolidacao,
                                                                    @RequestBody List<DespesasMensaisRequest> request) {
        consolidacaoBusiness.associarDespesaMensalConsolidacao(idConsolidacao, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/despesasMensais/consolidacao/desassociar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> desassociarDespesasMensaisConsolidacao(@RequestParam("idDespesa") Integer idDespesa,
                                                                       @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
                                                                       @RequestParam("idConsolidacao") Integer idConsolidacao,
                                                                       @RequestParam("idFuncionario") Integer idFuncionario) {
        consolidacaoBusiness.desassociarDespesaMensalConsolidacao(idDespesa, idDetalheDespesa, idConsolidacao, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais/observacoes/gravar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> gravarObservacoesDetalheDespesa(@RequestBody ObservacoesDetalheDespesaRequest request) {
        detalheDespesasBusiness.gravarObservacoesDetalheDespesa(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/despesasFixasMensais/gravar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> gravarDespesasFixasMensais(@RequestBody DespesasFixasMensaisRequest request) {
        lancamentosFinanceirosBusiness.gravarDespesasFixasMensais(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/lancamentosFinanceiros/despesasFixasMensais", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteDespesaFixaMensal(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idOrdem") Integer idOrdem,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        lancamentosFinanceirosBusiness.deleteDespesaFixaMensal(idDespesa, idOrdem, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/lancamentosFinanceiros/despesasMensais", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteDespesMensal(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idOrdem") Integer idOrdem,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        lancamentosFinanceirosBusiness.deleteDespesasMensais(idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteDetalheDespesasMensais(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idOrdem") Integer idOrdem,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        detalheDespesasBusiness.deleteDetalheDespesasMensais(idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/v2/lancamentosFinanceiros/detalheDespesasMensais/excluir", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteDetalheDespesasMensais(@RequestBody List<DetalheDespesasMensaisRequest> request) {
        detalheDespesasBusiness.deleteDetalheDespesasMensaisV2(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/despesasMensais/incluir", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> gravarDespesaMensal(@RequestBody DespesasMensaisRequest request) throws InvocationTargetException, IllegalAccessException {
        detalheDespesasBusiness.gravarDespesaMensal(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais/incluir", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> gravarDetalheDespesasMensais(@RequestBody List<DetalheDespesasMensaisRequest> request) {
        detalheDespesasBusiness.gravarDetalheDespesasMensais(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/despesasMensais/alterarReferenciaDespesa", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> alterarReferenciaDespesa(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idDetalheDespesaNova") Integer idDetalheDespesaNova,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        lancamentosFinanceirosBusiness.alterarDespesaMensalReferencia(idDespesa, idDetalheDespesa, idDetalheDespesaNova, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais/ordenarListaDespesas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> ordenarListaDetalheDespesasMensais(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("ordem") String ordem) {

        detalheDespesasBusiness.ordenarListaDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario, ordem);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/ordenarListaDespesas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> ordenarListaDespesasMensais(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        lancamentosFinanceirosBusiness.ordenarListaDespesasMensais(idDespesa, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais/baixarPagamentoDespesa", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> processarPagamentoDetalheDespesa(@RequestBody List<PagamentoDespesasRequest> request) {
        detalheDespesasBusiness.processarPagamentoDetalheDespesa(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/baixarPagamentoDespesa", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> processarPagamentoDespesa(@RequestBody List<LancamentosMensaisDAO> request) {

        detalheDespesasBusiness.processarPagamentoDespesa(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/desfazerPagamentoDespesa", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> desfazerPagamentoDespesa(@RequestBody List<LancamentosMensaisDAO> request) {

        detalheDespesasBusiness.desfazerPagamentoDespesas(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/lancamentosFinanceiros", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteTodosLancamentosMensais(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        importacaoBusiness.deleteTodosLancamentosMensais(idDespesa, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/importacao/processamento", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> processarImportacaoDespesasMensais(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("dsMes") String dsMes,
            @RequestParam("dsAno") String dsAno) {

        importacaoBusiness.processarImportacaoDespesasMensais(idDespesa, idFuncionario, dsMes, dsAno);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/importacao/detalheDespesasMensais", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> processarImportacaoDetalheDespesasMensais(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("dsMes") String dsMes,
            @RequestParam("dsAno") String dsAno,
            @RequestParam("bReprocessarTodosValores") Boolean bReprocessarTodosValores) {

        importacaoBusiness.processarImportacaoDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario, dsMes, dsAno, bReprocessarTodosValores);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/importacao/despesaParcelada", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> processarImportacaoDespesaParcelada(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idDespesaParcelada") Integer idDespesaParcelada,
            @RequestParam("idConsolidacao") Integer idConsolidacao,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        importacaoBusiness.processarImportacaoDespesaParcelada(idDespesa, idDetalheDespesa, idDespesaParcelada, idConsolidacao, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/importacao/despesaParceladaAmortizada", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> incluirDespesaParceladaAmortizada(
            @RequestBody List<ParcelasDAO> parcelas,
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        importacaoBusiness.incluirDespesaParceladaAmortizada(idDespesa, idDetalheDespesa, parcelas, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/parcelas/adiarFluxoParcelas", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> adiarFluxoParcelas(@RequestBody List<DetalheDespesasMensaisRequest> request) {
        despesasParceladasBusiness.adiarFluxoParcelas(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/parcelas/desfazerAdiamentoFluxoParcelas", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> desfazerAdiamentoFluxoParcelas(@RequestBody List<DetalheDespesasMensaisRequest> request) {
        despesasParceladasBusiness.desfazerAdiamentoFluxoParcelas(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/validaDespesaExistenteDebitoCartao", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> validaDespesaExistenteDebitoCartao(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        var response = lancamentosFinanceirosBusiness.validaDespesaExistenteDebitoCartao(idDespesa, idDetalheDespesa, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/lancamentosFinanceiros/validaTituloDespesaDuplicado", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> validaTituloDespesaDuplicado(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("tituloDespesa") String tituloDespesa,
            @RequestParam("anoReferencia") String anoReferencia) {

        var response = lancamentosFinanceirosBusiness.validaTituloDespesaDuplicado(idDespesa, idDetalheDespesa, idFuncionario, tituloDespesa, anoReferencia);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/lancamentosFinanceiros/alterarTituloDespesaReuso", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> alterarTituloDespesaReuso(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("novoTituloDespesa") String novoTituloDespesa) {

        var response = lancamentosFinanceirosBusiness.alterarTituloDespesaReuso(idDespesa, idDetalheDespesa, idFuncionario, novoTituloDespesa);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/lancamentosFinanceiros/alterarTituloDespesa", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> alterarTituloDespesa(
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("novoTituloDespesa") String novoTituloDespesa,
            @RequestParam("anoReferencia") String anoReferencia) {

        lancamentosFinanceirosBusiness.alterarTituloDespesa(idDetalheDespesa, idFuncionario, novoTituloDespesa, anoReferencia);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/alterarOrdemRegistroDetalheDespesas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> alterarOrdemRegistroDetalheDespesas(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("iOrdemAtual") Integer iOrdemAtual,
            @RequestParam("iOrdemNova") Integer iOrdemNova,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        detalheDespesasBusiness.alterarOrdemRegistroDetalheDespesas(idDespesa, idDetalheDespesa, iOrdemAtual, iOrdemNova, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/alterarOrdemRegistroDespesas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> alterarOrdemRegistroDespesas(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("iOrdemAtual") Integer iOrdemAtual,
            @RequestParam("iOrdemNova") Integer iOrdemNova,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        lancamentosFinanceirosBusiness.alterarOrdemRegistroDespesas(idDespesa, iOrdemAtual, iOrdemNova, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/alterarOrdemRegistroDespesasFixas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> alterarOrdemRegistroDespesasFixas(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("iOrdemAtual") Integer iOrdemAtual,
            @RequestParam("iOrdemNova") Integer iOrdemNova,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        lancamentosFinanceirosBusiness.alterarOrdemRegistroDespesasFixas(idDespesa, iOrdemAtual, iOrdemNova, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/gerarDespesasFuturas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DespesaFixaTemporariaResponse> gerarDespesasFuturas(
            @RequestParam("dsMes") Integer dsMes,
            @RequestParam("dsAno") Integer dsAno,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        var response = importacaoBusiness.gerarTemporariamenteDespesasMensais(dsMes, dsAno, idFuncionario);
        return ResponseEntity.ok(response);
    }
}

