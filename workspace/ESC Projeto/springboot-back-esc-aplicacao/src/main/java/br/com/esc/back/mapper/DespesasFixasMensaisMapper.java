package br.com.esc.back.mapper;

import br.com.esc.back.domain.ListaDespesasFixasMensais;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DespesasFixasMensaisMapper implements RowMapper<ListaDespesasFixasMensais> {

    @Override
    public ListaDespesasFixasMensais mapRow(ResultSet rs, int rowNum) throws SQLException {
        ListaDespesasFixasMensais fixasMensais = new ListaDespesasFixasMensais();

        fixasMensais.setDs_Descricao(rs.getString("ds_Descricao"));
        fixasMensais.setVl_Total(rs.getString("vl_Total"));
        fixasMensais.setTp_Status(rs.getString("tp_Status"));
        fixasMensais.setId_Despesa(rs.getInt("id_Despesa"));
        fixasMensais.setId_Ordem(rs.getInt("id_Ordem"));

        return fixasMensais;
    }
}
