package br.esc.software.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import static br.esc.software.global.Global.*;

import br.esc.software.dao.ImportarCepDao;
import br.esc.software.domain.CepModel;
import br.esc.software.exceptions.ErroGenericoException;
import br.esc.software.global.ObjectUtils;

public class ImportarCEPBusiness {

	private static String URL_CEP = "https://viacep.com.br/ws/";

	@Autowired
	ImportarCepDao dao;
	RestTemplate restTemplate;

	public boolean CepPOST(String sCep) {
		CepModel cep = restTemplate.getForObject((URL_CEP + sCep + "/json/"), CepModel.class);

		if (ObjectUtils.isNull(cep.getCep())) {
			LogInfo("Não existe dados na API para o CEP: " + sCep);
			return false;
		}

		try {
			this.GravarCepBaseDados(cep);
		} catch (ErroGenericoException e) {
			return false;
		}
		
		return true;
	}

	private boolean GravarCepBaseDados(CepModel sCep) throws ErroGenericoException {
		try {
			if (dao.inserirDadosCep(sCep)) {
				LogInfo("CEP " + sCep + " Inserido na base com sucesso!");
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new ErroGenericoException("Ocorreu um erro ao GravarCepBaseDados - ", e);
		}
	}

}
