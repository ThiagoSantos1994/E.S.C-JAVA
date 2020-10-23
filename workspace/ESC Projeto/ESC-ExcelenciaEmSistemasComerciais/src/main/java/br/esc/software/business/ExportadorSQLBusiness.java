package br.esc.software.business;

import br.esc.software.commons.utils.ObjectParser;
import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.domain.Response;
import br.esc.software.domain.exportador.TabelasSQL;
import br.esc.software.integration.ExportadorSQLImpl;
import br.esc.software.repository.exportador.ExportadorDao;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

import static br.esc.software.commons.utils.GlobalUtils.ExcluirArquivoTexto;
import static br.esc.software.commons.utils.GlobalUtils.LogInfo;

@Component
public class ExportadorSQLBusiness {

    ExportadorDao dao = new ExportadorDao();
    ExportadorSQLImpl exportador = new ExportadorSQLImpl();
    ObjectParser parser = new ObjectParser();

    String sDirArquivo = "";

    public String iniciarExportacao() throws ExcecaoGlobal {
        Response response = new Response();

        try {
            sDirArquivo = this.getNomeArquivo();

            ExcluirArquivoTexto(sDirArquivo);

            exportador.montaCabecalho();

            for (TabelasSQL tabelas : dao.getListaTabelas()) {
                String tabelaSQL = tabelas.getNomeTabela();
                String colunasSQL = dao.montaColunaTabela(tabelaSQL);

                LogInfo("Exportando dados tabela: " + tabelaSQL);
                exportador.gerarArquivoExportacao(tabelaSQL, colunasSQL, sDirArquivo);
            }

            response.setResponse("Processamento concluido! Arquivo disponibilizado em: " + sDirArquivo);
            LogInfo(response.getResponse());

            return parser.parser(response);
        } catch (Exception ex) {
            String strErro = ("Ocorreu um erro inesperado na classe ExportadorSQL, processamento interrompido -> " + ex);
            throw new ExcecaoGlobal(strErro, ex);
        }
    }

    private String getNomeArquivo() throws SQLException, ExcecaoGlobal {
        LogInfo("Obtendo diretorio e nome do arquivo...");

        String diretorio = exportador.gerarNomeArquivo();

        LogInfo("Diretorio: " + diretorio);

        return diretorio;
    }
}
