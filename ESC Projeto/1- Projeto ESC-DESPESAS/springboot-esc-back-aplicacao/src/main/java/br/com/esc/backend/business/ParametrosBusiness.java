package br.com.esc.backend.business;

import br.com.esc.backend.domain.ConfiguracaoLancamentosRequest;
import br.com.esc.backend.domain.ConfiguracaoLancamentosResponse;
import br.com.esc.backend.repository.AplicacaoRepository;
import br.com.esc.backend.utils.DataUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static br.com.esc.backend.utils.DataUtils.anoSeguinte;

@Component
@RequiredArgsConstructor
@Slf4j
public class ParametrosBusiness {

    private final AplicacaoRepository repository;
    private final LembretesBusiness lembretesBusiness;

    public ConfiguracaoLancamentosResponse obterConfiguracaoLancamentos(Integer idFuncionario) {
        log.info("Obtendo parametros sistemicos");

        var response = repository.getConfiguracaoLancamentos(idFuncionario);
        response.setQtdeLembretes(lembretesBusiness.obterListaMonitorLembretes(idFuncionario).size());
        response.setAnosReferenciaFiltro(this.obterListaAnosReferencia());

        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void gravarConfiguracoesLancamentos(ConfiguracaoLancamentosRequest request) {
        log.info("Gravando parametros sistemicos - {}", request);
        request.setViradaAutomatica(request.isBViradaAutomatica() ? 'S' : 'N');
        repository.updateConfiguracoesLancamentos(request);
    }

    private List<String> obterListaAnosReferencia() {
        List<String> responseList = new ArrayList<>();
        log.info("Obtendo lista de anos referencia...");

        try {
            var listAnosRef = repository.getListaAnoReferencia();
            if (!listAnosRef.contains(anoSeguinte())) {
                responseList.add(anoSeguinte());
            }
            responseList.addAll(listAnosRef);
        } catch (Exception ex) {
            responseList.add(DataUtils.anoAtual());
        }

        return responseList;
    }
}
