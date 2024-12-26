package br.com.esc.backend.service;

import br.com.esc.backend.domain.AutenticacaoResponse;
import br.com.esc.backend.domain.LoginDAO;
import br.com.esc.backend.domain.LoginRequest;
import br.com.esc.backend.domain.StringResponse;
import br.com.esc.backend.repository.AplicacaoRepository;
import br.com.esc.backend.repository.AutenticacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;

import static br.com.esc.backend.utils.DataUtils.*;
import static br.com.esc.backend.utils.GlobalUtils.getProperties;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.Integer.parseInt;
import static java.net.InetAddress.getLocalHost;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutenticacaoServices {

    private final AplicacaoRepository aplicacaoRepository;
    private final AutenticacaoRepository repository;

    public AutenticacaoResponse autenticarUsuario(LoginRequest login) {
        var idLogin = -1;
        var nomeUsuario = "";
        var status = "";

        log.info("Realizando a autenticacao do LOGIN: >> Usuario: {} - Senha: {}", login.getUsuario(), login.getSenha());

        for (LoginDAO dao : repository.getLoginUsuario()) {
            var user = dao.getDsLogin();
            var password = dao.getDsSenha();
            idLogin = dao.getIdLogin();
            nomeUsuario = dao.getDsLogin();

            if (login.getUsuario().equalsIgnoreCase(user) && login.getSenha().equalsIgnoreCase(password)) {
                if (dao.getIsUsuarioBloqueado().equalsIgnoreCase("S") || dao.getIsUsuarioExcluido().equalsIgnoreCase("S")) {
                    status = "Usuario com status bloqueado ou excluido da base de dados.";
                    break;
                }
                status = "Usuario autenticado com sucesso!";
                this.registrarAcesso(idLogin);
                this.validarViradaAutomatica(idLogin);
                break;
            }

            idLogin = -1;
        }

        return AutenticacaoResponse.builder()
                .idLogin(idLogin)
                .mensagem(idLogin != -1 ? status : "Usuario e\\ou senha invalidos.")
                .nomeUsuario(nomeUsuario)
                .autenticacao("Bearer -")
                .autorizado(idLogin != -1 ? true : false)
                .build();
    }

    public StringResponse validarSessaoUsuario(Integer idFuncionario) {
        var parametro = parseInt(getProperties().getProperty("prop.tempoLimiteSessao"));
        var result = repository.getHorarioLoginAuditoriaAcesso(idFuncionario);

        log.info("Validando Sessao Usuario >> ParametroRequest: {} - ResponseAuditoria: {}", parametro, result);

        if (result.getDataLogin().equalsIgnoreCase(formatarDataBR(DataAtual()))) {
            if (result.getTempoLogado().compareTo(parametro) >= 0) {
                log.info("Validando Sessao Usuario Response >> Sessao expirada por tempo excedido.");
                return StringResponse.builder().isSessaoValida(FALSE).build();
            } else {
                log.info("Validando Sessao Usuario Response >> Sessao validada com sucesso.");
                return StringResponse.builder().isSessaoValida(TRUE).build();
            }
        }

        log.info("Validando Sessao Usuario >> Result FALSE por validacao de Datas.");
        return StringResponse.builder().isSessaoValida(FALSE).build();
    }

    private void validarViradaAutomatica(Integer idFuncionario) {
        log.info("Validando parametros de virada de mês...");
        try {
            var configuracao = aplicacaoRepository.getConfiguracaoLancamentos(idFuncionario);

            //Muda a data de referencia somente no dia e mes programado
            if (configuracao.getDataViradaMes().compareTo(0) != 0 && (parseInt(DiaAtual()) >= configuracao.getDataViradaMes()) && parseInt(MesAtual()) == configuracao.getMesReferencia()) {
                var mesViradaConfiguracao = (parseInt(MesAtual()) + 1 == 13 ? 1 : parseInt(MesAtual()) + 1);
                var anoViradaConfiguracao = (parseInt(MesAtual()) + 1 == 13 ? parseInt(AnoSeguinte()) : parseInt(AnoAtual()));
                aplicacaoRepository.updateDataConfiguracoesLancamentos(idFuncionario, mesViradaConfiguracao, anoViradaConfiguracao);
            }
        }  catch (Exception ex) {
            log.info("Funcionario sem parametrizacao de configuracoes lancamentos, incluindo novo registro na base.");
            aplicacaoRepository.insertDataConfiguracoesLancamentosNovo(idFuncionario);
        }
    }

    private void registrarAcesso(Integer idFuncionario) {
        log.info("Registrando acessos...");

        var hostAcesso = this.obterDadosMaquina();
        repository.insertAuditoriaAcesso(idFuncionario, DataHoraAtual(), hostAcesso);
    }

    private String obterDadosMaquina() {
        try {
            return "WEB_".concat(getLocalHost().getHostAddress())
                    .concat("_")
                    .concat(getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            log.error("Ocorreu um erro ao obter os dados da maquina (nome e ip), retornando dados genéricos.");
            return "WEB_maquina_default";
        }
    }
}
