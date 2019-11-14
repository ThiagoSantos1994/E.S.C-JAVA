package br.esc.software.dao;

import java.sql.SQLException;

import br.esc.software.domain.CepModel;
import br.esc.software.persistence.ImportarCepPersistence;

public class ImportarCepDao extends ImportarCepPersistence{
	
	public boolean inserirDadosCep(CepModel cep) throws SQLException {
		return super.inserirDadosCep(cep);
	}
}
