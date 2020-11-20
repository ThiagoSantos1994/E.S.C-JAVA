package br.esc.software.service;

import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.domain.exportador.TabelasSQL;
import br.esc.software.repository.BackupRepository;
import br.esc.software.repository.ExportadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static br.esc.software.commons.utils.GlobalUtils.*;

@Service
public class BackupService {

    @Autowired
    private BackupRepository dao;
    @Autowired
    private ExportadorRepository exportadorRepository;

    private String sBaseBackup = getProperties().getProperty("prop.databaseBackup");
    private String sBasePrincipal = getProperties().getProperty("prop.database");
    private boolean bProcessamentoComFalhas = false;

    public Boolean executarBackup() throws ExcecaoGlobal {
        try {
            for (TabelasSQL tabelaSQL : exportadorRepository.getListaTabelas()) {
                String tabela = tabelaSQL.getNomeTabela();

                boolean statusDelete = this.excluir(tabela);
                if (statusDelete == false) {
                    bProcessamentoComFalhas = true;
                }

                boolean statusInsert = this.inserir(sBaseBackup, sBasePrincipal, tabela);
                if (statusInsert == false) {
                    bProcessamentoComFalhas = true;
                } else {
                    LogDebug("Backup realizado com sucesso -> " + tabela);
                }
            }
        } catch (Exception ex) {
            throw new ExcecaoGlobal("Ocorreu uma excessao durante o backup Java -> ", ex);
        }

        if (bProcessamentoComFalhas) {
            LogInfo("Stts processamento : Processamento com falha = " + bProcessamentoComFalhas);
        }
        return bProcessamentoComFalhas;
    }

    private Boolean excluir(String tabela) {
        try {
            dao.excluirDadosTabelas(tabela);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Boolean inserir(String baseBackup, String basePrincipal, String nomeTabela) {
        try {
            dao.inserirDadosTabelas(baseBackup, basePrincipal, nomeTabela);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
