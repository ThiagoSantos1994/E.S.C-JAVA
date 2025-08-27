package br.com.esc.backend.utils;

import lombok.var;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static br.com.esc.backend.utils.ObjectUtils.isEmpty;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

public class DataUtils {

    public static String dataHoraAtual() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date data = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(data);
    }

    public static String formatarDataBR(String ref) {
        if (isEmpty(ref)) {
            return "";
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date data = new Date(ref);
        return simpleDateFormat.format(data);
    }

    public static String formatarDataEUA(String ref) {
        if (isEmpty(ref)) {
            return "";
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date data = new Date(ref);
        return simpleDateFormat.format(data);
    }

    public static String formatarDataEUA(Date ref) {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        return df.format(ref);
    }

    public static String dataAtual() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date data = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(data);
    }

    public static String mesAnoAtual() {
        return mesAtual().concat("/").concat(anoAtual());
    }

    public static String diaAtual() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
        Date data = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(data);
    }

    public static String mesAtual() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM");
        Date data = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(data);
    }

    public static String anoAtual() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY");
        Date ano = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(ano);
    }

    public static String anoSeguinte() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY");
        Date ano = new Date(System.currentTimeMillis());
        return valueOf(parseInt(simpleDateFormat.format(ano)) + 1);
    }

    public static Date convertStringToDate(String sData) throws ParseException {
        return new SimpleDateFormat("dd/MM/yyyy").parse(sData);
    }

    public static String retornaMesAnterior(String dsMes) {
        var dsMesAnterior = (parseInt(dsMes) - 1 < 1 ? 12 : parseInt(dsMes) - 1);
        var result = (dsMesAnterior <= 9 ? "0" + dsMesAnterior : valueOf(dsMesAnterior));
        return result;
    }

    public static String parserMesToString(Integer dsMes) {
        return (dsMes <= 9 ? "0" + dsMes : valueOf(dsMes));
    }

    public static Date retornaDataPersonalizada(String data, Integer qtdeMes) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(convertStringToDate(data));
        cal.add(Calendar.MONTH, qtdeMes);
        return cal.getTime();
    }

    public static String retornaDataPersonalizadaEmDias(String data, Integer qtdeDias) throws ParseException {
        Date a = new Date(data);
        a.setDate(a.getDate() + qtdeDias);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return simpleDateFormat.format(a);
    }

    public static String convertDateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static String convertDateToString_MMYYYY(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yyyy");
        return simpleDateFormat.format(date);
    }

    public static Days diferencaEmDias(String referencia) throws ParseException {
        DateTime dataAtual = new DateTime();
        var dia = parseInt(referencia.substring(8, 10));
        var mes = parseInt(referencia.substring(5, 7));
        var ano = parseInt(referencia.substring(0, 4));

        DateTime dataReferencia = new DateTime(ano, mes, dia, 0, 0);

        return Days.daysBetween(dataAtual, dataReferencia);
    }
}
