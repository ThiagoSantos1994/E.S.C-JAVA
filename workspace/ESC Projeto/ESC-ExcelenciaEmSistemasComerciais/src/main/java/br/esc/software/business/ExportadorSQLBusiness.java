package br.esc.software.business;

import static br.esc.software.commons.Global.ExcluirArquivoTexto;
import static br.esc.software.commons.Global.LogInfo;

import java.sql.SQLException;

import org.springframework.stereotype.Component;

import br.esc.software.commons.DataUtils;
import br.esc.software.commons.Global;
import br.esc.software.commons.ObjectUtils;
import br.esc.software.domain.TabelasSQL;
import br.esc.software.exceptions.ExcecaoGlobal;
import br.esc.software.integration.ExportadorSQL;
import br.esc.software.persistence.ExportadorDao;

@Component
public class ExportadorSQLBusiness {

	ExportadorSQL exportador = new ExportadorSQL();
	DataUtils utils = new DataUtils();
	Global global = new Global();
	ExportadorDao dao = new ExportadorDao();
	
	private String sDirArquivo = "";
	public String iniciarExportacao() throws ExcecaoGlobal {

		try {
			sDirArquivo = this.nomeArquivo();

			ExcluirArquivoTexto(sDirArquivo);

			this.montaCabecalho();

			for (TabelasSQL tabelas : dao.getListaTabelas()) {
				String tabelaSQL = tabelas.getNomeTabela();
				String colunasSQL = dao.montaColunaTabela(tabelaSQL);
				
				LogInfo("Exportando dados tabela: " + tabelaSQL);
				exportador.gerarArquivoExportacao(tabelaSQL, colunasSQL, sDirArquivo);
			}
			
			String strConcluido = "Processamento concluido! Arquivo disponibilizado em: " + sDirArquivo;
			LogInfo(strConcluido);
			
			return strConcluido;
		} catch (Exception ex) {
			String strErro = ("Ocorreu um erro inesperado na classe ExportadorSQL, processamento interrompido -> " + ex);
			throw new ExcecaoGlobal(strErro, ex);
		}
	}

	private String nomeArquivo() throws SQLException, ExcecaoGlobal {
		String nomeArquivo = dao.getDiretorioDestinoArquivo() + "BACKUP_";
		nomeArquivo = nomeArquivo.concat(utils.MesNomeAtual() + "-" + utils.AnoAtual() + ".SQL");
		return nomeArquivo.toUpperCase();
	}

	private void montaCabecalho() throws Exception {
		StringBuffer buffer = new StringBuffer();
		buffer.append("--[EXPORTADOR DE CONFIGURAÇÕES E REGISTROS - PROCESSADO EM " + utils.DataAtual() + " - JAVA" + ObjectUtils.pularLinha());
		buffer.append(exportador.montaScriptImplantacao());
		global.EscreverArquivoTexto(buffer, sDirArquivo);
	}
	
}
