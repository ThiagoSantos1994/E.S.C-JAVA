package br.com.esc.back.mapper;

import br.com.esc.back.domain.ListaDespesasMensais;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DespesasMensaisMapper implements RowMapper<ListaDespesasMensais> {

    @Override
    public ListaDespesasMensais mapRow(ResultSet rs, int rowNum) throws SQLException {
        ListaDespesasMensais despesasMensais = new ListaDespesasMensais();

        despesasMensais.setId_Despesa(rs.getInt("id_Despesa"));
        despesasMensais.setId_OrdemExibicao(rs.getInt("id_OrdemExibicao"));
        despesasMensais.setDs_NomeDespesa(rs.getString("ds_NomeDespesa"));
        despesasMensais.setVl_Limite(rs.getString("vl_Limite"));
        despesasMensais.setId_DetalheDespesa(rs.getInt("id_DetalheDespesa"));
        despesasMensais.setTp_Emprestimo(rs.getString("tp_Emprestimo"));
        despesasMensais.setId_Emprestimo(rs.getInt("id_Emprestimo"));
        despesasMensais.setTp_Poupanca(rs.getString("tp_Poupanca"));
        despesasMensais.setTp_Anotacao(rs.getString("tp_Anotacao"));
        despesasMensais.setTp_DebitoAutomatico(rs.getString("tp_DebitoAutomatico"));
        despesasMensais.setTp_LinhaSeparacao(rs.getString("tp_LinhaSeparacao"));
        despesasMensais.setTp_DespesaReversa(rs.getString("tp_DespesaReversa"));
        despesasMensais.setTp_PoupancaNegativa(rs.getString("tp_PoupancaNegativa"));
        despesasMensais.setTp_Relatorio(rs.getString("tp_Relatorio"));

        return despesasMensais;
    }
}
