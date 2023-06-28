package br.esc.software.repository;

import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.domain.relatorio.DespesasFixasMensais;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import static br.esc.software.configuration.ConnectionSQL.Select_Table;

@Repository
public class RelatorioDespesasMensaisRepository {

    private ResultSet RSAdo;

    public DespesasFixasMensais obterDespesasFixasMensaisDAO(Integer ano) throws ExcecaoGlobal, SQLException {

        DespesasFixasMensais fixasMensais = new DespesasFixasMensais();

        RSAdo = Select_Table(
                "SELECT DISTINCT(id_Despesa) as id_Despesa, ds_Mes, ds_Ano FROM tbd_DespesasFixasMensais where ds_Ano = "
                        + ano + " and id_Funcionario = 2");
        while (RSAdo.next()) {
            fixasMensais.setId_Despesa(RSAdo.getInt("id_Despesa"));
            fixasMensais.setDs_Mes(RSAdo.getString("ds_Mes"));
            fixasMensais.setDs_Ano(RSAdo.getString("ds_Mes"));
        }
        RSAdo.close();

        return fixasMensais;
    }
}
