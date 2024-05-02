package br.com.esc.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LembretesDAO {
    private Integer idLembrete;
    private Integer idFuncionario;
    private Integer numeroDias;
    private String dsTituloLembrete;
    private String tpHabilitaNotificacaoDiaria;
    private String tpSegunda;
    private String tpTerca;
    private String tpQuarta;
    private String tpQuinta;
    private String tpSexta;
    private String tpSabado;
    private String tpDomingo;
    private String tpBaixado;
    private String tpContagemRegressiva;
    private String tpLembreteDatado;
    private String tpRenovarAuto;
    private String dataInicial;
    private String dsObservacoes;
    private String data1;
    private String data2;
    private String data3;
    private String data4;
    private String data5;
}
