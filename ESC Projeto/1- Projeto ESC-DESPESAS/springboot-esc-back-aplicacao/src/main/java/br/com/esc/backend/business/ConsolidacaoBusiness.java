package br.com.esc.backend.business;

import br.com.esc.backend.domain.*;
import br.com.esc.backend.exception.ErroNegocioException;
import br.com.esc.backend.repository.AplicacaoRepository;
import br.com.esc.backend.service.ConsolidacaoService;
import br.com.esc.backend.service.DetalheDespesasServices;
import br.com.esc.backend.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConsolidacaoBusiness {

    private final AplicacaoRepository repository;
    private final ConsolidacaoService consolidacaoService;
    private final DetalheDespesasServices detalheDespesasServices;
    private final LancamentosFinanceirosBusiness lancamentosFinanceirosBusiness;

    public ConsolidacaoDAO obterDetalheConsolidacao(Integer idConsolidacao, Integer idFuncionario) {
        log.info("Obtendo detalhes consolidacao id = {}", idConsolidacao);
        return consolidacaoService.getDetalheConsolidacao(idConsolidacao, idFuncionario);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void gravarConsolidacao(ConsolidacaoDAO request) {
        log.info("Gravando consolidacao >>> request: {}", request);
        consolidacaoService.gravarConsolidacao(request);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void excluirConsolidacao(ConsolidacaoDAO request) {
        log.info("Excluindo consolidacao >>> request: {}", request);
        consolidacaoService.excluirConsolidacao(request);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void associarDespesaDetalheDespesasConsolidacao(Integer idConsolidacao, List<DetalheDespesasMensaisRequest> request) {
        log.info("Associando detalhe despesa a consolidacao >>> - idConsolidacao: {} - request: {}", idConsolidacao, request);
        for (DetalheDespesasMensaisRequest detalhe : request) {
            var isDespesaExistente = repository.getDetalhesConsolidacao(idConsolidacao, detalhe.getIdFuncionario()).stream()
                    .filter(d -> d.getIdDespesaParcelada().equals(detalhe.getIdDespesaParcelada()))
                    .collect(Collectors.toList());

            if (isDespesaExistente.isEmpty()) {
                consolidacaoService.associarDespesa(Arrays.asList(ConsolidacaoDespesasRequest.builder()
                        .idConsolidacao(idConsolidacao)
                        .idDespesaParcelada(detalhe.getIdDespesaParcelada())
                        .idFuncionario(detalhe.getIdFuncionario())
                        .build()));
            }

            consolidacaoService.associarConsolidacaoDetalheDespesaMensal(detalhe.getIdDespesa(), detalhe.getIdDetalheDespesa(), idConsolidacao, detalhe.getIdFuncionario());
        }

        final var idDespesaRef = request.get(0).getIdDespesa();
        final var idFuncionarioRef = request.get(0).getIdFuncionario();

        this.lancamentosFinanceirosBusiness.ordenarListaDespesasMensais(idDespesaRef, idFuncionarioRef);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void associarDespesaMensalConsolidacao(Integer idConsolidacao, List<DespesasMensaisRequest> request) {
        log.info("Associando despesa a consolidacao >>> - idConsolidacao: {} - request: {}", idConsolidacao, request);
        for (DespesasMensaisRequest despesa : request) {
            DespesasMensaisDAO isDespesaExistente = repository.getDespesaMensalPorFiltro(despesa.getIdDespesa(), despesa.getIdDetalheDespesa(), despesa.getIdFuncionario());

            if (ObjectUtils.isEmpty(isDespesaExistente)) {
                throw new ErroNegocioException("Despesa inexistente na base de dados, não será possivel associar a um grupo de consolidacao.");
            }

            DespesasMensaisDAO mensaisDAO = new DespesasMensaisDAO();
            BeanUtils.copyProperties(mensaisDAO, despesa);

            mensaisDAO.setIdConsolidacao(idConsolidacao);
            mensaisDAO.setTpDespesaConsolidacao("N");
            detalheDespesasServices.gravarDespesasMensais(mensaisDAO);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void desassociarDespesaMensalConsolidacao(Integer idDespesa, Integer idDetalheDespesa, Integer idConsolidacao, Integer idFuncionario) {
        log.info("Desassociando despesa da consolidacao >>> - idDespesa: {} - idConsolidacao: {}", idDespesa, idConsolidacao);

        List<DespesasMensaisDAO> despesa = repository.getDespesasMensais(idDespesa, idFuncionario, idDetalheDespesa);

        if (ObjectUtils.isEmpty(despesa)) {
            throw new ErroNegocioException("Despesa inexistente na base de dados, não será possivel desassociar do grupo de consolidacao.");
        }

        DespesasMensaisDAO mensaisDAO = new DespesasMensaisDAO();
        BeanUtils.copyProperties(mensaisDAO, despesa.get(0));

        mensaisDAO.setIdConsolidacao(0);
        mensaisDAO.setTpDespesaConsolidacao("N");
        detalheDespesasServices.gravarDespesasMensais(mensaisDAO);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void desassociarDespesasConsolidacao(List<ConsolidacaoDespesasRequest> request) {
        log.info("Desassociando despesa a consolidacao >>> request: {}", request);
        consolidacaoService.desassociarDespesa(request);
    }

    public List<TituloConsolidacao> obterTituloConsolidacoes(Integer idFuncionario, Boolean tpBaixado) {
        log.info("Obtendo lista de consolidacoes - baixado : {}", tpBaixado);
        return consolidacaoService.getListaNomesConsolidacoes(idFuncionario, tpBaixado);
    }

    public TituloDespesaResponse consultarConsolidacoesParaAssociacao(Integer idFuncionario, Integer idDespesa, Integer idDetalheDespesa, String tipo) {
        log.info("Obtendo Lista de Nomes Consolidacoes para Associacao de despesas - Filtros >>> idFuncionario: {} - idDespesa: {} - idDetalheDespesa: {} - tipo: {}", idFuncionario, idDespesa, idDetalheDespesa, tipo);
        return consolidacaoService.getNomeConsolidacaoParaAssociacao(idFuncionario, idDespesa, idDetalheDespesa, tipo);
    }

    @SneakyThrows
    public StringResponse obterRelatorioDespesasParceladasConsolidadas(Integer idDespesa, Integer idDetalheDespesa, Integer idConsolidacao, Integer idFuncionario) {
        log.info("Obtendo relatorio despesas parceladas consolidadas - idDespesa: {} - idDetalheDespesa: {} - idConsolidacao: {} - idFuncionario: {}", idDespesa, idDetalheDespesa, idConsolidacao, idFuncionario);
        return consolidacaoService.obterRelatorioDespesasParceladasConsolidadas(idDespesa, idDetalheDespesa, idConsolidacao, idFuncionario);
    }

    public List<DetalheDespesasMensaisDAO> obterListDetalheDespesasConsolidadas(Integer idDespesa, Integer idDetalheDespesa, Integer idConsolidacao, Integer idFuncionario) {
        return consolidacaoService.obterListDetalheDespesasConsolidadas(idDespesa, idDetalheDespesa, idConsolidacao, idFuncionario);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void excluirConsolidacaoDetalheDespesaMensal(Integer idDespesa, Integer idDetalheDespesa, Integer idConsolidacao, Integer idFuncionario) {
        consolidacaoService.excluirConsolidacaoDetalheDespesaMensal(idDespesa, idDetalheDespesa, idConsolidacao, idFuncionario);
    }
}

