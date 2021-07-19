package com.br.unip.dados.uteis.apis.domain.cadastro;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CotacaoMoeda {
    private String moeda;
    private float valorCompra;
    private float valorVenda;
    private float variacao;
}
