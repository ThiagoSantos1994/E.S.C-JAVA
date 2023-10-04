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
                    "SELECT ISNULL(SUM(CAST(REPLACE(REPLACE(REPLACE(b.vl_Total, '.', ''), ',', '.'), '- ', '-') AS DECIMAL(10,2))),0) AS calculo " +
                            "FROM tbd_DespesaMensal a INNER JOIN tbd_DetalheDespesasMensais b ON b.id_Despesa = a.id_Despesa AND b.id_Funcionario = a.id_Funcionario AND b.id_DetalheDespesa = a.id_DetalheDespesa AND b.tp_Anotacao = 'N'  " +
                            "WHERE a.id_Despesa IN (SELECT id_Despesa FROM tbd_DespesasFixasMensais WHERE ds_Ano = " + ano + " AND id_Funcionario = a.id_Funcionario) " +
                            "AND a.id_Funcionario = 2 AND a.tp_Relatorio = 'N' AND a.tp_Emprestimo = 'N' AND a.tp_Poupanca = 'N' AND a.tp_PoupancaNegativa = 'N' AND a.tp_Anotacao = 'N'");
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
        Double resultPositivo = 0d;
        Double resultNegativo = 0d;

        try {
            RSAdo = Select_Table(
                    "SELECT ISNULL(SUM(CAST(REPLACE(REPLACE(vl_Total, '.', ''), ',', '.') AS DECIMAL(10,2))),0) AS calculo FROM tbd_DespesasFixasMensais where ds_Ano = "
                            + ano + " and tp_Status = '+' and id_Funcionario = 2");
            while (RSAdo.next()) {
                resultPositivo = RSAdo.getDouble("calculo");
            }
            RSAdo.close();

            RSAdo = Select_Table(
                    "SELECT ISNULL(SUM(CAST(REPLACE(REPLACE(vl_Total, '.', ''), ',', '.') AS DECIMAL(10,2))),0) AS calculo FROM tbd_DespesasFixasMensais where ds_Ano = "
                            + ano + " and tp_Status = '-' and id_Funcionario = 2");
            while (RSAdo.next()) {
                resultNegativo = RSAdo.getDouble("calculo");
            }
            RSAdo.close();

            result = (resultPositivo - resultNegativo);
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
            /* Recupera o valor dos emprestimos a pagar independente se estiver em aberto ou quitado */
            RSAdo = Select_Table(
                    "SELECT ISNULL(SUM(CAST(REPLACE(REPLACE(REPLACE(vl_Pago, '.', ''), ',', '.'), '-', '') AS DECIMAL(10,2))),0) AS calculo FROM tbd_PagamentoEmprestimo a INNER JOIN tbd_Emprestimos b on a.id_Emprestimo = b.id_Emprestimo WHERE a.ds_AnoPagamento = " + ano + " and b.tp_EmprestimoAReceber = 'N' and b.id_Funcionario = 2 and b.tp_ContabilizarPoupanca = 'N'");
            while (RSAdo.next()) {
                result = result + RSAdo.getDouble("calculo");
            }
            RSAdo.close();

            /* Recupera o valor das despesas marcadas como emprestimos a pagar e\ou marcadas como relatorio */
            RSAdo = Select_Table(
                    "SELECT SUM(vl_Total) AS calculo FROM (\n" +
                            "SELECT DISTINCT(ISNULL(CAST(REPLACE(REPLACE(REPLACE(a.vl_Total, '.', ''), ',', '.'), '-', '') AS DECIMAL(10,2)),0)) AS vl_Total , a.id_Despesa, c.ds_Ano\n" +
                            "FROM tbd_DetalheDespesasMensais a \n" +
                            "INNER JOIN tbd_DespesaMensal b on a.id_Despesa = b.id_Despesa and a.id_DetalheDespesa = b.id_DetalheDespesa \n" +
                            "INNER JOIN tbd_DespesasFixasMensais c on b.id_Despesa = c.id_Despesa \n" +
                            "WHERE b.tp_EmprestimoAPagar = 'S' and a.id_Funcionario = 2 or (a.id_DespesaLinkRelatorio IN \n" +
                            "(SELECT DISTINCT (id_DetalheDespesa) FROM tbd_DespesaMensal WHERE id_Despesa = a.id_Despesa AND tp_Relatorio = 'S' AND tp_EmprestimoAPagar = 'S' and id_Funcionario = 2))\n" +
                            ") as calculo WHERE ds_Ano = " + ano + "\n");
            while (RSAdo.next()) {
                result = result + RSAdo.getDouble("calculo");
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

    public Double getValorEmprestimosAPagar_PAGO(Integer ano) {
        result = 0d;

        try {
            /* Recupera o valor dos emprestimos a pagar independente se estiver em aberto ou quitado */
            RSAdo = Select_Table(
                    "SELECT ISNULL(SUM(CAST(REPLACE(REPLACE(REPLACE(vl_Pago, '.', ''), ',', '.'), '-', '') AS DECIMAL(10,2))),0) AS calculo FROM tbd_PagamentoEmprestimo a INNER JOIN tbd_Emprestimos b on a.id_Emprestimo = b.id_Emprestimo WHERE a.ds_AnoPagamento = " + ano + " and b.tp_EmprestimoAReceber = 'N' and b.id_Funcionario = 2 and b.tp_ContabilizarPoupanca = 'N' and a.tp_Status = 'PAGO'");
            while (RSAdo.next()) {
                result = result + RSAdo.getDouble("calculo");
            }
            RSAdo.close();

            /* Recupera o valor das despesas marcadas como emprestimos a pagar e\ou marcadas como relatorio que ja foram pagas*/
            RSAdo = Select_Table(
                    "SELECT SUM(vl_Total) AS calculo FROM (\n" +
                            "SELECT DISTINCT(ISNULL(CAST(REPLACE(REPLACE(REPLACE(a.vl_TotalPago, '.', ''), ',', '.'), '-', '') AS DECIMAL(10,2)),0)) AS vl_Total , a.id_Despesa, c.ds_Ano\n" +
                            "FROM tbd_DetalheDespesasMensais a \n" +
                            "INNER JOIN tbd_DespesaMensal b on a.id_Despesa = b.id_Despesa and a.id_DetalheDespesa = b.id_DetalheDespesa \n" +
                            "INNER JOIN tbd_DespesasFixasMensais c on b.id_Despesa = c.id_Despesa \n" +
                            "WHERE b.tp_EmprestimoAPagar = 'S' and a.id_Funcionario = 2 or (a.id_DespesaLinkRelatorio IN \n" +
                            "(SELECT DISTINCT (id_DetalheDespesa) FROM tbd_DespesaMensal WHERE id_Despesa = a.id_Despesa AND tp_Relatorio = 'S' AND tp_EmprestimoAPagar = 'S' and id_Funcionario = 2))\n" +
                            ") as calculo WHERE ds_Ano = " + ano + "\n");
            while (RSAdo.next()) {
                result = result + RSAdo.getDouble("calculo");
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
