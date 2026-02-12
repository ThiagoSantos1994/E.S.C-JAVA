package br.com.esc.backend.business;

import br.com.esc.backend.domain.LembretesDAO;
import br.com.esc.backend.domain.TituloLembretesDAO;
import br.com.esc.backend.repository.AplicacaoRepository;
import br.com.esc.backend.service.LembreteServices;
import br.com.esc.backend.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static br.com.esc.backend.service.LembreteServices.validarCamposEntradaLembrete;

@Component
@RequiredArgsConstructor
@Slf4j
public class LembretesBusiness {

    private final AplicacaoRepository repository;
    private final LembreteServices lembreteServices;
    private final ChaveKeyBusiness chaveKeyBusiness;

    public LembretesDAO obterDetalheLembrete(Integer idLembrete, Integer idFuncionario) {
        log.info("Obtendo detalhes lembrete id = {}", idLembrete);
        return lembreteServices.getLembreteDetalhe(idLembrete, idFuncionario);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void baixarLembretesMonitor(List<TituloLembretesDAO> request, String tipoBaixa) {
        log.info("Realizando a baixa de lembretes >>> tipoBaixa: {} - request: {}", request, tipoBaixa);
        lembreteServices.baixarLembretesMonitor(request, tipoBaixa);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void gravarLembrete(LembretesDAO request) {
        log.info("Validando parametros de entrada do lembrete...");
        validarCamposEntradaLembrete(request);

        log.info("Gravando detalhes do lembrete...");
        if (ObjectUtils.isNull(request.getIdLembrete()) || request.getIdLembrete() == -1) {
            request.setIdLembrete(chaveKeyBusiness.retornaNovaChaveKey("LEMBRETES").getNovaChave());
        }

        lembreteServices.gravarLembrete(request);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void excluirLembrete(LembretesDAO request) {
        log.info("Excluindo lembrete >> request {}", request);
        repository.deleteLembrete(request.getIdLembrete(), request.getIdFuncionario());
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public List<TituloLembretesDAO> obterListaMonitorLembretes(Integer idFuncionario) {
        log.info("Obtendo lista de lembretes");
        return lembreteServices.getListaMonitorLembretes(idFuncionario);
    }

    public List<TituloLembretesDAO> obterTituloLembretes(Integer idFuncionario, Boolean tpBaixado) {
        log.info("Obtendo lista de nomes dos lembretes");
        return lembreteServices.getListaNomesLembretes(idFuncionario, tpBaixado);
    }
}

