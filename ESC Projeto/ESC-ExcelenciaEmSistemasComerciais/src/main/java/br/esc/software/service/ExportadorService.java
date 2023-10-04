package br.esc.software.service;

import br.esc.software.commons.utils.DataUtils;
import br.esc.software.commons.utils.GlobalUtils;
import br.esc.software.commons.utils.ObjectUtils;
import br.esc.software.domain.exportador.ColunasSQL;
import br.esc.software.domain.exportador.TabelasSQL;
import br.esc.software.repository.ExportadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.ArrayList;

import static br.esc.software.commons.utils.GlobalUtils.*;

@Service
public class ExportadorService {

    @Autowired
    private ExportadorRepository dao;

    private GlobalUtils global = new GlobalUtils();
    private DataUtils utils = new DataUtils();
    private StringBuffer escreverArquivo = new StringBuffer();
    public String pathArquivo;

    /*private ExportadorRepository repo;
    public ExportadorSQLImpl(ExportadorRepository repository) {
        this.repo = repository;
    }*/

    public void gerarArquivoExportacao() throws Exception {
        pathArquivo = getDiretorioNomeArquivo();
        montaCabecalho();

        for (TabelasSQL tabelas : dao.getListaTabelas()) {
            String tabelaSQL = tabelas.getNomeTabela();
            String colunasSQL = dao.montaColunaTabela(tabelaSQL);

            try {
                LogInfo("Exportando dados tabela: " + tabelaSQL);
                montaCabecalhoTabela(tabelaSQL);
                montaScript(tabelaSQL, colunasSQL);
            } catch (Exception e) {
                throw new Exception("Erro metodo GerarArquivoExportacao ->>> " + e);
            }
        }
    }

    private void montaCabecalhoTabela(String tabela) throws Exception {
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
        LogInfo("Gerando script de implantação...");

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
            LogInfo("Script de implantação gerado com sucesso.");

            return buffer;
        } catch (Exception e) {
            throw new Exception("Erro metodo MontaScriptImplantacao ->>> " + e);
        }
    }

    private void montaCabecalho() throws Exception {
        LogInfo("Montando cabecalho arquivo exportacao...");

        StringBuffer buffer = new StringBuffer();
        buffer.append("--[EXPORTADOR DE CONFIGURAÇÕES E REGISTROS - PROCESSADO EM " + utils.DataAtual() + " - JAVA" + ObjectUtils.pularLinha());
        buffer.append(this.montaScriptImplantacao());

        global.EscreverArquivoTexto(buffer, this.pathArquivo);
        LogInfo("Cabecalho gerado com sucesso.");
    }

    private String getDiretorioNomeArquivo() throws Exception {
        String nomeArquivo = dao.getDiretorioDestinoArquivo();
        nomeArquivo = nomeArquivo.concat("BACKUP_" + utils.MesNomeAtual() + "-" + utils.AnoAtual() + ".SQL");
        return nomeArquivo.toUpperCase();
    }

}
