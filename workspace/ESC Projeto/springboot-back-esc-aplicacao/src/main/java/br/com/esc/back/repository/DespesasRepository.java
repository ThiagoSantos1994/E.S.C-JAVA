package br.com.esc.back.repository;

import br.com.esc.back.domain.*;
import br.com.esc.back.mapper.DespesasFixasMensaisMapper;
import br.com.esc.back.mapper.DespesasMensaisMapper;
import br.com.esc.back.mapper.DetalheDespesasMensaisMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DespesasRepository {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private JdbcTemplate jdbcTemplate;
    private DataSource dataSource;

    @Autowired
    public DespesasRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    public DespesasFixasMensaisResponse getListaDespesasFixas(String ds_Mes, String ds_Ano, Integer id_Usuario) {
        DespesasFixasMensaisResponse response = new DespesasFixasMensaisResponse();

        String sQuery = "SELECT ds_Descricao,vl_Total,tp_Status,id_Despesa,id_Ordem " +
                "FROM tbd_DespesasFixasMensais " +
                "WHERE ds_Mes = ? AND ds_Ano = ? AND id_Funcionario = ? AND id_Despesa <> 0 " +
                "ORDER BY id_Despesa,id_Ordem";

        logger.info("Consulta: " + sQuery);

        List<ListaDespesasFixasMensais> fixasMensais = jdbcTemplate.query(
                sQuery,
                new Object[]{ds_Mes, ds_Ano, id_Usuario},
                new DespesasFixasMensaisMapper());

        ArrayList<ListaDespesasFixasMensais> listaDespesasFixasMensais = new ArrayList<>();
        for (ListaDespesasFixasMensais row : fixasMensais) {
            ListaDespesasFixasMensais lista = new ListaDespesasFixasMensais();

            lista.setDs_Descricao(row.getDs_Descricao());
            lista.setVl_Total(row.getVl_Total());
            lista.setTp_Status(row.getTp_Status());
            lista.setId_Despesa(row.getId_Despesa());
            lista.setId_Ordem(row.getId_Ordem());

            listaDespesasFixasMensais.add(lista);
        }

        response.setListaDespesasFixasMensais(listaDespesasFixasMensais);
        return response;
    }

    public DespesasMensaisResponse getListaDespesas(Integer id_Usuario, Integer id_Despesa) {
        DespesasMensaisResponse response = new DespesasMensaisResponse();

        String sQuery = "SELECT id_Despesa, ds_NomeDespesa,vl_Limite,id_DetalheDespesa,tp_Emprestimo,id_Emprestimo,tp_Poupanca,tp_Anotacao,ds_NomeDespesa,tp_DebitoAutomatico,id_OrdemExibicao,tp_LinhaSeparacao,tp_DespesaReversa,tp_PoupancaNegativa,tp_Relatorio " +
                "FROM tbd_DespesaMensal " +
                "WHERE id_Despesa = ? AND id_Funcionario = ? " +
                "ORDER BY id_OrdemExibicao,id_DetalheDespesa";

        logger.info("Consulta: " + sQuery);

        List<ListaDespesasMensais> despesasMensais = jdbcTemplate.query(
                sQuery,
                new Object[]{id_Despesa, id_Usuario},
                new DespesasMensaisMapper());

        ArrayList<ListaDespesasMensais> listaDespesasMensais = new ArrayList<>();
        for (ListaDespesasMensais row : despesasMensais) {
            ListaDespesasMensais lista = new ListaDespesasMensais();

            lista.setId_Despesa(row.getId_Despesa());
            lista.setId_OrdemExibicao(row.getId_OrdemExibicao());
            lista.setDs_NomeDespesa(row.getDs_NomeDespesa());
            lista.setVl_Limite(row.getVl_Limite());
            lista.setId_DetalheDespesa(row.getId_DetalheDespesa());
            lista.setTp_Emprestimo(row.getTp_Emprestimo());
            lista.setId_Emprestimo(row.getId_Emprestimo());
            lista.setTp_Poupanca(row.getTp_Poupanca());
            lista.setTp_Anotacao(row.getTp_Anotacao());
            lista.setTp_DebitoAutomatico(row.getTp_DebitoAutomatico());
            lista.setTp_LinhaSeparacao(row.getTp_LinhaSeparacao());
            lista.setTp_DespesaReversa(row.getTp_DespesaReversa());
            lista.setTp_PoupancaNegativa(row.getTp_PoupancaNegativa());
            lista.setTp_Relatorio(row.getTp_Relatorio());

            listaDespesasMensais.add(lista);
        }

        response.setListaDespesasMensais(listaDespesasMensais);
        return response;
    }

    public DetalheDespesasMensais getDetalheDespesasMensais(Integer id_Despesa, Integer id_DetalheDespesa, Integer id_Usuario) {
        DetalheDespesasMensais response = new DetalheDespesasMensais();

        String sQuery = "SELECT id_Despesa, vl_Total,ds_Descricao,vl_TotalPago,tp_Status,id_Ordem,ds_Observacao,id_DetalheDespesa,ds_Observacao2,id_DespesaParcelada,id_Parcela,id_Funcionario,tp_Reprocessar,tp_Anotacao,tp_Meta,tp_ParcelaAdiada,tp_ParcelaAmortizada,tp_Relatorio,tp_LinhaSeparacao,id_DespesaLinkRelatorio " +
                "FROM tbd_DetalheDespesasMensais " +
                "WHERE id_Despesa = ? AND id_DetalheDespesa = ? AND id_Funcionario = ? " +
                "ORDER BY id_Ordem";

        logger.info("Consulta: " + sQuery);

        List<ListaDetalheDespesasMensais> detalheDespesasMensais = jdbcTemplate.query(
                sQuery,
                new Object[]{id_Despesa, id_DetalheDespesa, id_Usuario},
                new DetalheDespesasMensaisMapper());

        ArrayList<ListaDetalheDespesasMensais> despesasMensais = new ArrayList<>();
        for (ListaDetalheDespesasMensais row : detalheDespesasMensais) {
            ListaDetalheDespesasMensais listaDetalheDespesasMensais = new ListaDetalheDespesasMensais();

            listaDetalheDespesasMensais.setId_Despesa(row.getId_Despesa());
            listaDetalheDespesasMensais.setId_Ordem(row.getId_Ordem());
            listaDetalheDespesasMensais.setId_DetalheDespesa(row.getId_DetalheDespesa());
            listaDetalheDespesasMensais.setId_DespesaParcelada(row.getId_DespesaParcelada());
            listaDetalheDespesasMensais.setId_Parcela(row.getId_Parcela());
            listaDetalheDespesasMensais.setId_Funcionario(row.getId_Funcionario());
            listaDetalheDespesasMensais.setId_DespesaLinkRelatorio(row.getId_DespesaLinkRelatorio());
            listaDetalheDespesasMensais.setVl_Total(row.getVl_Total());
            listaDetalheDespesasMensais.setDs_Descricao(row.getDs_Descricao());
            listaDetalheDespesasMensais.setVl_TotalPago(row.getVl_TotalPago());
            listaDetalheDespesasMensais.setTp_Status(row.getTp_Status());
            listaDetalheDespesasMensais.setDs_Observacao(row.getDs_Observacao());
            listaDetalheDespesasMensais.setDs_Observacao2(row.getDs_Observacao2());
            listaDetalheDespesasMensais.setTp_Reprocessar(row.getTp_Reprocessar());
            listaDetalheDespesasMensais.setTp_Anotacao(row.getTp_Anotacao());
            listaDetalheDespesasMensais.setTp_Meta(row.getTp_Meta());
            listaDetalheDespesasMensais.setTp_ParcelaAdiada(row.getTp_ParcelaAdiada());
            listaDetalheDespesasMensais.setTp_ParcelaAmortizada(row.getTp_ParcelaAmortizada());
            listaDetalheDespesasMensais.setTp_Relatorio(row.getTp_Relatorio());
            listaDetalheDespesasMensais.setTp_LinhaSeparacao(row.getTp_LinhaSeparacao());

            despesasMensais.add(listaDetalheDespesasMensais);
        }

        response.setListaDetalheDespesasMensais(despesasMensais);
        return response;
    }
}
