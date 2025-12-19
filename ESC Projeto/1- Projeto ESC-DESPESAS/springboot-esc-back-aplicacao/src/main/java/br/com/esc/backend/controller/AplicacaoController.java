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
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AplicacaoController {

    private final LancamentosBusinessService service;

    @GetMapping(path = "/lancamentosFinanceiros/consultar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LancamentosFinanceirosDTO> obterLancamentosFinanceiros(
            @RequestParam("dsMes") String dsMes,
            @RequestParam("dsAno") String dsAno,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        var response = service.obterLancamentosFinanceiros(dsMes, dsAno, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosMensais/consolidados/consultar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LancamentosMensaisDAO>> obterLancamentosMensais(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idConsolidacao") Integer idConsolidacao,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        var response = service.obterDespesasMensaisConsolidadas(idDespesa, idConsolidacao, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais/consultar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DetalheDespesasMensaisDTO> obterDetalheDespesasMensais(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("ordem") String ordem,
            @RequestParam("visualizarConsolidacao") Boolean visualizarConsolidacao) {
        var response = service.obterDetalheDespesaMensal(idDespesa, idDetalheDespesa, idFuncionario, ordem, visualizarConsolidacao);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/categoriaDespesa/subTotal", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoriaDespesasResponse> obterSubTotalCategoriaDespesa(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = service.obterSubTotalCategoriaDespesa(idDespesa, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/obterNovaChaveKey", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChaveKeyDAO> obterNovaChaveKey(
            @RequestParam("tipoChave") String tipoChave) {
        var response = service.retornaNovaChaveKey(tipoChave);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/obterMesAnoPorID", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> obterMesAnoPorID(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = service.obterMesAnoPorID(idDespesa, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/obterSubTotalDespesa", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> obterSubTotalDespesa(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("ordem") String ordem) {
        var response = service.obterSubTotalDespesa(idDespesa, idDetalheDespesa, idFuncionario, ordem);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais/obterExtratoDespesasMes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExtratoDespesasDAO> obterExtratoDespesasMes(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("tipo") String tipo) {
        var response = service.obterExtratoDespesasMes(idDespesa, idDetalheDespesa, idFuncionario, tipo);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais/obterDespesasMensaisParaAssociacao", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TituloDespesaResponse> consultarDespesasMensaisParaAssociacao(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("anoReferencia") Integer anoReferencia) {
        var response = service.consultarDespesasMensaisParaAssociacao(idDespesa, idFuncionario, anoReferencia);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/obterTitulosDespesas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TituloDespesaResponse> obterTitulosDespesas(
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = service.obterTitulosDespesas(idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/obterTitulosEmprestimos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TituloDespesaResponse> obterTitulosEmprestimos(
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = service.obterTitulosEmprestimos(idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais/observacoes/consultar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> obterObservacoesDetalheDespesasMensais(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idObservacao") Integer idObservacao,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = service.obterObservacoesDetalheDespesa(idDespesa, idDetalheDespesa, idObservacao, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/obterTitulosDespesasRelatorio", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TituloDespesaResponse> obterTitulosDespesasRelatorio(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = service.obterTitulosDespesasRelatorio(idDespesa, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais/historico/consultar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> obterHistoricoDetalheDespesasMensais(
            @RequestParam("idDetalheDespesaLog") Integer idDetalheDespesaLog,
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = service.obterHistoricoDetalheDespesa(idDetalheDespesaLog, idDespesa, idDetalheDespesa, idFuncionario);
        return ResponseEntity.ok(response);
    }

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

    @GetMapping(path = "/v2/despesasParceladas/consultar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DetalheDespesasParceladasResponse> obterDespesaParceladaPorID(
            @RequestParam("idDespesaParcelada") Integer idDespesaParcelada,
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("isPendentes") Boolean isPendentes) {
        var response = service.obterDespesaParceladaPorID(idDespesaParcelada, idFuncionario, isPendentes);
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

    @GetMapping(path = "/consolidacao/importacao/consultarConsolidacoes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TituloDespesaResponse> consultarConsolidacoesParaAssociacao(
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("tipo") String tipo) {
        var response = service.consultarConsolidacoesParaAssociacao(idFuncionario, idDespesa, idDetalheDespesa, tipo);
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

    @GetMapping(path = "/detalheDespesas/despesasParceladas/obterRelatorioDespesasParceladasQuitacao", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> obterRelatorioDespesasParceladasQuitacao(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = service.obterRelatorioDespesasParceladasQuitacao(idDespesa, idDetalheDespesa, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/detalheDespesas/consolidacao/obterRelatorioDespesasParceladas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> obterRelatorioDespesasParceladasConsolidadas(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idConsolidacao") Integer idConsolidacao,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = service.obterRelatorioDespesasParceladasConsolidadas(idDespesa, idDetalheDespesa, idConsolidacao, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/parametros/obterConfiguracaoLancamentos/usuario", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConfiguracaoLancamentosResponse> obterConfiguracaoLancamentos(
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = service.obterConfiguracaoLancamentos(idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/despesasParceladas/obterParcelasParaAmortizacao", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ParcelasDAO>> obterParcelasParaAmortizacao(
            @RequestParam("idDespesaParcelada") Integer idDespesaParcelada,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = service.obterParcelasParaAmortizacao(idDespesaParcelada, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lembretes/detalhe", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LembretesDAO> obterDetalhesLembrete(
            @RequestParam("idLembrete") Integer idLembrete,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = service.obterDetalheLembrete(idLembrete, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lembretes/monitor", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TituloLembretesDAO>> obterMonitorLembretesEmAberto(
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = service.obterListaMonitorLembretes(idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/lembretes/obterTituloLembretes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TituloLembretesDAO>> obterTituloLembretes(
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("tpBaixado") Boolean tpBaixado) {
        var response = service.obterTituloLembretes(idFuncionario, tpBaixado);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/sessao/validar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> validaSessaoUsuario(
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = service.validaSessaoUsuario(idFuncionario);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/consolidacao/obterTituloConsolidacoes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TituloConsolidacao>> obterTituloConsolidacoes(
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("tpBaixado") Boolean tpBaixado) {
        var response = service.obterTituloConsolidacoes(idFuncionario, tpBaixado);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/consolidacao/consultar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConsolidacaoDAO> obterDetalhesConsolidacao(
            @RequestParam("idConsolidacao") Integer idConsolidacao,
            @RequestParam("idFuncionario") Integer idFuncionario) {
        var response = service.obterDetalheConsolidacao(idConsolidacao, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/consolidacao/gravar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> gravarConsolidacao(@RequestBody ConsolidacaoDAO request) {
        service.gravarConsolidacao(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/consolidacao/excluir", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> excluirConsolidacao(@RequestBody ConsolidacaoDAO request) {
        service.excluirConsolidacao(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/consolidacao/despesas/desassociar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> desassociarDespesaConsolidacao(@RequestBody List<ConsolidacaoDespesasRequest> request) {
        service.desassociarDespesasConsolidacao(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais/consolidacao/associar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> associarDetalheDespesasConsolidacao(
            @RequestParam("idConsolidacao") Integer idConsolidacao,
            @RequestBody List<DetalheDespesasMensaisRequest> request) {

        service.associarDespesaDetalheDespesasConsolidacao(idConsolidacao, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/despesasMensais/consolidacao/associar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> associarDespesasMensaisConsolidacao(@RequestParam("idConsolidacao") Integer idConsolidacao,
                                                                    @RequestBody List<DespesasMensaisRequest> request) {
        service.associarDespesaMensalConsolidacao(idConsolidacao, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/despesasMensais/consolidacao/desassociar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> desassociarDespesasMensaisConsolidacao(@RequestParam("idDespesa") Integer idDespesa,
                                                                       @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
                                                                       @RequestParam("idConsolidacao") Integer idConsolidacao,
                                                                       @RequestParam("idFuncionario") Integer idFuncionario) {
        service.desassociarDespesaMensalConsolidacao(idDespesa, idDetalheDespesa, idConsolidacao, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais/observacoes/gravar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> gravarObservacoesDetalheDespesa(@RequestBody ObservacoesDetalheDespesaRequest request) {
        service.gravarObservacoesDetalheDespesa(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lembretes/monitor/baixar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> baixarLembreteMonitor(
            @RequestBody List<TituloLembretesDAO> request,
            @RequestParam("tipoBaixa") String tipoBaixa) {

        service.baixarLembretesMonitor(request, tipoBaixa);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lembretes/detalhe/gravar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> gravarLembrete(@RequestBody LembretesDAO request) {
        service.gravarLembrete(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lembretes/detalhe/excluir", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> exluirLembrete(@RequestBody LembretesDAO request) {
        service.excluirLembrete(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/parametros/gravar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> gravarParametrosSistema(@RequestBody ConfiguracaoLancamentosRequest request) {
        service.gravarConfiguracoesLancamentos(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/despesasParceladas/gravar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> gravarDespesasParceladas(@RequestBody DespesaParceladaDAO request) {
        service.gravarDespesaParcelada(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/despesasParceladas/parcelas/gravar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> gravarParcela(@RequestBody List<ParcelasDAO> request) {
        service.gravarParcela(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/despesasParceladas/parcelas/excluir", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping(path = "/lancamentosFinanceiros/despesasFixasMensais/gravar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> gravarDespesasFixasMensais(@RequestBody DespesasFixasMensaisRequest request) {
        service.gravarDespesasFixasMensais(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/lancamentosFinanceiros/despesasFixasMensais", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteDespesaFixaMensal(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idOrdem") Integer idOrdem,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        service.deleteDespesaFixaMensal(idDespesa, idOrdem, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/lancamentosFinanceiros/despesasMensais", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteDespesMensal(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idOrdem") Integer idOrdem,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        service.deleteDespesasMensais(idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteDetalheDespesasMensais(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idOrdem") Integer idOrdem,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        service.deleteDetalheDespesasMensais(idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/v2/lancamentosFinanceiros/detalheDespesasMensais/excluir", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteDetalheDespesasMensais(@RequestBody List<DetalheDespesasMensaisRequest> request) {
        service.deleteDetalheDespesasMensaisV2(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/despesasMensais/incluir", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> gravarDespesaMensal(@RequestBody DespesasMensaisRequest request) throws InvocationTargetException, IllegalAccessException {
        service.gravarDespesaMensal(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais/incluir", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> gravarDetalheDespesasMensais(@RequestBody List<DetalheDespesasMensaisRequest> request) {
        service.gravarDetalheDespesasMensais(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/despesasMensais/alterarReferenciaDespesa", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> alterarReferenciaDespesa(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idDetalheDespesaNova") Integer idDetalheDespesaNova,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        service.alterarDespesaMensalReferencia(idDespesa, idDetalheDespesa, idDetalheDespesaNova, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais/ordenarListaDespesas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> ordenarListaDetalheDespesasMensais(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("ordem") String ordem) {

        service.ordenarListaDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario, ordem);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/ordenarListaDespesas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> ordenarListaDespesasMensais(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        service.ordenarListaDespesasMensais(idDespesa, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/detalheDespesasMensais/baixarPagamentoDespesa", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> processarPagamentoDetalheDespesa(@RequestBody List<PagamentoDespesasRequest> request) {
        service.processarPagamentoDetalheDespesa(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/baixarPagamentoDespesa", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> processarPagamentoDespesa(@RequestBody List<LancamentosMensaisDAO> request) {

        service.processarPagamentoDespesa(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/desfazerPagamentoDespesa", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> desfazerPagamentoDespesa(@RequestBody List<LancamentosMensaisDAO> request) {

        service.desfazerPagamentoDespesas(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/lancamentosFinanceiros", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteTodosLancamentosMensais(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        service.deleteTodosLancamentosMensais(idDespesa, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/importacao/processamento", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> processarImportacaoDespesasMensais(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("dsMes") String dsMes,
            @RequestParam("dsAno") String dsAno) {

        service.processarImportacaoDespesasMensais(idDespesa, idFuncionario, dsMes, dsAno);
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

        service.processarImportacaoDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario, dsMes, dsAno, bReprocessarTodosValores);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/importacao/despesaParcelada", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> processarImportacaoDespesaParcelada(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idDespesaParcelada") Integer idDespesaParcelada,
            @RequestParam("idConsolidacao") Integer idConsolidacao,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        service.processarImportacaoDespesaParcelada(idDespesa, idDetalheDespesa, idDespesaParcelada, idConsolidacao, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/importacao/despesaParceladaAmortizada", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> incluirDespesaParceladaAmortizada(
            @RequestBody List<ParcelasDAO> parcelas,
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        service.incluirDespesaParceladaAmortizada(idDespesa, idDetalheDespesa, parcelas, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/parcelas/adiarFluxoParcelas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> adiarFluxoParcelas(@RequestBody List<DetalheDespesasMensaisRequest> request) {
        service.adiarFluxoParcelas(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/parcelas/desfazerAdiamentoFluxoParcelas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> desfazerAdiamentoFluxoParcelas(@RequestBody List<DetalheDespesasMensaisRequest> request) {
        service.desfazerAdiamentoFluxoParcelas(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/validaDespesaExistenteDebitoCartao", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> validaDespesaExistenteDebitoCartao(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        var response = service.validaDespesaExistenteDebitoCartao(idDespesa, idDetalheDespesa, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/lancamentosFinanceiros/validaTituloDespesaDuplicado", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> validaTituloDespesaDuplicado(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("tituloDespesa") String tituloDespesa) {

        var response = service.validaTituloDespesaDuplicado(idDespesa, idDetalheDespesa, idFuncionario, tituloDespesa);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/lancamentosFinanceiros/alterarTituloDespesaReuso", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> alterarTituloDespesaReuso(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("novoTituloDespesa") String novoTituloDespesa) {

        var response = service.alterarTituloDespesaReuso(idDespesa, idDetalheDespesa, idFuncionario, novoTituloDespesa);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/lancamentosFinanceiros/alterarTituloDespesa", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> alterarTituloDespesa(
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("idFuncionario") Integer idFuncionario,
            @RequestParam("novoTituloDespesa") String novoTituloDespesa,
            @RequestParam("anoReferencia") String anoReferencia) {

        service.alterarTituloDespesa(idDetalheDespesa, idFuncionario, novoTituloDespesa, anoReferencia);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/alterarOrdemRegistroDetalheDespesas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> alterarOrdemRegistroDetalheDespesas(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("idDetalheDespesa") Integer idDetalheDespesa,
            @RequestParam("iOrdemAtual") Integer iOrdemAtual,
            @RequestParam("iOrdemNova") Integer iOrdemNova,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        service.alterarOrdemRegistroDetalheDespesas(idDespesa, idDetalheDespesa, iOrdemAtual, iOrdemNova, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/alterarOrdemRegistroDespesas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> alterarOrdemRegistroDespesas(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("iOrdemAtual") Integer iOrdemAtual,
            @RequestParam("iOrdemNova") Integer iOrdemNova,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        service.alterarOrdemRegistroDespesas(idDespesa, iOrdemAtual, iOrdemNova, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/alterarOrdemRegistroDespesasFixas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> alterarOrdemRegistroDespesasFixas(
            @RequestParam("idDespesa") Integer idDespesa,
            @RequestParam("iOrdemAtual") Integer iOrdemAtual,
            @RequestParam("iOrdemNova") Integer iOrdemNova,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        service.alterarOrdemRegistroDespesasFixas(idDespesa, iOrdemAtual, iOrdemNova, idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/lancamentosFinanceiros/gerarDespesasFuturas", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DespesaFixaTemporariaResponse> gerarDespesasFuturas(
            @RequestParam("dsMes") Integer dsMes,
            @RequestParam("dsAno") Integer dsAno,
            @RequestParam("idFuncionario") Integer idFuncionario) {

        var response = service.gerarTemporariamenteDespesasMensais(dsMes, dsAno, idFuncionario);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/backup/processar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> processarBackup() {
        var response = service.processarBackupBaseDados();
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/login/autenticar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AutenticacaoResponse> autenticarLogin(@RequestBody LoginRequest request) {
        var response = service.autenticarUsuario(request);
        if (response.getIdLogin().equals(-1)) {
            return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/login/limparDadosTemporarios", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> limparDadosTemporarios(
            @RequestParam("idFuncionario") Integer idFuncionario) {

        service.limparDadosTemporarios(idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
