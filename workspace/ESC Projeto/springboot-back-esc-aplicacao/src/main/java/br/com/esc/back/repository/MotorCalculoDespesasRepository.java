package br.com.esc.back.repository;

import br.com.esc.back.domain.SubTotalDespesasMensais;
import br.com.esc.back.mapper.SubTotalDespesasMensaisMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MotorCalculoDespesasRepository {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private JdbcTemplate jdbcTemplate;
    private DataSource dataSource;

    @Autowired
    public MotorCalculoDespesasRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    public Double getValorDespesaTotal(int id_Despesa, int id_DetalheDespesa, int id_Funcionario) {
        Double vlTotalDespesa = 0d;

        String sQuery = "SELECT ISNULL(SUM(CAST(REPLACE(REPLACE(vl_Total, '.', ''), ',', '.') AS DECIMAL(10,2))),0) " +
                "FROM tbd_DetalheDespesasMensais " +
                "WHERE id_Despesa = ? AND id_DetalheDespesa = ? AND id_Funcionario = ? AND tp_Anotacao = 'N' AND tp_LinhaSeparacao = 'N'";

        logger.info("Consulta: " + sQuery);

        vlTotalDespesa = jdbcTemplate.queryForObject(
                sQuery,
                new Object[]{id_Despesa, id_DetalheDespesa, id_Funcionario}, Double.class);

        return vlTotalDespesa;
    }

    public Double getValorDespesasPendentes(int id_Despesa, int id_DetalheDespesa, int id_Funcionario) {
        Double vlTotalAPagar = 0d;

        String sQuery = "SELECT ISNULL(SUM(CAST(REPLACE(REPLACE(vl_Total, '.', ''), ',', '.') AS DECIMAL(10,2))),0) " +
                "FROM tbd_DetalheDespesasMensais " +
                "WHERE id_Despesa = ? AND id_DetalheDespesa = ? AND id_Funcionario = ? AND tp_Anotacao = 'N' AND tp_LinhaSeparacao = 'N' AND tp_Status = 'Pendente'";

        logger.info("Consulta: " + sQuery);

        vlTotalAPagar = jdbcTemplate.queryForObject(
                sQuery,
                new Object[]{id_Despesa, id_DetalheDespesa, id_Funcionario}, Double.class);

        return vlTotalAPagar;
    }

    public Double getValorDespesasPagas(int id_Despesa, int id_DetalheDespesa, int id_Funcionario) {
        Double vlTotalPago = 0d;

        String sQuery = "SELECT ISNULL(SUM(CAST(REPLACE(REPLACE(vl_Total, '.', ''), ',', '.') AS DECIMAL(10,2))),0) AS totalPago " +
                "FROM tbd_DetalheDespesasMensais " +
                "WHERE id_Despesa = ? AND id_DetalheDespesa = ? AND id_Funcionario = ? AND tp_Anotacao = 'N' AND tp_LinhaSeparacao = 'N' AND tp_Status = 'Pago'";

        logger.info("Consulta: " + sQuery);

        vlTotalPago = jdbcTemplate.queryForObject(
                sQuery,
                new Object[]{id_Despesa, id_DetalheDespesa, id_Funcionario}, Double.class);

        return vlTotalPago;
    }

    public SubTotalDespesasMensais getSubTotalDespesas(String ds_Mes, String ds_Ano, Integer id_Despesa, String id_Usuario) {

        String sQuery = "SELECT (TOTAIS.SaldoPositivoMes - TOTAIS.SaldoDespesasMes) AS vl_SaldoPositivoMes, TOTAIS.SaldoPendentePagamento AS vl_SaldoPendentePagamentoMes, TOTAIS.SaldoSubTotalMes AS vl_SaldoSubTotalMes " +
                "FROM " +
                "(SELECT " +
                "       (SELECT ISNULL(SUM(CAST(REPLACE(REPLACE(a.vl_Total, '.', ''), ',', '.') AS DECIMAL(10,2))),0) FROM tbd_DespesasFixasMensais a WHERE a.ds_Mes = '" + ds_Mes + "' AND a.ds_ano = '" + ds_Ano + "' AND id_Funcionario = " + id_Usuario + " AND a.tp_Status = '+') AS SaldoPositivoMes, " +
                "       (SELECT ISNULL(SUM(CAST(REPLACE(REPLACE(b.vl_Total, '.', ''), ',', '.') AS DECIMAL(10,2))),0) FROM tbd_DespesasFixasMensais b WHERE b.ds_Mes = '" + ds_Mes + "' AND b.ds_ano = '" + ds_Ano + "' AND id_Funcionario = " + id_Usuario + " AND b.tp_Status IN ('-', '->')) AS SaldoDespesasMes, " +
                "       (SELECT ISNULL(SUM(CAST(REPLACE(REPLACE(vl_Total, '.', ''), ',', '.') AS DECIMAL(10,2))),0) FROM tbd_DetalheDespesasMensais WHERE id_Despesa = " + id_Despesa + " AND id_Funcionario = " + id_Usuario + " AND tp_LinhaSeparacao = 'N' AND tp_Anotacao = 'N' AND id_DetalheDespesa IN ( " +
                "           SELECT id_DetalheDespesa FROM tbd_DespesaMensal WHERE id_Despesa = " + id_Despesa + " AND id_Funcionario = " + id_Usuario + " AND (tp_LinhaSeparacao IS NULL OR tp_LinhaSeparacao = 'N') AND tp_Poupanca = 'N' AND tp_PoupancaNegativa = 'N' AND tp_Anotacao = 'N' AND tp_Relatorio = 'N' AND tp_Status = 'Pendente') " +
                "       ) AS SaldoPendentePagamento, " +
                "       (SELECT ISNULL(SUM(CAST(REPLACE(REPLACE(vl_Total, '.', ''), ',', '.') AS DECIMAL(10,2))),0) FROM tbd_DetalheDespesasMensais WHERE id_Despesa = " + id_Despesa + " AND id_Funcionario = " + id_Usuario + " AND tp_LinhaSeparacao = 'N' AND tp_Anotacao = 'N' AND id_DetalheDespesa IN ( " +
                "           SELECT id_DetalheDespesa FROM tbd_DespesaMensal WHERE id_Despesa = " + id_Despesa + " AND id_Funcionario = " + id_Usuario + " AND (tp_LinhaSeparacao IS NULL OR tp_LinhaSeparacao = 'N') AND tp_Poupanca = 'N' AND tp_PoupancaNegativa = 'N' AND tp_Anotacao = 'N' AND tp_Relatorio = 'N') " +
                "       ) AS SaldoSubTotalMes " +
                ") AS TOTAIS";

        logger.info("Consulta: " + sQuery);

        List<SubTotalDespesasMensais> despesasMensais = jdbcTemplate.query(
                sQuery,
                new SubTotalDespesasMensaisMapper());

        return despesasMensais.get(0);
    }

}
