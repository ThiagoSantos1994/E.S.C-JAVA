package com.br.unip.dados.uteis.apis.domain.cadastro;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class CadastroPessoaRequest {

    private Integer codigo;
    private String nome;
    private String sobreNome;
    private String dataNascimento;
    private String naturalidade;
    private String estadoCivil;
    private String nomeConjuge;
    private String nomeMae;
    private String escolaridade;
    private String profissao;
    private Long rg;
    private Long cpf;
    private String cep;
    private String numeroTelefone;
    private String numeroCelular;
    private String email;
    private String observacoes;

}
