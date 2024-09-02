package br.com.esc.backend.mapper;

import br.com.esc.backend.domain.ConsolidacaoDAO;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class ConsolidacaoRowMapper implements RowMapper<ConsolidacaoDAO> {

    @Override
    public ConsolidacaoDAO map(ResultSet rs, StatementContext ctx) throws SQLException {
        return ConsolidacaoDAO.builder()
                .idConsolidacao(rs.getInt("id_Consolidacao"))
                .dsTituloConsolidacao(rs.getString("ds_TituloConsolidacao"))
                .tpBaixado(rs.getString("tp_Baixado"))
                .idFuncionario(rs.getInt("id_Funcionario"))
                .dataCadastro(rs.getString("dt_Cadastro"))
                .build();
    }
}
