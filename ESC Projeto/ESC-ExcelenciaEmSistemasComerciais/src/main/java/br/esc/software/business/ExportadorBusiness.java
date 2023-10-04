package br.esc.software.business;

import br.esc.software.commons.exceptions.ExcecaoGlobal;
import br.esc.software.commons.utils.ObjectParser;
import br.esc.software.domain.Response;
import br.esc.software.service.ExportadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static br.esc.software.commons.utils.GlobalUtils.LogInfo;

@Component
public class ExportadorBusiness {

    @Autowired
    private ExportadorService service;

    public String iniciarExportacao() throws ExcecaoGlobal {
        Response response = new Response();
        ObjectParser parser = new ObjectParser();

        try {
            service.gerarArquivoExportacao();
            response.setResponse("Processamento concluido! Arquivo disponibilizado em: " + service.pathArquivo);
            LogInfo(response.getResponse());
            return parser.parser(response);
        } catch (Exception ex) {
            String strErro = ("Ocorreu um erro inesperado no metodo iniciarExportacao, processamento interrompido -> " + ex);
            throw new ExcecaoGlobal(strErro, ex);
        }
    }

    public StringBuffer gerarScriptImplantacao() throws Exception {
        return service.montaScriptImplantacao();
    }
}
