package br.com.esc.backend.mapper;

import br.com.esc.backend.domain.LembretesDAO;
import br.com.esc.backend.domain.ParcelasDAO;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class LembretesRowMapper implements RowMapper<LembretesDAO> {

    @Override
    public LembretesDAO map(ResultSet rs, StatementContext ctx) throws SQLException {
        return LembretesDAO.builder()
                .idLembrete(rs.getInt("id_Lembrete"))
                .idFuncionario(rs.getInt("id_Funcionario"))
                .numeroDias(rs.getInt("nr_NumeroDias"))
                .dsTituloLembrete(rs.getString("ds_TituloLembrete"))
                .tpHabilitaNotificacaoDiaria(rs.getString("chkHabilitarNotificacaoDiaria"))
                .tpSegunda(rs.getString("tp_Segunda"))
                .tpTerca(rs.getString("tp_Terca"))
                .tpQuarta(rs.getString("tp_Quarta"))
                .tpQuinta(rs.getString("tp_Quinta"))
                .tpSexta(rs.getString("tp_Sexta"))
                .tpSabado(rs.getString("tp_Sabado"))
                .tpDomingo(rs.getString("tp_Domingo"))
                .tpBaixado(rs.getString("tp_Baixado"))
                .tpContagemRegressiva(rs.getString("tp_LembreteContagemRegressiva"))
                .tpLembreteDatado(rs.getString("tp_LembreteDatado"))
                .tpRenovarAuto(rs.getString("tp_RenovarAuto"))
                .dataInicial(rs.getString("ds_DataInicial"))
                .dsObservacoes(rs.getString("ds_Observacoes"))
                .data1(rs.getString("data1"))
                .data2(rs.getString("data2"))
                .data3(rs.getString("data3"))
                .data4(rs.getString("data4"))
                .data5(rs.getString("data5"))
                .build();
    }
}
