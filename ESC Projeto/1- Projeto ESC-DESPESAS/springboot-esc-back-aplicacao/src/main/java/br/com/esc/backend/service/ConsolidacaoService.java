package br.com.esc.backend.service;

import br.com.esc.backend.domain.ConsolidacaoDAO;
import br.com.esc.backend.domain.ConsolidacaoDespesasDAO;
import br.com.esc.backend.domain.DetalheDespesasMensaisDAO;
import br.com.esc.backend.domain.TituloConsolidacao;
import br.com.esc.backend.repository.AplicacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static br.com.esc.backend.service.DetalheDespesasServices.parserOrdem;
import static br.com.esc.backend.utils.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsolidacaoService {

    private final AplicacaoRepository aplicacaoRepository;

    public List<TituloConsolidacao> getListaNomesConsolidacoes(Integer idFuncionario, Boolean tpBaixado) {
        return aplicacaoRepository.getTituloConsolidacao(idFuncionario, (tpBaixado.equals(true) ? "S" : null));
    }

    public ConsolidacaoDAO getDetalheConsolidacao(Integer idConsolidacao, Integer idFuncionario) {
        var consolidacao = aplicacaoRepository.getConsolidacao(idConsolidacao, idFuncionario);

        return ConsolidacaoDAO.builder()
                .idConsolidacao(consolidacao.getIdConsolidacao())
                .dsTituloConsolidacao(consolidacao.getDsTituloConsolidacao())
                .tpBaixado(consolidacao.getTpBaixado())
                .dataCadastro(consolidacao.getDataCadastro())
                .idFuncionario(consolidacao.getIdFuncionario())
                .despesasConsolidadas(aplicacaoRepository.getDetalhesConsolidacao(idConsolidacao, idFuncionario))
                .build();
    }

    public void gravarConsolidacao(ConsolidacaoDAO consolidacaoDAO) {
        var result = aplicacaoRepository.getConsolidacao(consolidacaoDAO.getIdConsolidacao(), consolidacaoDAO.getIdFuncionario());

        if (isEmpty(result)) {
            log.info("Incluindo uma nova consolidacao...");
            aplicacaoRepository.insertConsolidacao(consolidacaoDAO);
        } else {
            log.info("Atualizando consolidacao...");
            aplicacaoRepository.updateConsolidacao(consolidacaoDAO);
        }
    }

    public void excluirConsolidacao(ConsolidacaoDAO consolidacaoDAO) {
        log.info("Excluindo a consolidacao...");
        aplicacaoRepository.deleteConsolidacao(consolidacaoDAO);

        log.info("Excluindo detalhes da consolidacao...");
        aplicacaoRepository.deleteDetalhesConsolidacao(consolidacaoDAO);

        for (ConsolidacaoDespesasDAO despesa : aplicacaoRepository.getDetalhesConsolidacao(consolidacaoDAO.getIdConsolidacao(), consolidacaoDAO.getIdFuncionario())) {
            log.info("Realizando update despesas parceladas consolidadas... >> {} ", despesa);
            aplicacaoRepository.updateDespesasParceladasAssociacao(despesa.getIdDespesaParcelada(), 0, despesa.getIdFuncionario());
        }
    }

    public void associarDespesa(ConsolidacaoDespesasDAO despesas) {
        aplicacaoRepository.associarDespesaConsolidacao(despesas);
    }

    public void desassociarDespesa(List<ConsolidacaoDespesasDAO> despesas) {
        for (ConsolidacaoDespesasDAO despesa : despesas) {
            aplicacaoRepository.updateDespesasParceladasAssociacao(despesa.getIdDespesaParcelada(), null, despesa.getIdFuncionario());
            aplicacaoRepository.desassociarDespesaConsolidacao(despesa);
        }
    }

    public void associarConsolidacaoDetalheDespesaMensal(Integer idDespesa, Integer idDetalheDespesa, Integer idConsolidacao, Integer idFuncionario) {
        for (ConsolidacaoDespesasDAO despesaConsolidada : aplicacaoRepository.getDetalhesConsolidacao(idConsolidacao, idFuncionario)) {
            var idDespesaParcelada = despesaConsolidada.getIdDespesaParcelada();
            aplicacaoRepository.updateDetalheDespesasMensaisConsolidacao(idDespesa, idDetalheDespesa, idDespesaParcelada, idConsolidacao, idFuncionario);
        }

        aplicacaoRepository.updateConsolidacaoDetalheDespesa(idConsolidacao, idDetalheDespesa, idFuncionario);
    }

    public void excluirConsolidacaoDetalheDespesaMensal(Integer idDespesa, Integer idDetalheDespesa, Integer idConsolidacao, Integer idFuncionario) {
        for (ConsolidacaoDespesasDAO despesaConsolidada : aplicacaoRepository.getDetalhesConsolidacao(idConsolidacao, idFuncionario)) {
            var idDespesaParcelada = despesaConsolidada.getIdDespesaParcelada();
            aplicacaoRepository.updateDetalheDespesasMensaisConsolidacao(idDespesa, idDetalheDespesa, idDespesaParcelada, 0, idFuncionario);
        }

        if (aplicacaoRepository.getValidaDetalheDespesaComConsolidacao(idConsolidacao, idFuncionario).equals("N")) {
            aplicacaoRepository.updateConsolidacaoDetalheDespesa(idConsolidacao, null, idFuncionario);
        }
    }

    public List<DetalheDespesasMensaisDAO> obterListDetalheDespesasConsolidadas(Integer idDespesa, Integer idDetalheDespesa, Integer idConsolidacao, Integer idFuncionario) {
        return aplicacaoRepository.getDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario, parserOrdem(null)).stream()
                .filter(d -> d.getIdDespesaConsolidacao() == idConsolidacao)
                .collect(Collectors.toList());
    }

}
