package br.com.esc.backend.utils;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtils {

    public String DataAtual() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date data = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(data);
    }

    public String MesAtual() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM");
        Date data = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(data);
    }

    public String AnoAtual() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY");
        Date ano = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(ano);
    }

    public String AnoAnterior() {
        int ano = (Integer.parseInt(AnoAtual()) - 1);
        return String.valueOf(ano);
    }

    public String MesNome(int mes) {
        String[] months = new DateFormatSymbols().getMonths();
        if (mes < 13) {
            int arrayMes = (mes - 1);
            return months[arrayMes];
        } else {
            return "MES INVALIDO";
        }
    }

    public String MesNomeAtual() {
        int mes = (Integer.parseInt(MesAtual()) - 1);
        String[] months = new DateFormatSymbols().getMonths();
        return months[mes];
    }
}
