package br.esc.software.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.ini4j.Ini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.BaseEncoding;

/**
 * Classe global da aplicação, nela deve ser declarada variaveis, metodos,
 * regras de negocio global do sistema
 * 
 * @author Thiago Santos
 * @version: 19.6.4
 * @since: 06/2019
 */

public class Global {
	/*
	 * Variaveis globais\ENUM\Referencias
	 */
	public final static String VARCHAR = "VARCHAR";
	public final static String CHAR = "CHAR";
	public final static String DATE = "DATE";
	public final static String EncodingDefault = "UTF-8";
	private final static String dirINI = "C:\\WINDOWS\\INI_JAVA.ini";
	static final Logger logger = LoggerFactory.getLogger(Global.class);

	// Padrões de gravação de status processamento (13/09/2019)
	public final static String EXECUTANDO = "Em Execução";
	public final static String PROCESSANDO = "Processamento em andamento..";
	public final static String CONCLUIDO = "Processamento Concluido - OK";
	public final static String CONCLUIDO_FALHA = "Processamento Concluido (Com Falha) - NOK";
	public final static String ERRO = "Processamento ERRO - NOK";
	public static int RETORNO_OK = 1;
	public static int RETORNO_NOK = -1;

	private static final Logger log = LoggerFactory.getLogger(Global.class);

	public static void LogDebug(String sMsgLog) {
		log.debug(sMsgLog);
	}

	public static void LogInfo(String sMsgInfo) {
		log.info(sMsgInfo);
	}

	public static void LogErro(String sMsgErro) {
		log.error(sMsgErro);
	}

	public static void LogErro(String sMsgErro, Exception ex) {
		log.error(sMsgErro + " " + ex);
	}

	public static String LerArquivoINI(String sessao, String palavraChave) {
		try {
			Ini arquivoINI = new Ini(new File(dirINI));
			return arquivoINI.get(sessao, palavraChave);
		} catch (Exception e) {
			LogErro("Ocorreu um erro inesperado ao ler o arquivo INI >>> " + e);
		}
		return null;
	}

	public static String Descriptografar(String sTextoCodificado) {
		try {
			byte[] contentInBytes = BaseEncoding.base64().decode(sTextoCodificado);
			return new String(contentInBytes, EncodingDefault);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String Criptografar(String sTexto) {
		try {
			String base64String = BaseEncoding.base64().encode(sTexto.getBytes(EncodingDefault));
			return base64String;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Properties getProperties() {
		try {
			Properties props = new Properties();
			FileInputStream file = new FileInputStream("./src/main/resources/application.properties");
			props.load(file);
			return props;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean EscreverArquivoTexto(StringBuffer texto, String sFile) throws Exception {
		try {
			FileWriterWithEncoding fw = new FileWriterWithEncoding(sFile, EncodingDefault, true);
			fw.write(texto.toString());
			fw.close();
			return true;
		} catch (Exception e) {
			LogErro("Ocorreu um erro durante a escrita do arquivo texto: " + sFile + "Exceção: " + e);
			throw new Exception(e);
		}
	}

	public static void ExcluirArquivoTexto(String sFile) {
		try {
			logger.debug("Excluindo arquivo -> " + sFile);
			File file = new File(sFile);
			file.delete();
		} catch (Exception e) {
			LogErro("Erro ao excluir o arquivo do diretorio " + sFile + ". --> " + e);
		}
	}
}
