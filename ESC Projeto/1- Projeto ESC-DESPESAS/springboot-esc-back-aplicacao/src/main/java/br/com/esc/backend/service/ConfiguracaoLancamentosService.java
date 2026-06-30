package br.com.esc.backend.service;

import br.com.esc.backend.domain.ConfiguracaoLancamentosResponse;
import br.com.esc.backend.repository.AplicacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static br.com.esc.backend.utils.DataUtils.*;
import static java.lang.Integer.parseInt;

/**
 * Service responsável por validar e processar configurações de virada de mês.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ConfiguracaoLancamentosService {

    private final AplicacaoRepository aplicacaoRepository;

    public void validarViradaAutomatica(Integer idFuncionario) {
        log.info("Validando parametros de virada de mês >> idFuncionario: {}", idFuncionario);

        try {
            ConfiguracaoLancamentosResponse configuracao = aplicacaoRepository.getConfiguracaoLancamentos(idFuncionario);

            // Verifica se deve processar virada automática
            if (deveProcessarViradaMes(configuracao.getDataViradaMes(), configuracao.getMesReferencia())) {
                int mesVirada = calcularMesVirada();
                int anoVirada = calcularAnoVirada();

                log.info("Processando virada automática >> idFuncionario: {} | Novo período: {}/{}",
                        idFuncionario, mesVirada, anoVirada);

                aplicacaoRepository.updateDataConfiguracoesLancamentos(idFuncionario, mesVirada, anoVirada);
            }
        } catch (Exception ex) {
            log.warn("Funcionario sem parametrizacao de configuracoes lancamentos. Criando registro default >> idFuncionario: {}",
                    idFuncionario, ex);
            aplicacaoRepository.insertDataConfiguracoesLancamentosNovo(idFuncionario);
        }
    }

    private boolean deveProcessarViradaMes(Integer dataViradaMes, Integer mesReferencia) {
        if (dataViradaMes == null || dataViradaMes == 0) {
            return false;
        }

        int diaAtual = parseInt(diaAtual());
        int mesAtual = parseInt(mesAtual());

        return diaAtual >= dataViradaMes && mesAtual == mesReferencia;
    }

    private int calcularMesVirada() {
        int mesAtual = parseInt(mesAtual());
        return mesAtual + 1 == 13 ? 1 : mesAtual + 1;
    }

    private int calcularAnoVirada() {
        int mesAtual = parseInt(mesAtual());
        return mesAtual + 1 == 13 ? parseInt(anoSeguinte()) : parseInt(anoAtual());
    }
}
