package br.com.esc.backend.facade;

import br.com.esc.backend.domain.StringResponse;
import br.com.esc.backend.service.BackupServices;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BackupFacade {

    private final BackupServices backupServices;

    @SneakyThrows
    public StringResponse processarBackupBaseDados() {
        return backupServices.processarBackup();
    }
}
