package br.com.esc.backend.service;

import br.com.esc.backend.domain.LembretesDAO;
import br.com.esc.backend.domain.TituloLembretesDAO;
import br.com.esc.backend.repository.AplicacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static br.com.esc.backend.utils.DataUtils.*;
import static br.com.esc.backend.utils.ObjectUtils.isNull;
import static br.com.esc.backend.utils.VariaveisGlobais.*;
import static java.lang.String.valueOf;

@Service
@RequiredArgsConstructor
@Slf4j
public class LembreteServices {

    private final AplicacaoRepository aplicacaoRepository;

    public LembretesDAO getLembreteDetalhe(Integer idLembrete, Integer idFuncionario) {
        var response = aplicacaoRepository.getLembreteDetalhe(idLembrete, idFuncionario);
        response.setDataInicial(formatarDataBR(response.getDataInicial()));
        response.setData1(formatarDataBR(response.getData1()));
        response.setData2(formatarDataBR(response.getData2()));
        response.setData3(formatarDataBR(response.getData3()));
        response.setData4(formatarDataBR(response.getData4()));
        response.setData5(formatarDataBR(response.getData5()));

        return response;
    }

    public void gravarLembrete(LembretesDAO request) {
        var isLembreteExistente = this.isLembreteExistente(request.getIdLembrete(), request.getIdFuncionario());

        request.setDataInicial(formatarDataEUA(request.getDataInicial()));
        request.setData1(formatarDataEUA(request.getData1()));
        request.setData2(formatarDataEUA(request.getData2()));
        request.setData3(formatarDataEUA(request.getData3()));
        request.setData4(formatarDataEUA(request.getData4()));
        request.setData5(formatarDataEUA(request.getData5()));

        if (isLembreteExistente) {
            log.info("Atualizando lembrete >> request {}", request);
            aplicacaoRepository.updateLembrete(request);
        } else {
            log.info("Incluindo novo lembrete >> request {}", request);
            aplicacaoRepository.insertLembrete(request);
        }
    }

    public void baixarLembretesMonitor(List<TituloLembretesDAO> request, String tipoBaixa) throws ParseException {
        for (TituloLembretesDAO lembrete : request) {
            var data = this.validarNovaDataBaixa(lembrete, tipoBaixa);

            if (isNull(data)) {
                log.info("Realizando a baixa do lembrete >> {}", lembrete);
                aplicacaoRepository.updateBaixarLembrete(lembrete.getIdLembrete(), lembrete.getIdFuncionario());
            } else {
                log.info("Alterando a data da baixa do lembrete >> {} - novaData: {}", lembrete, data);
                aplicacaoRepository.updateDataBaixaLembrete(lembrete.getIdLembrete(), data, lembrete.getIdFuncionario());
            }
        }
    }

    private String validarNovaDataBaixa(TituloLembretesDAO lembrete, String tipoBaixa) throws ParseException {
        var dataLembrete = aplicacaoRepository.getLembreteDetalhe(lembrete.getIdLembrete(), lembrete.getIdFuncionario()).getDataInicial();

        if (tipoBaixa.equalsIgnoreCase(SEMANA)) {
            return retornaDataPersonalizadaEmDias(dataLembrete, 7);
        } else if (tipoBaixa.equalsIgnoreCase(MES)) {
            return formatarDataEUA(retornaDataPersonalizada(formatarDataBR(dataLembrete), 1));
        } else if (tipoBaixa.equalsIgnoreCase(ANO)) {
            return formatarDataEUA(retornaDataPersonalizada(formatarDataBR(dataLembrete), 12));
        } else {
            //Quando for baixa, retorna null
            return null;
        }
    }

    public List<TituloLembretesDAO> getListaMonitorLembretes(Integer idFuncionario) throws ParseException {
        ArrayList<TituloLembretesDAO> listLembretes = new ArrayList<>();

        for (LembretesDAO lembrete : aplicacaoRepository.getMonitorLembretes(idFuncionario, "N", this.sWhereSemanal())) {
            var isRenovaAUTO = lembrete.getTpRenovarAuto().equalsIgnoreCase("S");
            var qtdeDiasParaExibicao = lembrete.getNumeroDias();
            var iQtdeDiasVisualizacao = diferencaEmDias(lembrete.getDataInicial()).getDays();

            if (isRenovaAUTO && qtdeDiasParaExibicao > 0) {
                //Valida se a quantidade de dias restantes é menor a quantidade de dias para exibicao
                if (iQtdeDiasVisualizacao > qtdeDiasParaExibicao) {
                    continue;
                }
            }

            if (isRenovaAUTO) {
                this.atualizarDataRenovacaoAUTO(lembrete.getIdLembrete(), lembrete.getIdFuncionario(), lembrete.getDataInicial(), iQtdeDiasVisualizacao);
            }

            var response = TituloLembretesDAO.builder()
                    .idLembrete(lembrete.getIdLembrete())
                    .idFuncionario(lembrete.getIdFuncionario())
                    .dsTituloLembrete(lembrete.getDsTituloLembrete().replace("[D]", valueOf(iQtdeDiasVisualizacao)))
                    .build();

            listLembretes.add(response);
        }

        return listLembretes;
    }

    public List<TituloLembretesDAO> getListaNomesLembretes(Integer idFuncionario, Boolean tpBaixado) {
        return aplicacaoRepository.getTituloLembretes(idFuncionario, (tpBaixado.equals(true) ? "S" : "N"));
    }

    private void atualizarDataRenovacaoAUTO(Integer idLembrete, Integer idFuncionario, String dataInicial, Integer iQtdeDiasRestantes) throws ParseException {
        if (iQtdeDiasRestantes <= 0) {
            aplicacaoRepository.updateDataRenovacaoAUTOLembrete(idLembrete, idFuncionario, dataInicial);
        }
    }

    private boolean isLembreteExistente(Integer idLembrete, Integer idFuncionario) {
        var result = aplicacaoRepository.getLembreteDetalhe(idLembrete, idFuncionario);
        return (null != result);
    }

    private String sWhereSemanal() {
        var diaSemana = aplicacaoRepository.getDiaSemana();

        switch (diaSemana) {
            case SEGUNDA:
                return "tp_Segunda = 'S'";
            case TERCA:
                return "tp_Terca = 'S'";
            case QUARTA:
                return "tp_Quarta = 'S'";
            case QUINTA:
                return "tp_Quinta = 'S'";
            case SEXTA:
                return "tp_Sexta = 'S'";
            case SABADO:
                return "tp_Sabado = 'S'";
            case DOMINGO:
                return "tp_Domingo = 'S'";
            default:
                return null;
        }
    }
}
