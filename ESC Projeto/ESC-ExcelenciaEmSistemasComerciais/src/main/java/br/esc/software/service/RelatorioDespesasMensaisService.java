package br.esc.software.service;

import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.domain.relatorio.DespesasFixasMensais;
import br.esc.software.repository.RelatorioDespesasMensaisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class RelatorioDespesasMensaisService {
    @Autowired
    RelatorioDespesasMensaisRepository repository;

    public DespesasFixasMensais obterDespesasFixasMensais(Integer dsAno) throws ExcecaoGlobal, SQLException {
        return this.repository.obterDespesasFixasMensaisDAO(dsAno);
    }
}
