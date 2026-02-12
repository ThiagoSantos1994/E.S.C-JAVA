package br.com.esc.backend.service;

import br.com.esc.backend.repository.AutenticacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static br.com.esc.backend.utils.DataUtils.dataHoraAtual;

/**
 * Service responsável por registrar acessos e auditoria do sistema.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuditoriaAcessoService {

    private final AutenticacaoRepository repository;

    public void registrarAcesso(Integer idFuncionario) {
        log.info("Registrando acesso >> idFuncionario: {}", idFuncionario);

        String hostAcesso = obterDadosMaquina();
        String dataHora = dataHoraAtual();

        repository.insertAuditoriaAcesso(idFuncionario, dataHora, hostAcesso);

        log.debug("Acesso registrado com sucesso >> idFuncionario: {} | host: {} | dataHora: {}",
                idFuncionario, hostAcesso, dataHora);
    }

    private String obterDadosMaquina() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            return String.format("WEB_%s_%s",
                    localhost.getHostAddress(),
                    localhost.getHostName());
        } catch (UnknownHostException e) {
            log.warn("Erro ao obter dados da maquina. Usando identificador genérico.", e);
            return "WEB_maquina_default";
        }
    }
}
