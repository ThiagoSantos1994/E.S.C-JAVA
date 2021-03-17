package br.esc.software.repository;

import br.esc.software.commons.exceptions.ExcecaoGlobal;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import static br.esc.software.commons.utils.GlobalUtils.LogErro;
import static br.esc.software.configuration.ConnectionSQL.Select_Table;

@Repository
public class MotorCalculoRepository {

    private ResultSet RSAdo;
    private Double result = 0d;

    public Double getValorTotalDespesas(Integer ano) {
        result = 0d;

        try {
            RSAdo = Select_Table(
                    "SELECT ISNULL(SUM(CAST(REPLACE(REPLACE(REPLACE(a.vl_Total, '.', ''), ',', '.'), '-', '') AS DECIMAL(10,2))),0) AS calculo from tbd_DetalheDespesasMensais a INNER JOIN tbd_DespesaMensal b on a.id_Despesa = b.id_Despesa and a.id_DetalheDespesa = b.id_DetalheDespesa WHERE a.id_Despesa in (select DISTINCT(id_Despesa) from tbd_DespesasFixasMensais where ds_Ano = "
                            + ano
                            + ") and a.tp_Meta = 'N' and a.tp_ParcelaAdiada = 'N' and a.tp_Anotacao = 'N' and b.tp_Emprestimo = 'N' and b.tp_Poupanca = 'N' and b.tp_Anotacao = 'N' and b.tp_Relatorio = 'N' and b.tp_Emprestimo = 'N' and a.id_Funcionario = 2 and ISNULL(CAST(REPLACE(REPLACE(REPLACE(a.vl_Total, '.', ''), ',', '.'), '-', '') AS DECIMAL(10,2)),0) > 0");
            while (RSAdo.next()) {
                result = RSAdo.getDouble("calculo");
            }
            RSAdo.close();
        } catch (SQLException e) {
            LogErro("Erro ao realizar o calculo " + e);
            return 0d;
        } catch (ExcecaoGlobal e) {
            LogErro("Erro ao realizar o calculo " + e);
            return 0d;
        }

        return result;
    }

    public Double getValorTotalReceitaPositiva(Integer ano) {
        result = 0d;

        try {
            RSAdo = Select_Table(
                    "SELECT ISNULL(SUM(CAST(REPLACE(REPLACE(vl_Total, '.', ''), ',', '.') AS DECIMAL(10,2))),0) AS calculo FROM tbd_DespesasFixasMensais where ds_Ano = "
                            + ano + " and tp_Status = '+' and id_Funcionario = 2");
            while (RSAdo.next()) {
                result = RSAdo.getDouble("calculo");
            }
            RSAdo.close();
        } catch (SQLException e) {
            LogErro("Erro ao realizar o calculo " + e);
            return 0d;
        } catch (ExcecaoGlobal e) {
            LogErro("Erro ao realizar o calculo " + e);
            return 0d;
        }

        return result;
    }

