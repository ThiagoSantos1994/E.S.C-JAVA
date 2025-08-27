package br.com.esc.backend.service;

import br.com.esc.backend.domain.StringResponse;
import br.com.esc.backend.repository.BackupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static br.com.esc.backend.utils.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
@Slf4j
public class BackupServices {
    private final BackupRepository repository;

    @Value("${prop.basePrincipal}")
    private String basePrincipal;

    @Value("${prop.baseBackup}")
    private String baseBackup;

    public StringResponse processarBackup() {
        StringBuilder builder = new StringBuilder();

        for (String tabelaDAO : repository.getListaTabelasBaseDados()) {
            var principal = basePrincipal.concat(".dbo.").concat(tabelaDAO);
            var backup = baseBackup.concat(".dbo.").concat(tabelaDAO);

            try {
                repository.deleteDadosTabela(backup);

                log.info("Realizando backup dos dados >> tabela: {}", backup);
                repository.insertDadosBaseBackup(backup, principal);
            } catch (Exception e) {
                log.error("Erro ao realizar o Backup >> tabela: {}", backup);
                builder.append(isEmpty(builder.toString()) ? "Backup executado parcialmente com sucesso:".concat("\n") :
                        "Erro backup >> Tabela:" + backup.concat("\n"));
            }
        }

        return StringResponse.builder()
                .mensagem(isEmpty(builder.toString()) ? "Processamento concluido com sucesso!" : builder.toString())
                .build();
    }
}
