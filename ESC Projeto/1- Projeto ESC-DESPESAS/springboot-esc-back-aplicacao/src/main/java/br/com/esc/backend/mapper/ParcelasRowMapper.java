package br.com.esc.backend.mapper;

import br.com.esc.backend.domain.ParcelasDAO;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

import static br.com.esc.backend.utils.MotorCalculoUtils.convertStringToDecimal;

@Slf4j
public class ParcelasRowMapper implements RowMapper<ParcelasDAO> {

    @Override
    public ParcelasDAO map(ResultSet rs, StatementContext ctx) throws SQLException {
        return ParcelasDAO.builder()
                .idDespesaParcelada(rs.getInt("id_DespesaParcelada"))
                .idParcela(rs.getInt("id_Parcelas"))
                .nrParcela(rs.getString("nr_Parcela"))
                //.vlParcela(convertStringToDecimal(rs.getString("vl_Parcela")))
                //.vlDesconto(convertStringToDecimal(rs.getString("vl_Desconto")))
                .vlParcela(rs.getString("vl_Parcela"))
                .vlDesconto(rs.getString("vl_Desconto"))
                .idDespesa(rs.getInt("id_Despesa"))
                .idDetalheDespesa(rs.getInt("id_DetalheDespesa"))
                .idFuncionario(rs.getInt("id_Funcionario"))
                .dsDataVencimento(rs.getString("ds_DataVencimento"))
                .dsObservacoes(rs.getString("ds_Observacoes"))
                .tpBaixado(rs.getString("tp_Baixado"))
                .tpQuitado(rs.getString("tp_Quitado"))
                .tpParcelaAdiada(rs.getString("tp_ParcelaAdiada"))
                .tpParcelaAmortizada(rs.getString("tp_ParcelaAmortizada"))
                .build();
    }
}
