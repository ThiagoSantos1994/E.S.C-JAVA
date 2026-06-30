package br.com.esc.backend.business;

import br.com.esc.backend.domain.ChaveKeyDAO;
import br.com.esc.backend.domain.ChaveKeySemUsoDAO;
import br.com.esc.backend.exception.CamposObrigatoriosException;
import br.com.esc.backend.repository.AplicacaoRepository;
import br.com.esc.backend.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChaveKeyBusiness {

    private final AplicacaoRepository repository;

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public ChaveKeyDAO retornaNovaChaveKey(String tipoChave) {
        log.info("========= Gerando chaveKey, tipoChave: {} =========", tipoChave);

        if (ObjectUtils.isEmpty(tipoChave)) {
            throw new CamposObrigatoriosException("tipoChave e obrigatorio.");
        }

        ChaveKeyDAO keyDAO = repository.getNovaChaveKey(tipoChave);
        if (ObjectUtils.isEmpty(keyDAO)) {
            throw new Exception("Sequencia não identificada na tabela de chaves primárias, favor contatar imediatamente o desenvolvedor do software.");
        }

        ChaveKeySemUsoDAO keySemUsoDAO = repository.getChaveKeySemUso(keyDAO.getDsNomeColuna(), keyDAO.getDsNomeTabela());
        if (keySemUsoDAO.getChave().equals(keyDAO.getNovaChave())) {
            log.info("========= Nova ChaveKey gerada com sucesso, tipoChave: {} novaChave: {} =========", tipoChave, keyDAO.getNovaChave());
            repository.updateChaveKeyUtilizada(keyDAO.getIdChaveKey());
        } else {
            log.info("========= Reutilizando ChaveKey existente sem uso, tipoChave: {} chave: {} =========", tipoChave, keySemUsoDAO.getChave());
            keyDAO.setNovaChave(keySemUsoDAO.getChave());
        }

        //Alterado a logica em 24/11/25 para não considerar o reuso de chaves existentes sem uso.
        //log.info("========= Nova ChaveKey gerada com sucesso, tipoChave: {} novaChave: {} =========", tipoChave, keyDAO.getNovaChave());
        //repository.updateChaveKeyUtilizada(keyDAO.getIdChaveKey());

        return keyDAO;
    }
}