    public Double getValorTotalAplicadoPoupanca() {
        result = 0d;
        Double vlSaldoPositivo = 0d;
        Double vlSaldoNegativo = 0d;

        try {

            /* DESPESAS MENSAIS CATEGORIA POUPANCA + */
            RSAdo = Select_Table(
                    "SELECT ISNULL(SUM(CAST(REPLACE(REPLACE(a.vl_Total, '.', ''), ',', '.') AS DECIMAL(10,2))),0) AS calculoPositivo FROM tbd_DetalheDespesasMensais a INNER JOIN tbd_DespesaMensal b on a.id_Despesa = b.id_Despesa and a.id_DetalheDespesa = b.id_DetalheDespesa WHERE a.tp_Anotacao = 'N' and a.id_Funcionario = 2 and b.id_Despesa IN ( SELECT DISTINCT id_Despesa FROM tbd_DespesasFixasMensais WHERE ds_Ano >= 2020 AND tp_Poupanca = 'S' AND id_Funcionario = 2)");
            while (RSAdo.next()) {
                vlSaldoPositivo = RSAdo.getDouble("calculoPositivo");
            }
            RSAdo.close();

            /* DESPESAS MENSAIS CATEGORIA POUPANCA - */
            RSAdo = Select_Table(
                    "SELECT ISNULL(SUM(CAST(REPLACE(REPLACE(a.vl_Total, '.', ''), ',', '.') AS DECIMAL(10,2))),0) AS calculoNegativo FROM tbd_DetalheDespesasMensais a INNER JOIN tbd_DespesaMensal b on a.id_Despesa = b.id_Despesa and a.id_DetalheDespesa = b.id_DetalheDespesa WHERE a.tp_Anotacao = 'N' and a.id_Funcionario = 2 and b.id_Despesa IN ( SELECT DISTINCT id_Despesa FROM tbd_DespesasFixasMensais WHERE ds_Ano >= 2020 AND tp_PoupancaNegativa = 'S' AND id_Funcionario = 2)");
            while (RSAdo.next()) {
                vlSaldoNegativo = RSAdo.getDouble("calculoNegativo");
            }
            RSAdo.close();

            /* DESPESAS FIXAS MENSAIS - STTS P+ */
            RSAdo = Select_Table(
                    "SELECT ISNULL(SUM(CAST(REPLACE(REPLACE(vl_Total, '.', ''), ',', '.') AS DECIMAL(10,2))),0) AS calculoSttsPositivo FROM tbd_DespesasFixasMensais WHERE CAST(ds_Ano AS INT) >= '2020' and tp_Status = 'P(+)' and id_Funcionario = 2");
            while (RSAdo.next()) {
                vlSaldoPositivo += RSAdo.getDouble("calculoSttsPositivo");
            }
            RSAdo.close();

            /* DESPESAS FIXAS MENSAIS - STTS P- */
            RSAdo = Select_Table(
                    "SELECT ISNULL(SUM(CAST(REPLACE(REPLACE(vl_Total, '.', ''), ',', '.') AS DECIMAL(10,2))),0) AS calculoSttsNegativo FROM tbd_DespesasFixasMensais WHERE CAST(ds_Ano AS INT) >= '2020' and tp_Status = 'P(-)' and id_Funcionario = 2");
            while (RSAdo.next()) {
                vlSaldoNegativo += RSAdo.getDouble("calculoSttsNegativo");
            }
            RSAdo.close();

            /* EMPRESTIMOS MARCADOS PARA CONTABILIZAR SALDO NA POUPANCA (APARTIR DE 2020) */
            RSAdo = Select_Table(
                    "SELECT ISNULL(SUM(CAST(REPLACE(REPLACE(a.vl_Pago, '.', ''), ',', '.') AS DECIMAL(10,2))),0) AS emprestimoMarcadosPoupanca FROM tbd_PagamentoEmprestimo a INNER JOIN tbd_Emprestimos b on a.id_Emprestimo = b.id_Emprestimo WHERE b.ds_Ano >= 2020 and b.tp_ContabilizarPoupanca = 'S' and b.id_Funcionario = 2 and a.tp_AdicionarPoupanca = 'S'");
            while (RSAdo.next()) {
                vlSaldoPositivo += RSAdo.getDouble("emprestimoMarcadosPoupanca");
            }
            RSAdo.close();

        } catch (ExcecaoGlobal e) {
            LogErro("Erro ao realizar o calculo " + e);
            return 0d;
        } catch (SQLException e) {
            LogErro("Erro ao realizar o calculo " + e);
            return 0d;
        }

        result = (vlSaldoPositivo - vlSaldoNegativo);
        return result;
    }

