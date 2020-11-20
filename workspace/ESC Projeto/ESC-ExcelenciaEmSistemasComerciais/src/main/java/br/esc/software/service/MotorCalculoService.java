package br.esc.software.service;

import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.domain.motorcalculo.MotorCalculo;
import br.esc.software.repository.MotorCalculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static br.esc.software.commons.utils.GlobalUtils.LogInfo;

@Service
public class MotorCalculoService {

    @Autowired
    private MotorCalculoRepository dao;

    public MotorCalculo obterValoresMotorCalculo(Integer ano) throws ExcecaoGlobal {
        MotorCalculo mapper = new MotorCalculo();

        try {
            LogInfo(">>Calculando Valor Total das Despesas");
            mapper.setVlTotalDespesas(dao.getValorTotalDespesas(ano));

            LogInfo(">>Calculando Valor Total das Receitas Positivas");
            mapper.setVlReceitaPositiva(dao.getValorTotalReceitaPositiva(ano));

            LogInfo(">>Calculando Valor Total Aplicado Poupanca");
            mapper.setVlTotalAplicadoPoupanca(dao.getValorTotalAplicadoPoupanca());

            LogInfo(">>Calculando Valor Total Emprestimos A Receber");
            mapper.setVlTotalEmprestimosAReceber(dao.getValorTotalEmprestimosAReceber());

            LogInfo(">>Calculando Valor Total dos Outros Emprestimos A Receber");
            mapper.setVlOutrosEmprestimosAReceber(dao.getValorOutrosEmprestimosAReceber());

            LogInfo(">>Calculando Valor Total dos Emprestimos A Pagar");
            mapper.setVlEmprestimosAPagar(dao.getValorEmprestimosAPagar(ano));

            LogInfo(">>Calculando Valor Estimativa Poupanca");
            mapper.setVlEstimativaPoupanca(mapper.getVlTotalAplicadoPoupancaDouble() + mapper.getVlTotalEmprestimosAReceberDouble());

            return mapper;
        } catch (Exception ex) {
            throw new ExcecaoGlobal(ex);
        }
    }
}
