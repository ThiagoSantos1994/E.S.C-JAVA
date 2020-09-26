package br.esc.software.business;

import static br.esc.software.commons.GlobalUtils.ExcluirArquivoTexto;
import static br.esc.software.commons.GlobalUtils.LogInfo;

import java.sql.SQLException;

import org.springframework.stereotype.Component;

import br.esc.software.commons.DataUtils;
import br.esc.software.commons.ExcecaoGlobal;
import br.esc.software.commons.GlobalUtils;
import br.esc.software.commons.ObjectParser;
import br.esc.software.domain.Response;
import br.esc.software.domain.TabelasSQL;
import br.esc.software.integration.ExportadorSQLImpl;
import br.esc.software.repository.ExportadorDao;

@Component
public class ExportadorSQLBusiness {

	GlobalUtils global = new GlobalUtils();
	
	ExportadorDao dao = new ExportadorDao();
	ExportadorSQLImpl exportador = new ExportadorSQLImpl();
	
	DataUtils utils = new DataUtils();
	ObjectParser parser = new ObjectParser();
	
	private String sDirArquivo = "";

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
