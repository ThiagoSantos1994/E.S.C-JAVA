package br.esc.software.repository.mapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DiretorioMapper implements RowMapper<String> {

    @Override
    public String mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getString("tp_DiretorioArquivoJava");
    }
}
