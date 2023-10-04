package com.br.unip.dados.uteis.apis.domain.externas.cotacaodolar;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Currencies {

    private String name;
    private float buy;
    private float sell;
    private float variation;
}
