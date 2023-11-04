package br.com.esc.backend.mapper;

import br.com.esc.backend.domain.DespesaParceladaDAO;
import br.com.esc.backend.domain.ParcelasDAO;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class DespesaParceladaRowMapper implements RowMapper<DespesaParceladaDAO> {

    @Override
    public DespesaParceladaDAO map(ResultSet rs, StatementContext ctx) throws SQLException {
        return DespesaParceladaDAO.builder()
                .idDespesaParcelada(rs.getInt("id_DespesaParcelada"))
                .dsTituloDespesaParcelada(rs.getString("ds_TituloDespesaParcelada"))
                .dsMesVigIni(rs.getString("ds_MesVigIni"))
                .dsAnoVigIni(rs.getString("ds_AnoVigIni"))
                .nrTotalParcelas(rs.getInt("nr_TotalParcelas"))
                .dsVigenciaFin(rs.getString("ds_VigenciaFin"))
                .vlFatura(rs.getString("vl_Fatura"))
                .idFuncionario(rs.getInt("id_Funcionario"))
                .nrParcelasAdiantadas(rs.getInt("nr_ParcelasAdiantadas"))
                .tpBaixado(rs.getString("tp_Baixado"))
                .dtCadastro(rs.getString("dt_Cadastro"))
                .build();
    }
}
