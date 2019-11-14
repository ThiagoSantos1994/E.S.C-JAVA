package br.esc.software.integration;

import static br.esc.software.global.Global.LogErro;
import static br.esc.software.global.Global.LogInfo;

import br.esc.software.business.ImportarCEPBusiness;

public class ImportarCEP {
	/*
	 * Chama a API viacep para buscar os dados do endereço com base no CEP informado
	 * (05/07/2019)
	 */
	 
	public ImportarCEP(String[] parametroCep) {
		String sCep = parametroCep[4];
		LogInfo("Inicializando API de Importação de CEP. ParametroEntrada ->> " + sCep);

		if (sCep.equals("")) {
			LogErro("Necessário informar o CEP nos parametros de entrada");
			return;
		}
		
		ImportarCEPBusiness business = new ImportarCEPBusiness();
		business.CepPOST(sCep);
	}
}
