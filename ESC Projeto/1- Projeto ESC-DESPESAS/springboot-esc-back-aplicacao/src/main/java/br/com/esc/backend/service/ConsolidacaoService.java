package br.com.esc.backend.service;

import br.com.esc.backend.domain.*;
import br.com.esc.backend.repository.AplicacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
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
        return aplicacaoRepository.getTituloConsolidacao(idFuncionario, (tpBaixado.equals(true) ? "tp_Baixado IS NOT NULL" : "tp_Baixado = 'N'"));
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
        for (ConsolidacaoDespesasResponse despesa : aplicacaoRepository.getDetalhesConsolidacao(consolidacaoDAO.getIdConsolidacao(), consolidacaoDAO.getIdFuncionario())) {
            log.info("Desassociando a consolidacao da despesa parcelada... >> {} ", despesa);
            aplicacaoRepository.updateDespesasParceladasConsolidacao(despesa.getIdDespesaParcelada(), 0, despesa.getIdFuncionario());
        }

        log.info("Excluindo despesa consolidada dos detalheDespesasMensais...");
        aplicacaoRepository.deleteDetalheDespesasMensaisConsolidacao(consolidacaoDAO.getIdConsolidacao(), consolidacaoDAO.getIdFuncionario());

        log.info("Desassociando detalheDespesasMensais consolidadas...");
        aplicacaoRepository.updateDetalheDespesasMensaisDesassociacao(consolidacaoDAO.getIdConsolidacao(), null, consolidacaoDAO.getIdFuncionario());

        log.info("Excluindo detalhes da consolidacao...");
        aplicacaoRepository.deleteDetalhesConsolidacao(consolidacaoDAO);

        log.info("Excluindo a consolidacao...");
        aplicacaoRepository.deleteConsolidacao(consolidacaoDAO);
    }

    public void associarConsolidacaoDetalheDespesaMensal(Integer idDespesa, Integer idDetalheDespesa, Integer idConsolidacao, Integer idFuncionario) {
        for (ConsolidacaoDespesasResponse despesaConsolidada : aplicacaoRepository.getDetalhesConsolidacao(idConsolidacao, idFuncionario)) {
            var idDespesaParcelada = despesaConsolidada.getIdDespesaParcelada();
            //Atualiza com o ID da consolidacacao a despesa atual e das seguintes que ja estão importadas
            aplicacaoRepository.updateDetalheDespesasMensaisConsolidacao(idDespesa, idDetalheDespesa, idDespesaParcelada, idConsolidacao, idFuncionario);
        }

        aplicacaoRepository.updateConsolidacaoDespesa(idConsolidacao, idDetalheDespesa, idFuncionario);
    }

    public void associarDespesa(List<ConsolidacaoDespesasRequest> despesas) {
        for (ConsolidacaoDespesasRequest despesa : despesas) {
            aplicacaoRepository.insertDespesaConsolidacao(despesa);
            aplicacaoRepository.updateDespesasParceladasConsolidacao(despesa.getIdDespesaParcelada(), despesa.getIdConsolidacao(), despesa.getIdFuncionario());
        }
    }

    public void desassociarDespesa(List<ConsolidacaoDespesasRequest> despesas) {
        for (ConsolidacaoDespesasRequest despesa : despesas) {
            aplicacaoRepository.deleteDespesaConsolidacao(despesa);
            aplicacaoRepository.updateDespesasParceladasConsolidacao(despesa.getIdDespesaParcelada(), null, despesa.getIdFuncionario());
            aplicacaoRepository.updateDetalheDespesasMensaisDesassociacao(despesa.getIdConsolidacao(), despesa.getIdDespesaParcelada(), despesa.getIdFuncionario());
        }
    }

    public void excluirConsolidacaoDetalheDespesaMensal(Integer idDespesa, Integer idDetalheDespesa, Integer idConsolidacao, Integer idFuncionario) {
        for (ConsolidacaoDespesasResponse despesaConsolidada : aplicacaoRepository.getDetalhesConsolidacao(idConsolidacao, idFuncionario)) {
            var idDespesaParcelada = despesaConsolidada.getIdDespesaParcelada();
            aplicacaoRepository.updateDetalheDespesasMensaisConsolidacao(idDespesa, idDetalheDespesa, idDespesaParcelada, 0, idFuncionario);
        }

        if (aplicacaoRepository.getValidaDetalheDespesaComConsolidacao(idConsolidacao, idFuncionario).equals("N")) {
            aplicacaoRepository.updateConsolidacaoDespesa(idConsolidacao, null, idFuncionario);
        }
    }

    public StringResponse obterRelatorioDespesasParceladasConsolidadas(Integer idDespesa, Integer idDetalheDespesa, Integer idConsolidacao, Integer idFuncionario) {
        var listDespesas = this.obterListDetalheDespesasConsolidadas(idDespesa, idDetalheDespesa, idConsolidacao, idFuncionario);
        StringBuffer buffer = new StringBuffer();

        for (DetalheDespesasMensaisDAO dao : listDespesas) {
            StringBuilder builder = new StringBuilder();
            builder.append(dao.getVlTotal().concat("R$ - ").concat(dao.getDsTituloDespesa()));
            builder.append(System.lineSeparator());

            buffer.append(builder);
        }

        return StringResponse.builder()
                .nomeDespesaParcelada(buffer.toString())
                .build();
    }

    public List<DetalheDespesasMensaisDAO> obterListDetalheDespesasConsolidadas(Integer idDespesa, Integer idDetalheDespesa, Integer idConsolidacao, Integer idFuncionario) {
        return aplicacaoRepository.getDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario, parserOrdem(null)).stream()
                .filter(d -> d.getIdDespesaConsolidacao() == idConsolidacao)
                .collect(Collectors.toList());
    }

    public TituloDespesaResponse getNomeConsolidacaoParaAssociacao(Integer idFuncionario, Integer idDespesa, Integer idDetalheDespesa, String tipo) {
        List<TituloDespesa> listaConsolidacoes = new ArrayList<>();

        List<TituloConsolidacao> tituloConsolidacaoList = (tipo.equalsIgnoreCase("ativas") ? aplicacaoRepository.getNomeConsolidacoesAtivasParaAssociacao(idFuncionario, idDespesa, idDetalheDespesa) :
                aplicacaoRepository.getNomeConsolidacoes(idFuncionario));

        for (TituloConsolidacao consolidacao : tituloConsolidacaoList) {
            var tituloDespesa = TituloDespesa.builder()
                    .idDespesa(-consolidacao.getIdConsolidacao()) // para consolidacao foi necessario adicionar o - para tratar no frontend
                    .idConsolidacao(consolidacao.getIdConsolidacao())
                    .tituloDespesa(consolidacao.getTituloConsolidacao())
                    .build();

            listaConsolidacoes.add(tituloDespesa);
        }

        List<String> listTituloDespesa = new ArrayList<>();
        for (TituloDespesa despesas : listaConsolidacoes) {
            listTituloDespesa.add(despesas.getTituloDespesa());
        }

        log.info("ListaConsolidacoesParaAssociacao: {}", listaConsolidacoes);
        return TituloDespesaResponse.builder()
                .despesas(listaConsolidacoes)
                .sizeTituloDespesaVB(listaConsolidacoes.size())
                .tituloDespesa(listTituloDespesa)
                .build();
    }
}
