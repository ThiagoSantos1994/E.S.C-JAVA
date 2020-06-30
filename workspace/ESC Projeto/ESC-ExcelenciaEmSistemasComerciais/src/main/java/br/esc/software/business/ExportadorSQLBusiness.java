package br.esc.software.business;

import static br.esc.software.commons.DataUtils.AnoAtual;
import static br.esc.software.commons.DataUtils.DataAtual;
import static br.esc.software.commons.DataUtils.MesNomeAtual;
import static br.esc.software.commons.Global.EscreverArquivoTexto;
import static br.esc.software.commons.Global.ExcluirArquivoTexto;
import static br.esc.software.commons.Global.LogInfo;

import java.sql.SQLException;

import org.springframework.stereotype.Component;

import br.esc.software.commons.ObjectUtils;
import br.esc.software.domain.TabelasSQL;
import br.esc.software.exceptions.ExcecaoGlobal;
import br.esc.software.integration.ExportadorSQL;
import br.esc.software.persistence.ExportadorDao;

@Component
public class ExportadorSQLBusiness {

	ExportadorSQL exportador = new ExportadorSQL();

	ExportadorDao dao = new ExportadorDao();

	public String iniciarExportacao() throws ExcecaoGlobal {

		try {
			String sDirArquivo = this.nomeArquivo();

			ExcluirArquivoTexto(sDirArquivo);

			this.montaCabecalho();

			for (TabelasSQL tabelas : dao.getListaTabelas()) {
				String tabelaSQL = tabelas.getNomeTabela();
				String colunasSQL = dao.montaColunaTabela(tabelaSQL);
				
				LogInfo("Exportando dados tabela: " + tabelaSQL);
				exportador.gerarArquivoExportacao(tabelaSQL, colunasSQL, sDirArquivo);
			}
			
			return "Processamento concluido! Arquivo disponibilizado em: " + sDirArquivo;
		} catch (Exception ex) {
			String strErro = ("Ocorreu um erro inesperado na classe ExportadorSQL, processamento interrompido -> " + ex);
			throw new ExcecaoGlobal(strErro, ex);
		}
	}

	private String nomeArquivo() throws SQLException {
		String nomeArquivo = dao.getDiretorioDestinoArquivo() + "BACKUP_";
		nomeArquivo = nomeArquivo.concat(MesNomeAtual() + "-" + AnoAtual() + ".SQL");
		return nomeArquivo.toUpperCase();
	}

	private void montaCabecalho() throws Exception {
		StringBuffer buffer = new StringBuffer();
		buffer.append("--[EXPORTADOR DE CONFIGURAÇÕES E REGISTROS - PROCESSADO EM " + DataAtual() + " - JAVA"
				+ ObjectUtils.pularLinha());
		buffer.append(exportador.montaScriptImplantacao());
		EscreverArquivoTexto(buffer, this.nomeArquivo());
	}
}
