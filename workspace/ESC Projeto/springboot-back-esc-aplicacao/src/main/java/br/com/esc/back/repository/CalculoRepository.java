package br.com.esc.back.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class CalculoRepository {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private JdbcTemplate jdbcTemplate;
    private DataSource dataSource;

    @Autowired
    public CalculoRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
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
}
