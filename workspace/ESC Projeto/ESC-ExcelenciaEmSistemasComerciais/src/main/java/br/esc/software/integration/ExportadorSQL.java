package br.esc.software.integration;

import static br.esc.software.commons.Global.CHAR;
import static br.esc.software.commons.Global.DATE;
import static br.esc.software.commons.Global.VARCHAR;

import java.sql.ResultSet;
import java.util.ArrayList;

import br.esc.software.commons.Global;
import br.esc.software.commons.ObjectUtils;
import br.esc.software.domain.ColunasSQL;
import br.esc.software.domain.TabelasSQL;
import br.esc.software.persistence.ExportadorDao;

public class ExportadorSQL {
	ExportadorDao dao = new ExportadorDao();
	private Global global = new Global();
	private StringBuffer escreverArquivo = new StringBuffer();
	private String pathArquivo;

	public void gerarArquivoExportacao(String sTabela, String sColunas, String sPath) throws Exception {
		
		this.pathArquivo = sPath;
		
		try {
			this.montaCabecalho(sTabela);
			this.montaScript(sTabela, sColunas);
		} catch (Exception e) {
			throw new Exception("Erro metodo GerarArquivoExportacao ->>> " + e);
		}
	}

	private void montaCabecalho(String tabela) throws Exception {
		StringBuffer montaCabecalho = new StringBuffer();
		montaCabecalho.append(ObjectUtils.pularLinha());
		montaCabecalho.append("--<Tabela: " + tabela + " >");
		montaCabecalho.append(ObjectUtils.pularLinha(2));
		montaCabecalho.append("DELETE FROM " + tabela);
		montaCabecalho.append(ObjectUtils.pularLinha());

		escreverArquivo.append(montaCabecalho);
		global.EscreverArquivoTexto(montaCabecalho, pathArquivo);
	}

	private void montaScript(String tabelaSQL, String colunasSQL) throws Exception {
		ResultSet RSAdo;
		int iloop, iColuna = 0;
		String sTempScript = "";
		StringBuffer montaScript = new StringBuffer();

		try {
			ArrayList<String> tipoColuna = new ArrayList<>();
			for (ColunasSQL colunas : dao.getListaColunas(tabelaSQL)) {
				tipoColuna.add(colunas.getTipoColuna());
			}

			RSAdo = dao.executarSelect(tabelaSQL, colunasSQL);
			while (RSAdo.next()) {
				iloop = 0;
				iColuna = 1;
				montaScript.setLength(0);
				escreverArquivo.setLength(0);

				montaScript.append("INSERT INTO " + tabelaSQL + "(" + colunasSQL + ") VALUES(");

				while (iColuna <= tipoColuna.size()) {
					if (tipoColuna.get(iloop).equals(VARCHAR) || tipoColuna.get(iloop).equals(CHAR)
							|| tipoColuna.get(iloop).equals(DATE)) {
						if (null == RSAdo.getObject(iColuna)) {
							montaScript.append("'',");
						} else {
							montaScript.append("'" + RSAdo.getObject(iColuna) + "',");
						}
					} else {
						if (null == RSAdo.getObject(iColuna)) {
							montaScript.append("0,");
						} else {
							montaScript.append(RSAdo.getObject(iColuna) + ",");
						}
					}
					iloop++;
					iColuna++;
				}

				sTempScript = "";
				sTempScript = montaScript.substring(0, (montaScript.length() - 1)) + ")";
				escreverArquivo.append(sTempScript + ObjectUtils.pularLinha());
				global.EscreverArquivoTexto(escreverArquivo, pathArquivo);
			}
			RSAdo.close();
		} catch (Exception e) {
			throw new Exception("Erro metodo MontaScript ->>> " + e);
		}
	}

	public StringBuffer montaScriptImplantacao() throws Exception {
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append(ObjectUtils.pularLinha(2));
			buffer.append("--SCRIPT DE IMPLANTAÇÃO - SQL SERVER");
			buffer.append(ObjectUtils.pularLinha() + "/*");

			for (TabelasSQL tabelas : dao.getListaTabelas()) {
				buffer.append(ObjectUtils.pularLinha());
				buffer.append("CREATE TABLE " + tabelas.getNomeTabela() + "(");

				String sScriptImplantacao = "";
				for (ColunasSQL colunasImpl : dao.getListaColunas(tabelas.getNomeTabela())) {
					sScriptImplantacao = sScriptImplantacao.concat(colunasImpl.getNomeColuna());
					if (colunasImpl.getTipoColuna().equals(VARCHAR) || colunasImpl.getTipoColuna().equals(CHAR) || colunasImpl.getTipoColuna().equals(DATE)) {
						sScriptImplantacao = sScriptImplantacao.concat(" " + colunasImpl.getTipoColuna() + "(" + colunasImpl.getTamanhoColuna() + "), ");
					} else {
						sScriptImplantacao = sScriptImplantacao.concat(" " + colunasImpl.getTipoColuna() + ", ");
					}
				}
				sScriptImplantacao = sScriptImplantacao.concat(sScriptImplantacao.substring(0, (sScriptImplantacao.length() - 2)) + ")");
				buffer.append(sScriptImplantacao);
			}
			buffer.append("*/" + ObjectUtils.pularLinha());
			return buffer;
		} catch (Exception e) {
			throw new Exception("Erro metodo MontaScriptImplantacao ->>> " + e);
		}
	}
}
