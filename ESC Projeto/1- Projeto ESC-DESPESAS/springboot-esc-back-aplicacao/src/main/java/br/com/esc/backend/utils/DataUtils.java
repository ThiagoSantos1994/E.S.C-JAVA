package br.com.esc.backend.utils;

import lombok.var;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Integer.parseInt;

public class DataUtils {

    public static String DataHoraAtual() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date data = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(data);
    }

    public static String formatarDataBR(String ref) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date data = new Date(ref);
        return simpleDateFormat.format(data);
    }

    public static String formatarDataBR(Calendar ref) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(ref);
    }

    public static String formatarDataEUA(String ref) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date data = new Date(ref);
        return simpleDateFormat.format(data);
    }

    public static String formatarDataEUA(Date ref) {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        return df.format(ref);
    }

    public static String DataAtual() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date data = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(data);
    }

    public static String MesAnoAtual() {
        return MesAtual().concat("/").concat(AnoAtual());
    }

    public static String DiaAtual() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
        Date data = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(data);
    }

    public static String MesAtual() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM");
        Date data = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(data);
    }

    public static String AnoAtual() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY");
        Date ano = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(ano);
    }

    public static String AnoAnterior() {
        int ano = (parseInt(AnoAtual()) - 1);
        return String.valueOf(ano);
    }

    public static String MesNome(int mes) {
        String[] months = new DateFormatSymbols().getMonths();
        if (mes < 13) {
            int arrayMes = (mes - 1);
            return months[arrayMes];
        } else {
            return "MES INVALIDO";
        }
    }

    public static String MesNomeAtual() {
        int mes = (parseInt(MesAtual()) - 1);
        String[] months = new DateFormatSymbols().getMonths();
        return months[mes];
    }

    public static Date convertStringToDate(String sData) throws ParseException {
        return new SimpleDateFormat("dd/MM/yyyy").parse(sData);
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
