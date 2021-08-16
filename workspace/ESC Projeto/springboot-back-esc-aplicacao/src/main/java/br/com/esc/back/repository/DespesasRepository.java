package br.com.esc.back.repository;

import br.com.esc.back.domain.DespesasFixasMensaisResponse;
import br.com.esc.back.domain.DespesasMensaisResponse;
import br.com.esc.back.domain.ListaDespesasFixasMensais;
import br.com.esc.back.domain.ListaDespesasMensais;
import br.com.esc.back.mappers.DespesasFixasMensaisMapper;
import br.com.esc.back.mappers.DespesasMensaisMapper;
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
}
