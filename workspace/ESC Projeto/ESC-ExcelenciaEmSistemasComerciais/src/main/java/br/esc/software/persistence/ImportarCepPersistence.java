package br.esc.software.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import br.esc.software.domain.CepModel;
import br.esc.software.global.ConnectionSQL;

public class ImportarCepPersistence {

	static ConnectionSQL SQL = new ConnectionSQL();
	ResultSet RSAdo;

	protected boolean inserirDadosCep(CepModel cep) throws SQLException {
		boolean statusInsert = true;

		if (!this.validaExistenciaCEP(cep.getCep())) {
			try {
				statusInsert = 
						SQL.Insert_Table("INSERT INTO tbd_ListaCEP (CEP, Logradouro, Complemento, Bairro, Localidade, UF, Unidade, IBGE, GIA) Values ( '"
								+ cep.getCep() + "', '" + cep.getLogradouro() + "', '" + cep.getComplemento() + "' , '"
								+ cep.getBairro() + "' , '" + cep.getLocalidade() + "', '" + cep.getUf() + "','"
								+ cep.getUnidade() + "', '" + cep.getIbge() + "','" + cep.getGia() + "')");
			} catch (Exception e) {
				statusInsert = false;
				throw new SQLException("Ocorreu um erro no metodo inserirDadosCep -> ", e);
			}
		}
		
		return statusInsert;
	}

	private boolean validaExistenciaCEP(String cep) throws SQLException {
		RSAdo = SQL.Select_Table("SELECT CEP FROM tbd_ListaCEP WHERE CEP = '" + cep + "'");
		if (RSAdo.next()) {
			return true;
		}
		RSAdo.close();
		return false;
	}
}
