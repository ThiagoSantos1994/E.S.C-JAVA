package br.esc.software.global;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.BaseEncoding;

/**Classe global da aplicação, nela deve ser declarada variaveis, metodos, regras de negocio global do sistema
 * 
 * @author Thiago Santos
 * @version: 19.6.4
 * @since: 06/2019
 */
/*
 * Classe global do sistema, nela contem todos os métodos comuns do sistema
 */
public class Global {
	/*
	 * Variaveis globais\ENUM\Referencias
	 */
	public final String VARCHAR = "VARCHAR";
	public final String CHAR = "CHAR";
	public final String DATE = "DATE";
	public final String EncodingDefault = "UTF-8";
	private final String dirINI = "C:\\WINDOWS\\INI_JAVA.ini";
	static final Logger logger = LoggerFactory.getLogger(Global.class);
	
	public String lerArquivoINI(String sessao, String palavraChave) {
		try {
			Ini arquivoINI = new Ini(new File(dirINI));
			return arquivoINI.get(sessao, palavraChave);
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo não localizado no diretório definido");
			e.printStackTrace();
		} catch (InvalidFileFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getDataAtual() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    Date dataAtual = new Date(System.currentTimeMillis());
	    return simpleDateFormat.format(dataAtual);
	}
	
	public static String getMesAtual() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM");
	    Date dataAtual = new Date(System.currentTimeMillis());
	    return simpleDateFormat.format(dataAtual);
	}
	
	public static String getAnoAtual() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY");
	    Date dataAtual = new Date(System.currentTimeMillis());
	    return simpleDateFormat.format(dataAtual);
	}
	
	public String descriptografar(String sTextoCodificado) {
		try {
			byte[] contentInBytes = BaseEncoding.base64().decode(sTextoCodificado);
			return new String(contentInBytes, EncodingDefault);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String criptografar(String sTexto) {
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
	
	public boolean escreverArquivoTexto(StringBuffer texto, String sFile) {
		try {
			FileWriter fw  = new FileWriter(sFile, true);
			fw.write(texto.toString());
			fw.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean excluirArquivoTexto(String sFile) {
		logger.debug("Excluindo arquivo -> " + sFile);
		File file = new File(sFile);
		file.delete();
		return true;
	}
}
