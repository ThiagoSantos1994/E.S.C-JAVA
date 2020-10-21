package br.esc.software.business;

import static br.esc.software.commons.GlobalUtils.LogInfo;

import org.springframework.stereotype.Component;

import br.esc.software.domain.MotorCalculoMapper;
import br.esc.software.repository.MotorCalculoDao;

@Component
public class MotorCalculoBusiness {

	MotorCalculoDao dao = new MotorCalculoDao();

	public MotorCalculoMapper realizarCalculo(Integer ano) {

		MotorCalculoMapper mapper = new MotorCalculoMapper();

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
	}

}
