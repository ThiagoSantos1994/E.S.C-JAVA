package br.com.esc.backend.controller;

import br.com.esc.backend.facade.BackupFacade;
import br.com.esc.backend.domain.StringResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/backup")
@RequiredArgsConstructor
@Slf4j
public class BackupController {

    private final BackupFacade backupFacade;

    @PostMapping(path = "/processar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> processarBackup() {
        var response = backupFacade.processarBackupBaseDados();
        return ResponseEntity.ok(response);
    }
}

