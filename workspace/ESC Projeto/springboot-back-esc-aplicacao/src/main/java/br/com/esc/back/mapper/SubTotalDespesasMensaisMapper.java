package br.com.esc.back.mapper;

import br.com.esc.back.domain.SubTotalDespesasMensais;
import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
public class SubTotalDespesasMensaisMapper implements RowMapper<SubTotalDespesasMensais> {

    @Override
    public SubTotalDespesasMensais mapRow(ResultSet rs, int rowNum) throws SQLException {
        SubTotalDespesasMensais despesasMensais = new SubTotalDespesasMensais();

        despesasMensais.setVl_SaldoPositivoMes(rs.getDouble("vl_SaldoPositivoMes"));
        despesasMensais.setVl_SaldoPendentePagamentoMes(rs.getDouble("vl_SaldoPendentePagamentoMes"));
        despesasMensais.setVl_SaldoSubTotalMes((rs.getDouble("vl_SaldoPositivoMes") - rs.getDouble("vl_SaldoSubTotalMes")));

        return despesasMensais;
    }

}
