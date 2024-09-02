package br.com.esc.backend.mapper;

import br.com.esc.backend.domain.ConsolidacaoDespesasDAO;
import br.com.esc.backend.service.DespesasParceladasServices;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class DetalheConsolidacaoRowMapper implements RowMapper<ConsolidacaoDespesasDAO> {
    @Autowired
    DespesasParceladasServices services;

    @Override
    public ConsolidacaoDespesasDAO map(ResultSet rs, StatementContext ctx) throws SQLException {
        return ConsolidacaoDespesasDAO.builder()
                .idConsolidacao(rs.getInt("id_Consolidacao"))
                .idFuncionario(rs.getInt("id_Funcionario"))
                .idDespesaParcelada(rs.getInt("id_DespesaParcelada"))
                .dsNomeDespesa(rs.getString("ds_DescricaoDespesa"))
                .valorDespesa(rs.getString("vl_Fatura"))
                .nrParcelasAdiantadas(rs.getInt("nr_ParcelasAdiantadas"))
                .statusDespesa(rs.getString("statusDespesa"))
                .dataAssociacao(rs.getString("dt_Associacao"))
                .build();
    }
}
