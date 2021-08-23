package br.com.esc.back.business;

import br.com.esc.back.domain.DespesasFixasMensaisResponse;
import br.com.esc.back.domain.DespesasMensaisResponse;
import br.com.esc.back.domain.DetalheDespesasMensais;
import br.com.esc.back.domain.ListaDespesasMensais;
import br.com.esc.back.repository.CalculoRepository;
import br.com.esc.back.repository.DespesasRepository;
import br.com.esc.back.restcontroller.LoginApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

@Component
public class DespesasBusiness {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    DespesasRepository repository;
    @Autowired
    CalculoRepository calculoRepository;
    @Autowired
    MotorCalculoBusiness calculoService = new MotorCalculoBusiness();

    public DespesasFixasMensaisResponse getListaDespesasFixasMensais(String ds_Mes, String ds_Ano, String id_Usuario) {
        logger.info("Obtendo lista despesas fixas mensais...");
        DespesasFixasMensaisResponse response = repository.getListaDespesasFixas(ds_Mes, ds_Ano, parseInt(id_Usuario));
        return response;
    }

    public DespesasMensaisResponse getListaDespesasMensais(String id_Usuario, String id_Despesa) throws Exception {
        logger.info("Obtendo lista despesas mensais...");
        DespesasMensaisResponse response = repository.getListaDespesas(parseInt(id_Usuario), parseInt(id_Despesa));
        return parserResponse(response, parseInt(id_Usuario));
    }

    public DetalheDespesasMensais getDetalheDespesasMensais(String id_Despesa, String id_DetalheDespesa, String id_Usuario) throws Exception {
        logger.info("Obtendo detalhe despesas mensais...");
        DetalheDespesasMensais response = repository.getDetalheDespesasMensais(parseInt(id_Despesa), parseInt(id_DetalheDespesa), parseInt(id_Usuario));
        return response;
    }

    private DespesasMensaisResponse parserResponse(DespesasMensaisResponse res, Integer id_Usuario) throws Exception {
        logger.info("Realizando parser despesas mensais...");
        DespesasMensaisResponse parser = new DespesasMensaisResponse();
        List<ListaDespesasMensais> listaDespesasMensais = new ArrayList<>();

        try {
            for (ListaDespesasMensais lista : res.getListaDespesasMensais()) {
                if (null == lista.getTp_LinhaSeparacao() || lista.getTp_LinhaSeparacao().isEmpty()) {
                    Double vlTotalDespesa = calculoRepository.getValorDespesaTotal(lista.getId_Despesa(), lista.getId_DetalheDespesa(), id_Usuario);
                    Double vlTotalPendente = calculoRepository.getValorDespesasPendentes(lista.getId_Despesa(), lista.getId_DetalheDespesa(), id_Usuario);
                    Double vlTotalPago = calculoRepository.getValorDespesasPagas(lista.getId_Despesa(), lista.getId_DetalheDespesa(), id_Usuario);
                    String vlLimite = lista.getVl_Limite().replace(',', '.');
                    String porcentagem = "";
                    if (!vlLimite.isEmpty()) {
                        porcentagem = calculoService.obterPercentual(vlTotalDespesa, parseDouble(vlLimite));
                    }

                    lista.setVl_Total(vlTotalDespesa.toString());
                    lista.setVl_TotalPendente(vlTotalPendente.toString());
                    lista.setVl_TotalPago(vlTotalPago.toString());
                    lista.setPercentual(porcentagem);

                    if (vlTotalPendente == 0d) {
                        lista.setTp_Status("*PAGO*");
                    } else {
                        lista.setTp_Status("*PEND*");
                    }
                    listaDespesasMensais.add(lista);
                } else {
                    /*Linha Separacao*/
                    listaDespesasMensais.add(lista);
                }
            }

            parser.setListaDespesasMensais(listaDespesasMensais);
            return parser;
        } catch (Exception e) {
            logger.error("Ocorreu um erro ao realizar o parser => " + e.getMessage());
            throw new Exception();
        }
    }
}
