package br.esc.software.configuration;

import br.esc.software.commons.exceptions.ExcecaoGlobal;
import org.springframework.stereotype.Component;

import java.sql.*;

import static br.esc.software.commons.utils.GlobalUtils.*;

@Component
public class ConnectionSQL {
    private static Connection connection;
    private static Statement stmt;

    public ConnectionSQL() throws ExcecaoGlobal {
        validarConexaoDAO();
    }

    private static void conectarBaseSQL() throws ExcecaoGlobal {
        LogInfo("Conectando na base de dados SQL...");

        try {
            connection = DriverManager.getConnection(getUriConexao());
            stmt = connection.createStatement();
            LogInfo("<<OK>> Conexao realizada com sucesso!");
        } catch (SQLException e) {
            throw new ExcecaoGlobal("Erro ao conectar no banco de dados -> ", e);
        }
    }

    private static String getUriConexao() {
        /*
         * Obtem os dados de conexao DAO atravez dos parametros configurados no arquivo
         * application.properties
         */
        String servidor = getProperties().getProperty("prop.server");
        String banco = getProperties().getProperty("prop.database");
        String usuario = getProperties().getProperty("prop.user");
        String senha = getProperties().getProperty("prop.password");

        return "jdbc:sqlserver://" + servidor + ";databaseName=" + banco + ";user=" + usuario + ";password=" + senha;
    }

    @Deprecated
    public void abrirConexao() throws ExcecaoGlobal {
        conectarBaseSQL();
    }

    @Deprecated
    public void fecharConexao() throws ExcecaoGlobal {
        try {
            connection.close();
            stmt.close();
        } catch (SQLException e) {
            throw new ExcecaoGlobal("Ocorreu uma falha ao fechar a conexao DAO", e);
        }
    }

    public static ResultSet Select_Table(String sSelectTable) throws ExcecaoGlobal {
        ResultSet rs;
        try {
            validarConexaoDAO();
            rs = stmt.executeQuery(sSelectTable);
            return rs;
        } catch (SQLException e) {
            LogErro("Erro ao executar o metodo SELECT_TABLE:", e);
        }
        return null;
    }

    public static void Insert_Table(String sInsertTable) throws SQLException {
        ExecuteInstrucoesSQL(sInsertTable);
    }

    public static void Update_Table(String sUpdateTable) throws SQLException {
        ExecuteInstrucoesSQL(sUpdateTable);
    }

    public static void Delete_Table(String sDeleteTable) throws SQLException {
        ExecuteInstrucoesSQL(sDeleteTable);
    }

    private static void ExecuteInstrucoesSQL(String sQueryOperacao) throws SQLException {
        try {
            validarConexaoDAO();
            stmt.executeUpdate(sQueryOperacao);
        } catch (Exception e) {
            throw new SQLException("Erro ao executar metodo ExecuteInstrucoesSQL", e);
        }

    }

    private static void validarConexaoDAO() throws ExcecaoGlobal {
        try {
            if (null == stmt || stmt.isClosed()) {
                conectarBaseSQL();
            }
        } catch (Exception ex) {
            throw new ExcecaoGlobal("Ocorreu um erro ao conectar na base de dados SQL >>>> ", ex.getCause());
        }
    }
}
