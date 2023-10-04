package com.br.unip.dados.uteis.apis.service;

import com.br.unip.dados.uteis.apis.commons.ObjectParser;
import com.br.unip.dados.uteis.apis.domain.cadastro.CadastroPessoaRequest;
import com.br.unip.dados.uteis.apis.domain.cadastro.CadastroPessoaResponse;
import com.br.unip.dados.uteis.apis.domain.cadastro.CotacaoMoeda;
import com.br.unip.dados.uteis.apis.domain.cadastro.Endereco;
import com.br.unip.dados.uteis.apis.domain.externas.consultacep.ConsultaCEP;
import com.br.unip.dados.uteis.apis.domain.externas.cotacaodolar.Cotacao;
import com.br.unip.dados.uteis.apis.domain.externas.previsaotempo.PrevisaoTempo;
import com.br.unip.dados.uteis.apis.repository.CadastroRepository;
import com.br.unip.dados.uteis.apis.repository.entity.PessoaEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CadastroService {

    @Autowired
    CadastroRepository repository;
    @Autowired
    CepService cepService;
    @Autowired
    PrevisaoTempoService tempoService;
    @Autowired
    CotacaoService cotacaoService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ObjectParser objectParser = new ObjectParser();
    private static Integer ID = 0;

    public String salvar(CadastroPessoaRequest request) {

        try {
            /*Obtem os dados do cep*/
            ConsultaCEP dadosCep = obterDadosCep(request);

            /*Obtem os dados da previsão do tempo*/
            PrevisaoTempo previsaoTempo = obterDadosPrevisao(dadosCep);

            /*Obtem os dados da cotação dolar*/
            Cotacao cotacao = obterDadosCotacao();

            /*Persiste os dados na base H2*/
            repository.save(this.parserRequest(request, dadosCep, previsaoTempo, cotacao));

            return "Gravacao realizada com sucesso, ID >> " + ++ID;
        } catch (Exception ex) {
            logger.error("Ocorreu um erro ao salvar os dados na base H2 >> " + ex.getStackTrace());
            return "Ocorreu um erro ao gravar os dados do cliente.";
        }
    }

    public ArrayList<CadastroPessoaResponse> consultar(String nome, Integer id) {
        ArrayList<CadastroPessoaResponse> cadastroPessoa = new ArrayList<>();
        List<PessoaEntity> listaEntityPessoas = new ArrayList<>();

        try {
            if (nome.contentEquals("ALL")) {
                listaEntityPessoas = repository.findAll();
            } else if (id > 0) {
                Optional<PessoaEntity> listaEntity = repository.findById(id);
                listaEntityPessoas.add(listaEntity.get());
            }

            for (PessoaEntity entity : listaEntityPessoas) {
                CadastroPessoaResponse dados = this.parserResponse(entity);
                cadastroPessoa.add(dados);
            }

            return cadastroPessoa;
        } catch (Exception ex) {
            logger.error("Ocorreu um erro ao recuperar os dados cadastrados do H2 >> " + ex.getCause());
            return cadastroPessoa;
        }
    }

    public String excluir(String nome, Integer id) {
        String status = "Registro excluido com sucesso!";

        try {
            if (nome.contentEquals("ALL")) {
                repository.deleteAll();
            } else if (id > 0) {
                repository.deleteById(id);
            }

            return status;
        } catch (Exception ex) {
            status = "Este registro não existe na base! >> ";
            logger.error(status + ex.getCause());
            return (status + ex.getMessage());
        }
    }

    public String alterar(CadastroPessoaRequest request) {
        String status = "Cadastro atualizado com sucesso!";

        try {
            this.salvar(request);
            return status;
        } catch (Exception ex) {
            status = "Ocorreu um erro ao atualizar o cadastro na base! >> ";
            logger.error(status + ex.getCause());
            return (status + ex.getMessage());
        }
    }

    private PessoaEntity parserRequest(CadastroPessoaRequest request, ConsultaCEP consultaCEP,
                                       PrevisaoTempo previsaoTempo, Cotacao cotacao) {

        logger.info("Realizando parser response para persistir no H2");

        PessoaEntity parser = new PessoaEntity();

        parser.setNome(request.getNome());
        parser.setSobreNome(request.getSobreNome());
        parser.setDataNascimento(request.getDataNascimento());
        parser.setNaturalidade(request.getNaturalidade());
        parser.setEstadoCivil(request.getEstadoCivil());
        parser.setNomeConjuge(request.getNomeConjuge());
        parser.setNomeMae(request.getNomeMae());
        parser.setEscolaridade(request.getEscolaridade());
        parser.setProfissao(request.getProfissao());
        parser.setRg(request.getRg());
        parser.setCpf(request.getCpf());
        parser.setCep(request.getCep());
        parser.setNumeroTelefone(request.getNumeroTelefone());
        parser.setNumeroCelular(request.getNumeroCelular());
        parser.setEmail(request.getEmail());
        parser.setObservacoes(request.getObservacoes());

        try {
            parser.setLogradouro(consultaCEP.getLogradouro());
            parser.setComplemento(consultaCEP.getComplemento());
            parser.setBairro(consultaCEP.getBairro());
            parser.setLocalidade(consultaCEP.getLocalidade());
            parser.setUf(consultaCEP.getUf());
            parser.setDddRegiao(consultaCEP.getDdd());
        } catch (Exception ex1) {
            logger.error("Ocorreu um erro ao realizar o parser endereco >> " + ex1.getCause());
        }

        parser.setPrevisaoTempo(previsaoTempo.toString());

        try {
            parser.setMoeda(cotacao.getResults().getCurrencies().getName());
            parser.setValorCompra(cotacao.getResults().getCurrencies().getBuy());
            parser.setValorVenda(cotacao.getResults().getCurrencies().getSell());
            parser.setVariacao(cotacao.getResults().getCurrencies().getVariation());
        } catch (Exception ex2) {
            logger.error("Ocorreu um erro ao realizar o parser cotacao >> " + ex2.getCause());
        }

        logger.info("Parser realizado com sucesso! >> " + parser.toString());
        return parser;
    }

    private CadastroPessoaResponse parserResponse(PessoaEntity entity) {
        logger.info("Realizando parser response...");
        CadastroPessoaResponse parser = new CadastroPessoaResponse();

        parser.setCodigo(entity.getCodigo());
        parser.setNome(entity.getNome());
        parser.setSobreNome(entity.getSobreNome());
        parser.setDataNascimento(entity.getDataNascimento());
        parser.setNaturalidade(entity.getNaturalidade());
        parser.setEstadoCivil(entity.getEstadoCivil());
        parser.setNomeConjuge(entity.getNomeConjuge());
        parser.setNomeMae(entity.getNomeMae());
        parser.setEscolaridade(entity.getEscolaridade());
        parser.setProfissao(entity.getProfissao());
        parser.setRg(entity.getRg());
        parser.setCpf(entity.getCpf());
        parser.setCep(entity.getCep());
        parser.setNumeroTelefone(entity.getNumeroTelefone());
        parser.setNumeroCelular(entity.getNumeroCelular());
        parser.setEmail(entity.getEmail());
        parser.setObservacoes(entity.getObservacoes());

        try {
            Endereco endereco = new Endereco();
            endereco.setLogradouro(entity.getLogradouro());
            endereco.setComplemento(entity.getComplemento());
            endereco.setBairro(entity.getBairro());
            endereco.setLocalidade(entity.getLocalidade());
            endereco.setUf(entity.getUf());
            endereco.setDddRegiao(entity.getDddRegiao());

            parser.setDadosEndereco(endereco);
        } catch (Exception ex1) {
            logger.error("Ocorreu um erro ao realizar o parser endereco >> " + ex1.getCause());
        }

        parser.setPrevisaoTempo(entity.getPrevisaoTempo());

        try {
            CotacaoMoeda cotacaoMoeda = new CotacaoMoeda();
            cotacaoMoeda.setMoeda(entity.getMoeda());
            cotacaoMoeda.setValorCompra(entity.getValorCompra());
            cotacaoMoeda.setValorVenda(entity.getValorVenda());
            cotacaoMoeda.setVariacao(entity.getVariacao());

            parser.setCotacaoDolar(cotacaoMoeda);
        } catch (Exception ex2) {
            logger.error("Ocorreu um erro ao realizar o parser cotacao >> " + ex2.getCause());
        }

        logger.info("Parser realizado com sucesso! >> " + parser.toString());
        return parser;
    }

    private ConsultaCEP obterDadosCep(CadastroPessoaRequest body) {
        ConsultaCEP consultaCEP = new ConsultaCEP();

        try {
            logger.info("Realizando consulta do CEP : " + body.getCep());
            return cepService.consultaCep(body.getCep());
        } catch (Exception ex) {
            logger.error("Ocorreu um erro ao recuperar os dados do CEP >> " + ex.getCause());
            return consultaCEP;
        }
    }

    private PrevisaoTempo obterDadosPrevisao(ConsultaCEP body) {
        PrevisaoTempo previsaoTempo = new PrevisaoTempo();

        try {
            logger.info("Realizando consulta da previsão do tempo para a cidade : " + body.getLocalidade());
            return tempoService.consultaPrevisaoTempo(body.getLocalidade());
        } catch (Exception ex) {
            logger.error("Ocorreu um erro ao recuperar os dados da Previsão do Tempo >> " + ex.getCause());
            return previsaoTempo;
        }
    }

    private Cotacao obterDadosCotacao() {
        Cotacao cotacao = new Cotacao();

        try {
            logger.info("Realizando consulta da cotacao do dolar");
            return cotacaoService.obterCotacaoDolar();
        } catch (Exception ex) {
            logger.error("Ocorreu um erro ao recuperar os dados da Cotação do dolar >> " + ex.getCause());
            return cotacao;
        }
    }
}
