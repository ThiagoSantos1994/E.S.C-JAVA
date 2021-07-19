package com.br.unip.dados.uteis.apis.domain.cadastro;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Endereco {
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;
    private String dddRegiao;
}
