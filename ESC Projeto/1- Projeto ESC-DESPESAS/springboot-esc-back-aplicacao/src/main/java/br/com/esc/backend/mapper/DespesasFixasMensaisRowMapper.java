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
        return DespesasFixasMensaisDAO.builder()
                .idDespesa(rs.getInt("id_Despesa"))
                .dsDescricao(rs.getString("ds_Descricao"))
                .vlTotal(rs.getString("vl_Total"))
                .tpStatus(rs.getString("tp_Status"))
                .dsMes(rs.getString("ds_Mes"))
                .dsAno(rs.getString("ds_Ano"))
                .idFuncionario(rs.getInt("id_Funcionario"))
                .idOrdem(rs.getInt("id_Ordem"))
                .tpFixasObrigatorias(rs.getString("tpFixasObrigatorias"))
                .tpDespesaDebitoCartao(rs.getString("tp_DespesaDebitoCartao"))
                .build();
    }
}
