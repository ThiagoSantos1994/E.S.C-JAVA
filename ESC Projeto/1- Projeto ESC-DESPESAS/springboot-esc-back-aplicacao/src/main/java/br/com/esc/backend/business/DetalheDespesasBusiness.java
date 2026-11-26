package br.com.esc.backend.business;

import br.com.esc.backend.config.RetryOnDeadlock;
import br.com.esc.backend.domain.*;
import br.com.esc.backend.exception.ErroNegocioException;
import br.com.esc.backend.facade.DespesasParceladasFacade;
import br.com.esc.backend.repository.AplicacaoRepository;
import br.com.esc.backend.service.ConsolidacaoService;
import br.com.esc.backend.service.DetalheDespesasServices;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static br.com.esc.backend.service.DetalheDespesasServices.isDetalheDespesaTipoRelatorio;
import static br.com.esc.backend.utils.MotorCalculoUtils.convertStringToDecimal;
import static br.com.esc.backend.utils.MotorCalculoUtils.convertToMoedaBR;

@Component
@RequiredArgsConstructor
@Slf4j
public class DetalheDespesasBusiness {

    private final AplicacaoRepository repository;
    private final DetalheDespesasServices detalheDespesasServices;
    private final ConsolidacaoService consolidacaoService;
    private final ChaveKeyBusiness chaveKeyBusiness;
    private final DespesasParceladasFacade despesasParceladasFacade;

