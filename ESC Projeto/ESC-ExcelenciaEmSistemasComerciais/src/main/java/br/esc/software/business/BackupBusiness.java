package br.esc.software.business;

import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.commons.utils.ObjectParser;
import br.esc.software.domain.Response;
import br.esc.software.service.BackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BackupBusiness {

    @Autowired
    private BackupService service;

    public String iniciarBackup() throws ExcecaoGlobal {
        Response response = new Response();
        ObjectParser parser = new ObjectParser();

        try {
            if (service.executarBackup()) {
                response.setResponse("[WARN] Processamento concluido com FALHA! Backup parcial executado com sucesso!");
                return parser.parser(response);
            } else {
                response.setResponse("Processamento concluido! Backup executado com sucesso!");
                return parser.parser(response);
            }
        } catch (Exception ex) {
            String strErro = ("Ocorreu um erro inesperado no metodo iniciarBackup, processamento interrompido -> " + ex);
            throw new ExcecaoGlobal(strErro, ex);
        }
    }
}
