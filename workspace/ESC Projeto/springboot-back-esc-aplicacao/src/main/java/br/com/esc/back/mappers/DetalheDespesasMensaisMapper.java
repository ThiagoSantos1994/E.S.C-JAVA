package br.com.esc.back.mappers;

import br.com.esc.back.domain.DetalheDespesasMensais;
import br.com.esc.back.domain.ListaDetalheDespesasMensais;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DetalheDespesasMensaisMapper implements RowMapper<ListaDetalheDespesasMensais> {

    @Override
    public ListaDetalheDespesasMensais mapRow(ResultSet rs, int rowNum) throws SQLException {
        ListaDetalheDespesasMensais despesasMensais = new ListaDetalheDespesasMensais();

        despesasMensais.setId_Despesa(rs.getInt("id_Despesa"));
        despesasMensais.setId_Ordem(rs.getInt("id_Ordem"));
        despesasMensais.setId_DetalheDespesa(rs.getInt("id_DetalheDespesa"));
        despesasMensais.setId_DespesaParcelada(rs.getInt("id_DespesaParcelada"));
        despesasMensais.setId_Parcela(rs.getInt("id_Parcela"));
        despesasMensais.setId_Funcionario(rs.getInt("id_Funcionario"));
        despesasMensais.setId_DespesaLinkRelatorio(rs.getInt("id_DespesaLinkRelatorio"));
        despesasMensais.setVl_Total(rs.getString("vl_Total"));
        despesasMensais.setDs_Descricao(rs.getString("ds_Descricao"));
        despesasMensais.setVl_TotalPago(rs.getString("vl_TotalPago"));
        despesasMensais.setTp_Status(rs.getString("tp_Status"));
        despesasMensais.setDs_Observacao(rs.getString("ds_Observacao"));
        despesasMensais.setDs_Observacao2(rs.getString("ds_Observacao2"));
        despesasMensais.setTp_Reprocessar(rs.getString("tp_Reprocessar"));
        despesasMensais.setTp_Anotacao(rs.getString("tp_Anotacao"));
        despesasMensais.setTp_Meta(rs.getString("tp_Meta"));
        despesasMensais.setTp_ParcelaAdiada(rs.getString("tp_ParcelaAdiada"));
        despesasMensais.setTp_ParcelaAmortizada(rs.getString("tp_ParcelaAmortizada"));
        despesasMensais.setTp_Relatorio(rs.getString("tp_Relatorio"));
        despesasMensais.setTp_LinhaSeparacao(rs.getString("tp_LinhaSeparacao"));

        return despesasMensais;
    }
}
