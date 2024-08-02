package br.com.esc.backend.mapper;

import br.com.esc.backend.domain.LancamentosMensaisDAO;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class LancamentosMensaisRowMapper implements RowMapper<LancamentosMensaisDAO> {

    @Override
    public LancamentosMensaisDAO map(ResultSet rs, StatementContext ctx) throws SQLException {
        return LancamentosMensaisDAO.builder()
                .dsTituloDespesa(rs.getString("ds_TituloDespesa"))
                .dsNomeDespesa(rs.getString("ds_NomeDespesa"))
                .vlLimite(rs.getString("vl_Limite"))
                .vlTotalDespesa(rs.getBigDecimal("vl_TotalDespesa"))
                .idDetalheDespesa(rs.getInt("id_DetalheDespesa"))
                .idEmprestimo(rs.getInt("id_Emprestimo"))
                .idOrdemExibicao(rs.getInt("id_OrdemExibicao"))
                .tpEmprestimo(rs.getString("tp_Emprestimo"))
                .tpPoupanca(rs.getString("tp_Poupanca"))
                .tpAnotacao(rs.getString("tp_Anotacao"))
                .tpDebitoAutomatico(rs.getString("tp_DebitoAutomatico"))
                .tpLinhaSeparacao(rs.getString("tp_LinhaSeparacao"))
                .tpDespesaReversa(rs.getString("tp_DespesaReversa"))
                .tpPoupancaNegativa(rs.getString("tp_PoupancaNegativa"))
                .tpRelatorio(rs.getString("tp_Relatorio"))
                .tpReferenciaSaldoMesAnterior(rs.getString("tp_ReferenciaSaldoMesAnterior"))
                .tpDespesaCompartilhada(rs.getString("tp_DespesaCompartilhada"))
                .tpDebitoCartao(rs.getString("tp_DebitoCartao"))
                .build();
    }
}
