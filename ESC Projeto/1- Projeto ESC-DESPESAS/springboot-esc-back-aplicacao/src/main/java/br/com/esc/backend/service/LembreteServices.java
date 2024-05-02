package br.com.esc.backend.service;

import br.com.esc.backend.domain.LembretesDAO;
import br.com.esc.backend.repository.AplicacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static br.com.esc.backend.utils.DataUtils.*;
import static java.lang.String.valueOf;

@Service
@RequiredArgsConstructor
@Slf4j
public class LembreteServices {

    private final AplicacaoRepository aplicacaoRepository;

    public List<LembretesDAO> getLembretes(Integer idFuncionario) throws ParseException {
        ArrayList<LembretesDAO> listLembretes = new ArrayList<>();

        for (LembretesDAO lembrete: aplicacaoRepository.getLembretes(idFuncionario, "N", this.sWhereSemanal())) {
            var isRenovaAUTO = lembrete.getTpRenovarAuto().equalsIgnoreCase("S");
            var qtdeDiasParaExibicao = lembrete.getNumeroDias();
            var iQtdeDiasVisualizacao = diferencaEmDias(lembrete.getDataInicial()).getDays();

            if (isRenovaAUTO && qtdeDiasParaExibicao > 0) {
                //Valida se a quantidade de dias restantes é menor a quantidade de dias para exibicao
                if (iQtdeDiasVisualizacao > qtdeDiasParaExibicao) {
                    continue;
                }
            }

            var tituloLembrete = lembrete.getDsTituloLembrete().replace("[D]", valueOf(iQtdeDiasVisualizacao));
            lembrete.setDsTituloLembrete(tituloLembrete);

            if (isRenovaAUTO){
                this.atualizarDataRenovacaoAUTO(lembrete.getIdLembrete(), lembrete.getIdFuncionario(), lembrete.getDataInicial(), iQtdeDiasVisualizacao);
            }

            listLembretes.add(lembrete);
        }

        return listLembretes;
    }

    private void atualizarDataRenovacaoAUTO(Integer idLembrete, Integer idFuncionario, String dataInicial, Integer iQtdeDiasRestantes) throws ParseException {
        if (iQtdeDiasRestantes <= 0) {
            aplicacaoRepository.updateDataRenovacaoAUTOLembrete(idLembrete, idFuncionario, dataInicial);
        }
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
