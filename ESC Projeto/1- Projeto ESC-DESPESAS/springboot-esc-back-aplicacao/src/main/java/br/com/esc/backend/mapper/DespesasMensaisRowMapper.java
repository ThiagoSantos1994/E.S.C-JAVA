package br.com.esc.backend.mapper;

import br.com.esc.backend.domain.DespesasMensaisDAO;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class DespesasMensaisRowMapper implements RowMapper<DespesasMensaisDAO> {

    @Override
    public DespesasMensaisDAO map(ResultSet rs, StatementContext ctx) throws SQLException {
        if (rs.getString("tp_LinhaSeparacao").equalsIgnoreCase("S")) {
            return DespesasMensaisDAO.builder()
                    .idDespesa(rs.getInt("id_Despesa"))
                    .idDetalheDespesa(rs.getInt("id_DetalheDespesa"))
                    .idOrdemExibicao(rs.getInt("id_OrdemExibicao"))
                    .idFuncionario(rs.getInt("id_Funcionario"))
                    .idEmprestimo(rs.getInt("id_Emprestimo"))
                    .tpLinhaSeparacao(rs.getString("tp_LinhaSeparacao"))
                    .build();
        }

        return DespesasMensaisDAO.builder()
                .idDespesa(rs.getInt("id_Despesa"))
                .idDetalheDespesa(rs.getInt("id_DetalheDespesa"))
                .dsNomeDespesa(rs.getString("ds_NomeDespesa"))
                .dsTituloDespesa(rs.getString("ds_TituloDespesa"))
                //.vlLimite(rs.getBigDecimal("vl_Limite"))
                .vlLimite(rs.getString("vl_Limite"))
                .idOrdemExibicao(rs.getInt("id_OrdemExibicao"))
                .idFuncionario(rs.getInt("id_Funcionario"))
                .idEmprestimo(rs.getInt("id_Emprestimo"))
                .tpReprocessar(rs.getString("tp_Reprocessar"))
                .tpEmprestimo(rs.getString("tp_Emprestimo"))
                .tpPoupanca(rs.getString("tp_Poupanca"))
                .tpAnotacao(rs.getString("tp_Anotacao"))
                .tpDebitoAutomatico(rs.getString("tp_DebitoAutomatico"))
                .tpMeta(rs.getString("tp_Meta"))
                .tpLinhaSeparacao(rs.getString("tp_LinhaSeparacao"))
                .tpDespesaReversa(rs.getString("tp_DespesaReversa"))
                .tpPoupancaNegativa(rs.getString("tp_PoupancaNegativa"))
                .tpRelatorio(rs.getString("tp_Relatorio"))
                .tpDebitoCartao(rs.getString("tp_DebitoCartao"))
                .tpEmprestimoAPagar(rs.getString("tp_EmprestimoAPagar"))
                .tpReferenciaSaldoMesAnterior(rs.getString("tp_ReferenciaSaldoMesAnterior"))
                .tpVisualizacaoTemp(rs.getString("tp_VisualizacaoTemp"))
                .tpDespesaCompartilhada(rs.getString("tp_DespesaCompartilhada"))
                .build();
    }
}
