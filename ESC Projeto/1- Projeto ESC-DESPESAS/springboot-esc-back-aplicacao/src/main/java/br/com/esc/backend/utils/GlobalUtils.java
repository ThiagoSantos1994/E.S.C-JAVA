package br.com.esc.backend.utils;

import com.google.common.io.BaseEncoding;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.ini4j.Ini;

import java.io.*;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Properties;

/**
 * Classe global da aplicação, nela deve ser declarada variaveis, metodos,
 * regras de negocio global do sistema
 *
 * @author Thiago Santos
 * @version: 19.6.4
 * @since: 06/2019
 */

@Slf4j
public class GlobalUtils {
    /*
     * Variaveis globais\ENUM\Referencias
     */
    public final static String VARCHAR = "VARCHAR";
    public final static String CHAR = "CHAR";
    public final static String DATE = "DATE";
    public final static String EncodingDefault = "UTF-8";
    private final static String dirINI = "C:\\WINDOWS\\INI_JAVA.ini";

    // Padrões de gravação de status processamento
    public final static String EXECUTANDO = "Em Execução";
    public final static String PROCESSANDO = "Processamento em andamento..";
    public final static String CONCLUIDO = "Processamento Concluido - OK";
    public final static String CONCLUIDO_FALHA = "Processamento Concluido (Com Falha) - NOK";
    public final static String ERRO = "Processamento ERRO - NOK";
    public static int RETORNO_OK = 1;
    public static int RETORNO_NOK = -1;

    public static String LerArquivoINI(String sessao, String palavraChave) {
        try {
            Ini arquivoINI = new Ini(new File(dirINI));
            return arquivoINI.get(sessao, palavraChave);
        } catch (Exception e) {
            log.error("Ocorreu um erro inesperado ao ler o arquivo INI >>> " + e);
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

    public boolean EscreverArquivoTexto(StringBuffer texto, String sFile) throws Exception {
        try {
            FileWriterWithEncoding fw = new FileWriterWithEncoding(sFile, EncodingDefault, true);
            fw.write(texto.toString());
            fw.close();
            return true;
        } catch (Exception e) {
            log.error("Ocorreu um erro durante a escrita do arquivo texto: " + sFile + "Exceção: " + e);
            throw new Exception(e);
        }
    }

    public static void ExcluirArquivoTexto(String sFile) throws Exception {
        try {
            File file = new File(sFile);
            file.delete();
            log.debug("Arquivo -> " + sFile + " Excluido com sucesso...");
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static String getMesAtual() {
        Calendar cal = Calendar.getInstance();
        var mes = cal.get(Calendar.MONTH) + 1;
        return String.valueOf(mes);
    }

    public static String getAnoAtual() {
        Calendar cal = Calendar.getInstance();
        var mes = cal.get(Calendar.YEAR);
        return String.valueOf(mes);
    }
}
