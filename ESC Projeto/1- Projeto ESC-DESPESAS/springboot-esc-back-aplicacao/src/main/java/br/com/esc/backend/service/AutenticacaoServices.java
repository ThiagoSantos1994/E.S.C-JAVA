package br.com.esc.backend.service;

import br.com.esc.backend.domain.*;
import br.com.esc.backend.exception.CredenciaisInvalidasException;
import br.com.esc.backend.exception.UsuarioBloqueadoException;
import br.com.esc.backend.repository.AutenticacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static br.com.esc.backend.utils.DataUtils.dataAtual;
import static br.com.esc.backend.utils.DataUtils.formatarDataBR;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutenticacaoServices {

    private final AutenticacaoRepository repository;
    private final AuditoriaAcessoService auditoriaAcessoService;
    private final ConfiguracaoLancamentosService configuracaoLancamentosService;
    private final PasswordEncoder passwordEncoder;

    @Value("${prop.tempoLimiteSessao}")
    private Integer tempoLimiteSessao;

    public AutenticacaoResponse autenticarUsuario(LoginRequest request) {
        log.info("Iniciando autenticacao >> usuario: {}", request.getUsuario());

        LoginDAO usuario = repository.buscarPorUsuario(request.getUsuario())
                .orElseThrow(() -> {
                    log.warn("Tentativa de login com usuario invalido: {}", request.getUsuario());
                    return new CredenciaisInvalidasException();
                });

        validarSenha(request.getSenha(), usuario.getDsSenha());

        validarStatusUsuario(usuario);

        processarPosAutenticacao(usuario.getIdLogin());

        log.info("Usuario autenticado com sucesso >> idLogin: {} | usuario: {}",
                usuario.getIdLogin(), usuario.getDsLogin());

        return construirResposta(usuario);
    }

    public StringResponse validarSessaoUsuario(Integer idFuncionario) {
        log.info("Validando sessao >> idFuncionario: {}", idFuncionario);

        try {
            SessaoDAO sessao = repository.getHorarioLoginAuditoriaAcesso(idFuncionario);

            if (!sessao.getDataLogin().equalsIgnoreCase(formatarDataBR(dataAtual()))) {
                log.info("Sessao expirada >> Login de data diferente | idFuncionario: {}", idFuncionario);
                return StringResponse.builder().isSessaoValida(false).build();
            }

            if (sessao.getTempoLogado() >= tempoLimiteSessao) {
                log.info("Sessao expirada >> Tempo excedido ({} >= {}) | idFuncionario: {}",
                        sessao.getTempoLogado(), tempoLimiteSessao, idFuncionario);
                return StringResponse.builder().isSessaoValida(false).build();
            }

            log.info("Sessao validada com sucesso >> idFuncionario: {} | tempoLogado: {}min",
                    idFuncionario, sessao.getTempoLogado());
            return StringResponse.builder().isSessaoValida(true).build();

        } catch (Exception e) {
            log.error("Erro ao validar sessao >> idFuncionario: {}", idFuncionario, e);
            return StringResponse.builder().isSessaoValida(false).build();
        }
    }

    private void validarSenha(String senhaFornecida, String senhaArmazenada) {
        // Verifica se a senha armazenada é um hash BCrypt (começa com $2a$, $2b$ ou $2y$)
        if (isBCryptHash(senhaArmazenada)) {
            // Senha já está em formato BCrypt - usar passwordEncoder.matches()
            if (!passwordEncoder.matches(senhaFornecida, senhaArmazenada)) {
                log.warn("Tentativa de login com senha invalida (BCrypt)");
                throw new CredenciaisInvalidasException();
            }
            log.info("Senha validada com sucesso usando BCrypt");
        } else {
            // Migração desabilitada - rejeitar senhas em texto plano
            log.error("Senha em texto plano rejeitada. Configuracao prop.seguranca.permitirSenhaTextoPlano=false");
            throw new CredenciaisInvalidasException();
        }
    }

    /**
     * Verifica se a string é um hash BCrypt válido.
     * Hashes BCrypt começam com $2a$, $2b$ ou $2y$ seguido de custo e hash.
     *
     * @param hash string a ser verificada
     * @return true se for um hash BCrypt válido
     */
    private boolean isBCryptHash(String hash) {
        if (hash == null || hash.length() < 60) {
            return false;
        }
        return hash.startsWith("$2a$") || hash.startsWith("$2b$") || hash.startsWith("$2y$");
    }

    private void validarStatusUsuario(LoginDAO usuario) {
        if ("S".equalsIgnoreCase(usuario.getIsUsuarioBloqueado())) {
            log.warn("Tentativa de login com usuario bloqueado >> idLogin: {}", usuario.getIdLogin());
            throw new UsuarioBloqueadoException();
        }

        if ("S".equalsIgnoreCase(usuario.getIsUsuarioExcluido())) {
            log.warn("Tentativa de login com usuario excluido >> idLogin: {}", usuario.getIdLogin());
            throw new UsuarioBloqueadoException();
        }
    }

    private void processarPosAutenticacao(Integer idFuncionario) {
        auditoriaAcessoService.registrarAcesso(idFuncionario);
        configuracaoLancamentosService.validarViradaAutomatica(idFuncionario);
    }

    private AutenticacaoResponse construirResposta(LoginDAO usuario) {
        return AutenticacaoResponse.builder()
                .idLogin(usuario.getIdLogin())
                .mensagem("Usuario autenticado com sucesso!")
                .nomeUsuario(usuario.getDsLogin())
                .autenticacao("Bearer -") // TODO: Implementar JWT real
                .autorizado(true)
                .build();
    }
}
