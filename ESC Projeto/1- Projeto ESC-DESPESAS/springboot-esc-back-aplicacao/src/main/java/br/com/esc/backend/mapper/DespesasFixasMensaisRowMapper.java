package br.com.esc.backend.mapper;

import br.com.esc.backend.domain.DespesasFixasMensaisDAO;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class DespesasFixasMensaisRowMapper implements RowMapper<DespesasFixasMensaisDAO> {

    @Override
    public DespesasFixasMensaisDAO map(ResultSet rs, StatementContext ctx) throws SQLException {
        DespesasFixasMensaisDAO dao = new DespesasFixasMensaisDAO();

        dao.setId_Despesa(rs.getInt("id_Despesa"));
        dao.setId_Ordem(rs.getInt("id_Ordem"));
        dao.setDs_Descricao(rs.getString("ds_Descricao"));
        dao.setVl_Total(rs.getString("vl_Total"));
        dao.setTp_Status(rs.getString("tp_Status"));
        dao.setTp_FixasObrigatorias(rs.getString("tpFixasObrigatorias"));
        dao.setTp_DespesaDebitoCartao(rs.getString("tp_DespesaDebitoCartao"));

        log.info("DespesasFixasMensaisRowMapper >> {}", dao);
        return dao;
    }
}