    @SneakyThrows
    public DetalheDespesasMensaisDTO obterDetalheDespesaMensal(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String ordem, Boolean visualizarConsolidacao) {
        log.info("Consultando detalhes despesa mensal >>>  idDespesa = {} - idDetalheDespesa = {} - idFuncionario = {} - ordem = {}", idDespesa, idDetalheDespesa, idFuncionario, ordem);
        return detalheDespesasServices.obterDetalheDespesaMensal(idDespesa, idDetalheDespesa, idFuncionario, ordem, visualizarConsolidacao);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void ordenarListaDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String ordem) {
        detalheDespesasServices.ordenarListaDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario, ordem);
    }

    @RetryOnDeadlock
    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void deleteDetalheDespesasMensais(Integer idDespesa, Integer idDetalheDespesa, Integer idOrdem, Integer idFuncionario) {
        log.info("Excluindo detalhe despesa mensal - request: idDespesa= {} - idDetalheDespesa= {} - idOrdem= {} - idFuncionario= {}", idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
        detalheDespesasServices.deleteDetalheDespesasMensais(idDespesa, idDetalheDespesa, idOrdem, idFuncionario);
    }

    @RetryOnDeadlock
    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void deleteDetalheDespesasMensaisV2(List<DetalheDespesasMensaisRequest> request) {
        for (DetalheDespesasMensaisRequest detalhe : request) {
            log.info("Excluindo detalhe despesa mensal - request: {}", detalhe);
            detalheDespesasServices.deleteDetalheDespesasMensais(detalhe.getIdDespesa(), detalhe.getIdDetalheDespesa(), detalhe.getIdOrdem(), detalhe.getIdFuncionario());

            if (detalhe.getIdConsolidacao() > 0) {
                log.info("Removendo a consolidacao das despesas parceladas importadas que foram consolidadas.. idConsolidacao: {}", detalhe.getIdConsolidacao());
                consolidacaoService.excluirConsolidacaoDetalheDespesaMensal(detalhe.getIdDespesa(), detalhe.getIdDetalheDespesa(), detalhe.getIdConsolidacao(), detalhe.getIdFuncionario());
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void gravarDespesaMensal(DespesasMensaisRequest request) {
        DespesasMensaisDAO mensaisDAO = new DespesasMensaisDAO();
        BeanUtils.copyProperties(mensaisDAO, request);

        if ((request.getTpLinhaSeparacao().equalsIgnoreCase("S") || request.getTpDespesaConsolidacao().equalsIgnoreCase("S"))
                && request.getIdDetalheDespesa().equals(-1)) {
            mensaisDAO.setIdDetalheDespesa(chaveKeyBusiness.retornaNovaChaveKey("DETALHEDESPESA").getNovaChave());
        }
        detalheDespesasServices.gravarDespesasMensais(mensaisDAO);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void gravarDetalheDespesasMensais(List<DetalheDespesasMensaisRequest> request) {
        for (DetalheDespesasMensaisRequest detalhe : request) {
            DetalheDespesasMensaisDAO dao = new DetalheDespesasMensaisDAO();
            BeanUtils.copyProperties(dao, detalhe);

            if (this.isProcessarDespesaParceladaAutomatica(detalhe)) {
                log.info("Geracao de despesa parcelada automatica realizada com sucesso. Detalhe despesa mensal não será gravado por este fluxo.");
                continue;
            }

            detalheDespesasServices.gravarDetalheDespesasMensais(dao);
            if (detalhe.getIdConsolidacao() > 0) {
                for (DetalheDespesasMensaisDAO despesa : consolidacaoService.obterListDetalheDespesasConsolidadas(detalhe.getIdDespesa(), detalhe.getIdDetalheDespesa(), detalhe.getIdConsolidacao(), detalhe.getIdFuncionario())) {
                    var despesaRequest = detalheDespesasServices.parserToDetalheDespesasConsolidadas(detalhe, despesa);
                    detalheDespesasServices.gravarDetalheDespesasMensais(despesaRequest);
                }
            }
        }
    }

    @RetryOnDeadlock
    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void processarPagamentoDetalheDespesa(List<PagamentoDespesasRequest> request) {
        for (PagamentoDespesasRequest despesa : request) {
            log.info("Processando pagamento detalhe despesa mensal - Request: {}", despesa);
            detalheDespesasServices.baixarPagamentoDespesa(despesa);
            detalheDespesasServices.baixarPagamentoDespesasConsolidadas(despesa);
        }
    }

    @RetryOnDeadlock
    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void processarDesfazerPagamentoDetalheDespesa(List<PagamentoDespesasRequest> request) {
        for (PagamentoDespesasRequest despesa : request) {
            log.info("Processando rollback pagamento detalhe despesa mensal - Request: {}", despesa);
            detalheDespesasServices.desfazerBaixaPagamentoDespesas(despesa);
            detalheDespesasServices.desfazerBaixaPagamentoDespesasConsolidadas(despesa);
        }
    }

    @RetryOnDeadlock
    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void processarPagamentoDespesa(List<LancamentosMensaisDAO> lancamentosMensaisList) {
        for (LancamentosMensaisDAO despesa : lancamentosMensaisList) {
            var isDespesaConsolidacao = despesa.getTpDespesaConsolidacao().equals("S");

            if (isDespesaConsolidacao) {
                List<LancamentosMensaisDAO> lancamentosMensais = repository.getLancamentosMensaisConsolidados(despesa.getIdDespesa(), despesa.getIdConsolidacao(), despesa.getIdFuncionario());
                for (LancamentosMensaisDAO mensaisDAO : lancamentosMensais) {
                    this.processarPagamentoDetalheDespesa(detalheDespesasServices.obterDetalheDespesasParaPagamento(mensaisDAO));
                }
            } else {
                this.processarPagamentoDetalheDespesa(detalheDespesasServices.obterDetalheDespesasParaPagamento(despesa));
            }
        }
    }

    @RetryOnDeadlock
    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void desfazerPagamentoDespesas(List<LancamentosMensaisDAO> lancamentosMensaisList) {
        for (LancamentosMensaisDAO despesa : lancamentosMensaisList) {
            var isDespesaConsolidacao = despesa.getTpDespesaConsolidacao().equals("S");

            if (isDespesaConsolidacao) {
                List<LancamentosMensaisDAO> lancamentosMensais = repository.getLancamentosMensaisConsolidados(despesa.getIdDespesa(), despesa.getIdConsolidacao(), despesa.getIdFuncionario());
                for (LancamentosMensaisDAO mensaisDAO : lancamentosMensais) {
                    this.processarDesfazerPagamentoDetalheDespesa(detalheDespesasServices.obterDetalheDespesasParaDesfazerPagamento(mensaisDAO));
                }
            } else {
                this.processarDesfazerPagamentoDetalheDespesa(detalheDespesasServices.obterDetalheDespesasParaDesfazerPagamento(despesa));
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void alterarOrdemRegistroDetalheDespesas(Integer idDespesa, Integer idDetalheDespesa, Integer iOrdemAtual, Integer iOrdemNova, Integer idFuncionario) {
        log.info("Alterando ordem registros DetalheDespesasMensais - Filtros: idDespesa = {}, idDetalheDespesa = {}, iOrdemAtual = {}, iOrdemNova = {}, idFuncionario = {}", idDespesa, idDetalheDespesa, iOrdemAtual, iOrdemNova, idFuncionario);
        detalheDespesasServices.alterarOrdemRegistroDetalheDespesas(idDespesa, idDetalheDespesa, isDetalheDespesaTipoRelatorio(iOrdemAtual), iOrdemNova, idFuncionario);
    }

    @SneakyThrows
    public StringResponse obterSubTotalDespesa(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String ordem) {
        log.info("Consultando subtotal despesa >> idDespesa: {}", idDespesa);
        return detalheDespesasServices.obterSubTotalDespesa(idDespesa, idDetalheDespesa, idFuncionario, ordem);
    }

    @SneakyThrows
    public ExtratoDespesasDAO obterExtratoDespesasMes(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario, String tipo) {
        return detalheDespesasServices.obterExtratoDespesasMes(idDespesa, idDetalheDespesa, idFuncionario, tipo);
    }

    public StringResponse obterObservacoesDetalheDespesa(Integer idDespesa, Integer idDetalheDespesa, Integer idObservacao, Integer idFuncionario) {
        log.info("Consultando observações dos detalhes da despesa");
        return detalheDespesasServices.getObservacoesDetalheDespesa(idDespesa, idDetalheDespesa, idObservacao, idFuncionario);
    }

    public StringResponse obterHistoricoDetalheDespesa(Integer idDetalheDespesaLog, Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario) {
        return detalheDespesasServices.getHistoricoDetalheDespesa(idDetalheDespesaLog, idDespesa, idDetalheDespesa, idFuncionario);
    }

    public void gravarObservacoesDetalheDespesa(ObservacoesDetalheDespesaRequest request) {
        log.info("Gravando observações detalhe despesa >> Request: {}", request);
        detalheDespesasServices.gravarObservacoesDetalheDespesa(request);
    }

    public CategoriaDespesasResponse obterSubTotalCategoriaDespesa(Integer idDespesa, Integer idFuncionario) {
        log.info("Consultando subTotal categorias despesa >>> idDespesa = {} - idFuncionario= {}", idDespesa, idFuncionario);
        return CategoriaDespesasResponse.builder()
                .mesAnoReferencia(repository.getMesAnoPorID(idDespesa, idFuncionario))
                .categorias(detalheDespesasServices.getSubTotalCategoriaDespesa(idDespesa, idFuncionario))
                .build();
    }

    public CategoriaDespesasResponse obterSubTotalCategoriaDespesaAno(Integer dsAno, Integer idFuncionario) {
        log.info("Consultando subTotal categorias despesa >>> dsAno = {} - idFuncionario= {}", dsAno, idFuncionario);

        List<CategoriaDespesasDAO> categoriasList = detalheDespesasServices.getSubTotalCategoriaDespesaAno(dsAno, idFuncionario);
        categoriasList.add(new CategoriaDespesasDAO("_______________", "_______________"));
        categoriasList.add(new CategoriaDespesasDAO("Receita ANO", convertToMoedaBR(repository.getCalculoReceitaPositivaANO(dsAno, idFuncionario))));
        categoriasList.add(new CategoriaDespesasDAO("Despesa ANO", convertToMoedaBR(repository.getCalculoReceitaNegativaANO(dsAno, idFuncionario))));
        categoriasList.add(new CategoriaDespesasDAO("_______________", "_______________"));
        categoriasList.add(new CategoriaDespesasDAO("Poupança (+)", convertToMoedaBR(repository.getCalculoPoupancaPositivaANO(dsAno, idFuncionario))));
        categoriasList.add(new CategoriaDespesasDAO("Poupança (-)", convertToMoedaBR(repository.getCalculoPoupancaNegativaANO(dsAno, idFuncionario))));

        return CategoriaDespesasResponse.builder()
                .categorias(categoriasList)
                .build();
    }

    public TituloDespesaResponse consultarDespesasMensaisParaAssociacao(Integer idDespesa, Integer idFuncionario, Integer anoReferencia) {
        log.info("Obtendo Lista de Despesas Mensais para Associacao - Filtros >>> idDespesa: {} - idFuncionario: {} - anoReferencia: {}", idDespesa, idFuncionario, anoReferencia);
        return detalheDespesasServices.getNomeDespesasMensaisParaAssociacao(idDespesa, idFuncionario, anoReferencia);
    }

    public List<PagamentoDespesasRequest> obterDetalheDespesasParaPagamento(LancamentosMensaisDAO despesa) {
        return detalheDespesasServices.obterDetalheDespesasParaPagamento(despesa);
    }

    public List<PagamentoDespesasRequest> obterDetalheDespesasParaDesfazerPagamento(LancamentosMensaisDAO despesa) {
        return detalheDespesasServices.obterDetalheDespesasParaDesfazerPagamento(despesa);
    }

    private boolean isProcessarDespesaParceladaAutomatica(DetalheDespesasMensaisRequest request) {
        log.info("Validando se a despesa informada possui indicador de geração de despesa parcelada automática.");

        var descricao = request.getDsDescricao();

        if (descricao == null || descricao.isBlank()) {
            log.warn("Descricao vazia ou nula, Caracteriza uma possivel linha de separacao.");
            return false;
        }

        var mesAno = repository.getMesAnoPorID(request.getIdDespesa(), request.getIdFuncionario());

        //Valida se contem divisao na descrição da despesa.
        Pattern divisaoValor = Pattern.compile("^/(\\d+)X\\s*(.*)$");
        Matcher divisor = divisaoValor.matcher(descricao.trim());

        if (divisor.matches()) {
            var quantidade = Integer.parseInt(divisor.group(1));
            var titulo = divisor.group(2).trim();

            if (quantidade <= 0) {
                throw new ErroNegocioException("A quantidade de parcelas informada é inválida.");
            }

            var valorTotal = convertStringToDecimal(request.getVlTotal());

            var valorParcela = valorTotal.divide(
                    BigDecimal.valueOf(quantidade),
                    2,
                    RoundingMode.HALF_UP
            );

            var despesaRequest = DespesasParceladasRequest.builder()
                    .nomeDespesaParcelada(titulo)
                    .vigenciaInicial(mesAno)
                    .qtdeParcelas(quantidade)
                    .valorParcela(convertToMoedaBR(valorParcela))
                    .detalheDespesasMensais(request)
                    .build();

            log.info("Despesas: valor total dividido em {} parcelas.", quantidade);
            log.info("Valor de cada parcela: {}", valorParcela);

            despesasParceladasFacade
                    .processarDespesaParceladaAutomatica(despesaRequest);

            return true;
        }

        // Valida se contem quantidade de parcelas na descrição da despesa.
        Pattern quantidadeParcelas = Pattern.compile("^(\\d+)X\\s*(.*)$");
        Matcher parcelas = quantidadeParcelas.matcher(descricao.trim());

        if (parcelas.matches()) {
            int quantidade = Integer.parseInt(parcelas.group(1));
            String titulo = parcelas.group(2).trim();

            log.info("Quantidade de parcelas: {}", quantidade);
            log.info("Descrição sem quantidade: {}", titulo);

            DespesasParceladasRequest despesaRequest =
                    DespesasParceladasRequest.builder()
                            .nomeDespesaParcelada(titulo)
                            .vigenciaInicial(mesAno)
                            .qtdeParcelas(quantidade)
                            .valorParcela(request.getVlTotal())
                            .detalheDespesasMensais(request)
                            .build();

            despesasParceladasFacade
                    .processarDespesaParceladaAutomatica(despesaRequest);

            return true;
        }

        log.info("Não foi localizado indicador de parcelas na descrição. Texto completo: {}", descricao);
        return false;
    }

}

