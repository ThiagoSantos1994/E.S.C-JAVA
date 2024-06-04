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
import static java.lang.String.valueOf;

@Service
@RequiredArgsConstructor
@Slf4j
public class LembreteServices {

    private final AplicacaoRepository aplicacaoRepository;

    public LembretesDAO getLembreteDetalhe(Integer idLembrete, Integer idFuncionario) {
        return aplicacaoRepository.getLembreteDetalhe(idLembrete, idFuncionario);
    }

    public void gravarLembrete(LembretesDAO request) {
        var isLembreteExistente = this.isLembreteExistente(request.getIdLembrete(), request.getIdFuncionario());

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

        if (tipoBaixa.equals("semana")) {
            return retornaDataPersonalizadaEmDias(dataLembrete, 7);
        } else if (tipoBaixa.equals("mes")) {
            return formatarDataEUA(retornaDataPersonalizada(formatarDataBR(dataLembrete), 1));
        } else if (tipoBaixa.equals("ano")) {
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
            case "SEG":
                return "tp_Segunda = 'S'";
            case "TER":
                return "tp_Terca = 'S'";
            case "QUA":
                return "tp_Quarta = 'S'";
            case "QUI":
                return "tp_Quinta = 'S'";
            case "SEX":
                return "tp_Sexta = 'S'";
            case "SAB":
                return "tp_Sabado = 'S'";
            case "DOM":
                return "tp_Domingo = 'S'";
            default:
                return null;
        }
    }
}
