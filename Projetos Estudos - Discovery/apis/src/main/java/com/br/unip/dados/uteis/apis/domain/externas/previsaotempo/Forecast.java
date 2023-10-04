package com.br.unip.dados.uteis.apis.domain.externas.previsaotempo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Forecast {

    private String date;
    private String weekday;
    private float max;
    private float min;
    private String description;
    private String condition;

}
