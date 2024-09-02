package br.com.esc.backend.mapper;

import br.com.esc.backend.domain.DetalheDespesasMensaisDAO;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

import static br.com.esc.backend.utils.MotorCalculoUtils.convertStringToDecimal;

@Slf4j
public class DetalheDespesasMensaisRowMapper implements RowMapper<DetalheDespesasMensaisDAO> {

    @Override
    public DetalheDespesasMensaisDAO map(ResultSet rs, StatementContext ctx) throws SQLException {
        if (rs.getString("tp_LinhaSeparacao").equalsIgnoreCase("S")) {
            return DetalheDespesasMensaisDAO.builder()
                    .idDespesa(rs.getInt("id_Despesa"))
                    .idDetalheDespesa(rs.getInt("id_DetalheDespesa"))
                    .idConsolidacao(rs.getInt("id_Consolidacao"))
                    .idDespesaConsolidacao(rs.getInt("id_DespesaConsolidacao"))
                    .idOrdem(rs.getInt("id_Ordem"))
                    .idFuncionario(rs.getInt("id_Funcionario"))
                    //.vlTotal(convertStringToDecimal(rs.getString("vl_Total")))
                    //.vlTotalPago(convertStringToDecimal(rs.getString("vl_TotalPago")))
                    .vlTotal(rs.getString("vl_Total"))
                    .vlTotalPago(rs.getString("vl_TotalPago"))
                    .dsObservacao(rs.getString("ds_Observacao"))
                    .dsObservacao2(rs.getString("ds_Observacao2"))
                    .tpCategoriaDespesa(rs.getString("tp_CategoriaDespesa"))
                    .tpStatus(rs.getString("tp_Status"))
                    .tpReprocessar(rs.getString("tp_Reprocessar"))
                    .tpAnotacao(rs.getString("tp_Anotacao"))
                    .tpRelatorio(rs.getString("tp_Relatorio"))
                    .tpLinhaSeparacao(rs.getString("tp_LinhaSeparacao"))
                    .tpParcelaAdiada(rs.getString("tp_ParcelaAdiada"))
                    .tpParcelaAmortizada(rs.getString("tp_ParcelaAmortizada"))
                    .tpMeta(rs.getString("tp_Meta"))
                    .build();
        }

        return DetalheDespesasMensaisDAO.builder()
                .idDespesa(rs.getInt("id_Despesa"))
                .idDetalheDespesa(rs.getInt("id_DetalheDespesa"))
                .dsTituloDespesa(rs.getString("ds_TituloDespesa"))
                .dsDescricao(rs.getString("ds_Descricao"))
                .idOrdem(rs.getInt("id_Ordem"))
                .idConsolidacao(rs.getInt("id_Consolidacao"))
                .idDespesaConsolidacao(rs.getInt("id_DespesaConsolidacao"))
                .idParcela(rs.getInt("id_Parcela"))
                .idDespesaParcelada(rs.getInt("id_DespesaParcelada"))
                .idFuncionario(rs.getInt("id_Funcionario"))
                .idDespesaLinkRelatorio(rs.getInt("id_DespesaLinkRelatorio"))
                //.vlTotal(convertStringToDecimal(rs.getString("vl_Total")))
                //.vlTotalPago(convertStringToDecimal(rs.getString("vl_TotalPago")))
                .vlTotal(rs.getString("vl_Total"))
                .vlTotalPago(rs.getString("vl_TotalPago"))
                .dsObservacao(rs.getString("ds_Observacao"))
                .dsObservacao2(rs.getString("ds_Observacao2"))
                .tpCategoriaDespesa(rs.getString("tp_CategoriaDespesa"))
                .tpStatus(rs.getString("tp_Status"))
                .tpReprocessar(rs.getString("tp_Reprocessar"))
                .tpAnotacao(rs.getString("tp_Anotacao"))
                .tpRelatorio(rs.getString("tp_Relatorio"))
                .tpLinhaSeparacao(rs.getString("tp_LinhaSeparacao"))
                .tpParcelaAdiada(rs.getString("tp_ParcelaAdiada"))
                .tpParcelaAmortizada(rs.getString("tp_ParcelaAmortizada"))
                .tpMeta(rs.getString("tp_Meta"))
                .build();
    }
}
