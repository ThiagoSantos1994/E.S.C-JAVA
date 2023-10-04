package br.esc.software.business;

import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.domain.relatorio.DespesasFixasMensais;
import br.esc.software.service.RelatorioDespesasMensaisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component
public class RelatorioDespesasMensaisBusiness {

    @Autowired
    RelatorioDespesasMensaisService service;

    public DespesasFixasMensais obterDespesasFixasMensais(Integer dsAno) throws ExcecaoGlobal, SQLException {
        DespesasFixasMensais despesasDAO = this.service.obterDespesasFixasMensais(dsAno);
        return despesasDAO;
    }
}
