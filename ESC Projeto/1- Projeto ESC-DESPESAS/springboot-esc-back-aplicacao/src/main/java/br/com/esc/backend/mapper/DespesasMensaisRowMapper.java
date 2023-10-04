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
        DespesasMensaisDAO dao = new DespesasMensaisDAO();

        dao.setDs_NomeDespesa(rs.getString("ds_NomeDespesa"));
        dao.setVl_Limite(rs.getString("vl_Limite"));
        dao.setVl_TotalDespesa(rs.getBigDecimal("vl_TotalDespesa"));
        dao.setId_DetalheDespesa(rs.getInt("id_DetalheDespesa"));
        dao.setTp_Emprestimo(rs.getString("tp_Emprestimo"));
        dao.setId_Emprestimo(rs.getInt("id_Emprestimo"));
        dao.setTp_Poupanca(rs.getString("tp_Poupanca"));
        dao.setTp_Anotacao(rs.getString("tp_Anotacao"));
        dao.setTp_DebitoAutomatico(rs.getString("tp_DebitoAutomatico"));
        dao.setId_OrdemExibicao(rs.getInt("id_OrdemExibicao"));
        dao.setTp_LinhaSeparacao(rs.getString("tp_LinhaSeparacao"));
        dao.setTp_DespesaReversa(rs.getString("tp_DespesaReversa"));
        dao.setTp_PoupancaNegativa(rs.getString("tp_PoupancaNegativa"));
        dao.setTp_Relatorio(rs.getString("tp_Relatorio"));
        dao.setTp_ReferenciaSaldoMesAnterior(rs.getString("tp_ReferenciaSaldoMesAnterior"));
        dao.setTp_DespesaCompartilhada(rs.getString("tp_DespesaCompartilhada"));

        log.info("DetalheDespesasMensaisRowMapper >> {}", dao);
        return dao;
    }
}