    public Double getValorTotalEmprestimosAReceber() {
        result = 0d;

        /* EMPRESTIMOS A RECEBER (TODOS OS ANOS) CONTABILIZANDO NA POUPANCA */
        try {
            RSAdo = Select_Table(
                    "SELECT ISNULL(SUM(CAST(REPLACE(REPLACE(a.vl_Limite, '.', ''), ',', '.') AS DECIMAL(10,2))),0) AS calculoAPagar FROM tbd_DespesaMensal a INNER JOIN tbd_Emprestimos b ON a.id_Emprestimo = b.id_Emprestimo WHERE b.tp_EmprestimoAReceber = 'S' and b.tp_ContabilizarPoupanca = 'S'");
            while (RSAdo.next()) {
                result = RSAdo.getDouble("calculoAPagar");
            }
            RSAdo.close();

            /*
             * CALCULA OS VALORES PAGOS DOS EMPRESTIMOS CONTABILIZADOS NA POUPANÇA E SUBTRAI
             * DO SUBTOTAL
             */
            RSAdo = Select_Table(
                    "SELECT ISNULL(SUM(CAST(REPLACE(REPLACE(a.vl_Pago, '.', ''), ',', '.') AS DECIMAL(10,2))),0) AS calculoPago FROM tbd_PagamentoEmprestimo a inner join tbd_Emprestimos b on b.id_Emprestimo = a.id_Emprestimo WHERE b.tp_EmprestimoAReceber = 'S' and b.tp_ContabilizarPoupanca = 'S' and a.tp_Status = 'PAGO' and a.id_Funcionario = '2'");
            while (RSAdo.next()) {
                result -= RSAdo.getDouble("calculoPago");
            }
            RSAdo.close();
        } catch (ExcecaoGlobal e) {
            LogErro("Erro ao realizar o calculo " + e);
            return 0d;
        } catch (SQLException e) {
            LogErro("Erro ao realizar o calculo " + e);
            return 0d;
        }

        return result;
    }

    public Double getValorOutrosEmprestimosAReceber() {
        result = 0d;

        try {
            RSAdo = Select_Table(
                    "SELECT ISNULL(SUM(CAST(REPLACE(REPLACE(a.vl_Limite, '.', ''), ',', '.') AS DECIMAL(10,2))),0) AS calculoOutrosEmprestimos FROM tbd_DespesaMensal a INNER JOIN tbd_Emprestimos b ON a.id_Emprestimo = b.id_Emprestimo WHERE b.tp_EmprestimoAReceber = 'S' and b.tp_ContabilizarPoupanca = 'N' and a.tp_Anotacao = 'N'");
            while (RSAdo.next()) {
                result = RSAdo.getDouble("calculoOutrosEmprestimos");
            }
            RSAdo.close();

            /* CALCULA O VALOR DOS EMPRESTIMOS PAGOS E SUBTRAI DO TOTAL */
            RSAdo = Select_Table(
                    "SELECT ISNULL(SUM(CAST(REPLACE(REPLACE(a.vl_Pago, '.', ''), ',', '.') AS DECIMAL(10,2))),0) AS calculoPago FROM tbd_PagamentoEmprestimo a inner join tbd_Emprestimos b on b.id_Emprestimo = a.id_Emprestimo WHERE b.tp_EmprestimoAReceber = 'S' and b.tp_ContabilizarPoupanca = 'N' and a.tp_Status = 'PAGO' and a.id_Funcionario = '2'");
            while (RSAdo.next()) {
                result -= RSAdo.getDouble("calculoPago");
            }
            RSAdo.close();

        } catch (ExcecaoGlobal e) {
            LogErro("Erro ao realizar o calculo " + e);
            return 0d;
        } catch (SQLException e) {
            LogErro("Erro ao realizar o calculo " + e);
            return 0d;
        }

        return result;
    }

    public Double getValorEmprestimosAPagar(Integer ano) {
        result = 0d;

        try {
            RSAdo = Select_Table(
                    "SELECT ISNULL(SUM(CAST(REPLACE(REPLACE(a.vl_Pago, '.', ''), ',', '.') AS DECIMAL(10,2))),0) AS calculo FROM tbd_PagamentoEmprestimo a inner join tbd_Emprestimos b on a.id_Emprestimo = b.id_Emprestimo WHERE a.tp_Status = 'PEND' and b.tp_EmprestimoAReceber = 'N' and a.id_Funcionario = '2' and SUBSTRING(a.ds_DataPagamento,7,5) = '"
                            + Integer.toString(ano) + "'");
            while (RSAdo.next()) {
                result = RSAdo.getDouble("calculo");
            }
            RSAdo.close();
        } catch (ExcecaoGlobal e) {
            LogErro("Erro ao realizar o calculo " + e);
            return 0d;
        } catch (SQLException e) {
            LogErro("Erro ao realizar o calculo " + e);
            return 0d;
        }

        return result;
    }
}
