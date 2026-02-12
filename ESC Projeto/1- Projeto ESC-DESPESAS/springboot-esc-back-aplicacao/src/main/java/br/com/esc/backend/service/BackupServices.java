package br.com.esc.backend.service;

import br.com.esc.backend.domain.StringResponse;
import br.com.esc.backend.repository.BackupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BackupServices {

    private final BackupRepository repository;

    @Value("${prop.basePrincipal}")
    private String basePrincipal;

    @Value("${prop.baseBackup}")
    private String baseBackup;

    /**
     * Processa o backup de todas as tabelas da base de dados.
     * Executa o backup tabela por tabela, registrando sucesso ou falha de cada operação.
     *
     * @return StringResponse contendo o resultado do processamento
     */
    public StringResponse processarBackup() {
        log.info("========== Iniciando processo de backup da base de dados ==========");

        List<String> tabelas = repository.getListaTabelasBaseDados();
        log.info("Total de tabelas a processar: {}", tabelas.size());

        BackupResult resultado = executarBackupTabelas(tabelas);

        log.info("========== Backup finalizado - Sucesso: {} | Falhas: {} ==========",
                resultado.getSuccessCount(), resultado.getFailureCount());

        return construirResposta(resultado);
    }

    /**
     * Executa o backup de todas as tabelas e registra os resultados.
     *
     * @param tabelas Lista de nomes das tabelas
     * @return BackupResult com estatísticas do processo
     */
    private BackupResult executarBackupTabelas(List<String> tabelas) {
        BackupResult resultado = new BackupResult();

        for (String nomeTabela : tabelas) {
            try {
                realizarBackupTabela(nomeTabela);
                resultado.addSuccess(nomeTabela);
                log.info("Backup concluído com sucesso >> Tabela: {}", nomeTabela);
            } catch (Exception e) {
                resultado.addFailure(nomeTabela, e.getMessage());
                log.error("Erro ao realizar backup >> Tabela: {} | Erro: {}", nomeTabela, e.getMessage(), e);
            }
        }

        return resultado;
    }

    /**
     * Realiza o backup de uma tabela específica.
     * Limpa os dados existentes na tabela de backup e insere os dados atuais.
     * Tenta primeiro sem IDENTITY_INSERT. Se falhar com erro de identity column,
     * habilita IDENTITY_INSERT e tenta novamente.
     *
     * @param nomeTabela Nome da tabela a ser processada
     * @throws Exception em caso de erro durante o backup
     */
    @Transactional(rollbackFor = Exception.class)
    private void realizarBackupTabela(String nomeTabela) throws Exception {
        String tabelaPrincipal = construirNomeCompleto(basePrincipal, nomeTabela);
        String tabelaBackup = construirNomeCompleto(baseBackup, nomeTabela);

        log.info("Limpando dados existentes >> Tabela backup: {}", tabelaBackup);
        repository.deleteDadosTabela(tabelaBackup);

        try {
            log.info("Copiando dados >> De: {} | Para: {}", tabelaPrincipal, tabelaBackup);
            repository.insertDadosBaseBackup(tabelaBackup, tabelaPrincipal);
        } catch (Exception e) {
            if (isIdentityInsertError(e)) {
                log.info("Detectado erro de identity column. Tentando novamente com IDENTITY_INSERT habilitado >> Tabela: {}", tabelaBackup);
                realizarBackupComIdentityInsert(tabelaPrincipal, tabelaBackup);
            } else {
                throw e;
            }
        }
    }

    /**
     * Realiza o backup de uma tabela com IDENTITY_INSERT habilitado.
     * Usado quando a primeira tentativa falha devido a coluna identity.
     * Lista explicitamente as colunas para atender requisito do IDENTITY_INSERT.
     *
     * @param tabelaPrincipal Nome completo da tabela principal
     * @param tabelaBackup Nome completo da tabela de backup
     * @throws Exception em caso de erro durante o backup
     */
    private void realizarBackupComIdentityInsert(String tabelaPrincipal, String tabelaBackup) throws Exception {
        log.info("Habilitando IDENTITY_INSERT >> Tabela: {}", tabelaBackup);
        repository.setIdentityInsert(tabelaBackup, true);

        try {
            log.info("Limpando dados existentes novamente >> Tabela backup: {}", tabelaBackup);
            repository.deleteDadosTabela(tabelaBackup);

            // Obter lista de colunas da tabela
            String nomeTabela = extrairNomeTabela(tabelaBackup);
            log.info("Obtendo lista de colunas >> Tabela: {}", nomeTabela);
            List<String> colunas = repository.getColunasTabela(nomeTabela);
            String listaColunas = String.join(", ", colunas);

            log.info("Copiando dados com IDENTITY_INSERT e lista de colunas >> De: {} | Para: {} | Colunas: {}",
                    tabelaPrincipal, tabelaBackup, colunas.size());
            repository.insertDadosComColunas(tabelaBackup, tabelaPrincipal, listaColunas);
        } finally {
            log.info("Desabilitando IDENTITY_INSERT >> Tabela: {}", tabelaBackup);
            repository.setIdentityInsert(tabelaBackup, false);
        }
    }

    /**
     * Extrai apenas o nome da tabela do nome completo (baseDados.dbo.nomeTabela).
     *
     * @param nomeCompleto Nome completo da tabela
     * @return Nome da tabela sem schema
     */
    private String extrairNomeTabela(String nomeCompleto) {
        String[] partes = nomeCompleto.split("\\.");
        return partes[partes.length - 1];
    }

    /**
     * Verifica se a exceção é relacionada a erro de identity column.
     *
     * @param e Exceção a ser verificada
     * @return true se for erro de identity column, false caso contrário
     */
    private boolean isIdentityInsertError(Exception e) {
        String errorMessage = e.getMessage();
        if (errorMessage == null) {
            return false;
        }

        // Verifica se é erro de identity column
        return errorMessage.contains("identity column")
                || errorMessage.contains("IDENTITY_INSERT")
                || errorMessage.contains("explicit value for the identity column");
    }

    /**
     * Constrói o nome completo da tabela com schema.
     *
     * @param baseDados Nome da base de dados
     * @param nomeTabela Nome da tabela
     * @return Nome completo no formato: baseDados.dbo.nomeTabela
     */
    private String construirNomeCompleto(String baseDados, String nomeTabela) {
        return String.format("%s.dbo.%s", baseDados, nomeTabela);
    }

    /**
     * Constrói a resposta com base no resultado do backup.
     * Não expõe detalhes técnicos de falhas ao usuário final (mantidos apenas em logs).
     *
     * @param resultado Resultado do processamento
     * @return StringResponse formatada
     */
    private StringResponse construirResposta(BackupResult resultado) {
        if (resultado.isAllSuccess()) {
            return StringResponse.builder()
                    .mensagem("Backup executado com sucesso! Total de tabelas processadas: " + resultado.getSuccessCount())
                    .build();
        }

        if (resultado.isAllFailure()) {
            return StringResponse.builder()
                    .mensagem("Erro: Falha ao executar o backup. Verifique os logs para mais detalhes.")
                    .build();
        }

        return StringResponse.builder()
                .mensagem(String.format("Backup executado com avisos. Tabelas processadas com sucesso: %d. Verifique os logs para detalhes sobre as falhas.",
                        resultado.getSuccessCount()))
                .build();
    }

    /**
     * Classe interna para armazenar o resultado do processamento do backup.
     */
    private static class BackupResult {
        private final List<String> successTables = new ArrayList<>();
        private final List<String> failureTables = new ArrayList<>();
        private final List<String> failureMessages = new ArrayList<>();

        public void addSuccess(String tabela) {
            successTables.add(tabela);
        }

        public void addFailure(String tabela, String mensagem) {
            failureTables.add(tabela);
            failureMessages.add(String.format("Tabela: %s | Erro: %s", tabela, mensagem));
        }

        public int getSuccessCount() {
            return successTables.size();
        }

        public int getFailureCount() {
            return failureTables.size();
        }

        public boolean isAllSuccess() {
            return failureTables.isEmpty();
        }

        public boolean isAllFailure() {
            return successTables.isEmpty();
        }

        public String buildFailureMessage() {
            return String.join("\n", failureMessages);
        }
    }
}
