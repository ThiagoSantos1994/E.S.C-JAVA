package br.com.esc.backend.mapper;

import br.com.esc.backend.domain.ConfiguracaoLancamentosResponse;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.Boolean.valueOf;

@Slf4j
public class ConfiguracaoLancamentosRowMapper implements RowMapper<ConfiguracaoLancamentosResponse> {

    @Override
    public ConfiguracaoLancamentosResponse map(ResultSet rs, StatementContext ctx) throws SQLException {
        return ConfiguracaoLancamentosResponse.builder()
                .dataViradaMes(rs.getInt("dt_ViradaMes"))
                .mesReferencia(rs.getInt("ds_MesReferencia"))
                .anoReferencia(rs.getInt("ds_AnoReferencia"))
                .bViradaAutomatica((rs.getString("tp_ViradaAutomatica").equalsIgnoreCase("S") ? valueOf(true) : valueOf(false)))
                .idFuncionario(rs.getInt("id_Funcionario"))
                .qtdeAcessos(rs.getInt("qt_Acessos"))
                .build();
    }
}
