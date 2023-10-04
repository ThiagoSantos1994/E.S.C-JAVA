package com.br.unip.dados.uteis.apis.domain.cadastro;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CadastroPessoaResponse {

    private Integer codigo;
    private String nome;
    private String sobreNome;
    private String dataNascimento;
    private String naturalidade;
    private String nomeMae;
    private String estadoCivil;
    private String nomeConjuge;
    private String escolaridade;
    private String profissao;
    private Long rg;
    private Long cpf;
    private String cep;

    /*Dados obtidos da api publica de consulta de CEP*/
    private Endereco dadosEndereco;

    private String numeroTelefone;
    private String numeroCelular;
    private String email;
    private String observacoes;

    /*Dados obtidos da api publica de previsao do tempo*/
    private String previsaoTempo;

    /*Dados obtidos da api publica de cotação do dolar*/
    private CotacaoMoeda cotacaoDolar;

}
