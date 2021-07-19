package com.br.unip.dados.uteis.apis.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tb_pessoa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PessoaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Integer codigo;

    @Column(name = "nome")
    private String nome;

    @Column(name = "sobreNome")
    private String sobreNome;

    @Column(name = "dataNascimento")
    private String dataNascimento;

    @Column(name = "naturalidade")
    private String naturalidade;

    @Column(name = "estadoCivil")
    private String estadoCivil;

    @Column(name = "nomeConjuge")
    private String nomeConjuge;

    @Column(name = "nomeMae")
    private String nomeMae;

    @Column(name = "escolaridade")
    private String escolaridade;

    @Column(name = "profissao")
    private String profissao;

    @Column(name = "rg")
    private Long rg;

    @Column(name = "cpf")
    private Long cpf;

    @Column(name = "cep")
    private String cep;

    @Column(name = "numeroTelefone")
    private String numeroTelefone;

    @Column(name = "numeroCelular")
    private String numeroCelular;

    @Column(name = "email")
    private String email;

    @Column(name = "observacoes")
    private String observacoes;

    @Column(name = "logradouro")
    private String logradouro;

    @Column(name = "complemento")
    private String complemento;

    @Column(name = "bairro")
    private String bairro;

    @Column(name = "localidade")
    private String localidade;

    @Column(name = "uf")
    private String uf;

    @Column(name = "dddRegiao")
    private String dddRegiao;

    @Column(name = "previsaoTempo")
    private String previsaoTempo;

    @Column(name = "moeda")
    private String moeda;

    @Column(name = "valorCompra")
    private float valorCompra;

    @Column(name = "valorVenda")
    private float valorVenda;

    @Column(name = "variacao")
    private float variacao;

}
