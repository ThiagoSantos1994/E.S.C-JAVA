package br.com.esc.backend.facade;

import br.com.esc.backend.business.DespesasParceladasBusiness;
import br.com.esc.backend.business.ImportacaoBusiness;
import br.com.esc.backend.domain.DespesaParceladaDAO;
import br.com.esc.backend.domain.DespesasParceladasRequest;
import br.com.esc.backend.domain.DetalheDespesasParceladasResponse;
import br.com.esc.backend.exception.ErroNegocioException;
import br.com.esc.backend.service.ImportarLancamentosServices;
import br.com.esc.backend.utils.AuthUtils;
import br.com.esc.backend.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;

import static br.com.esc.backend.utils.DataUtils.*;
import static br.com.esc.backend.utils.MotorCalculoUtils.convertStringToDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DespesasParceladasFacade {

    private final DespesasParceladasBusiness parceladasBusiness;
    private final ImportacaoBusiness importacaoBusiness;
    private final ImportarLancamentosServices importarLancamentosServices;

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void processarDespesaParceladaAutomatica(DespesasParceladasRequest request) {
        log.info("== Iniciando orquestração de geração automatica de despesas parceladas e importação ==");

        this.validarPreCondicoes(request);

        log.info("== Gerando fluxo de parcelas para despesa parcelada automatica ==");
        var parcelas = this.gerarFluxoParcelas(request);
        request.setIdDespesaParcelada(parcelas.getIdDespesaParcelada());

        log.info("== Criando despesa parcelada automatica ==");
        this.criarDespesaParcelada(request);

        log.info("== Gravando parcelas da despesa parcelada automatica ==");
        parceladasBusiness.gravarParcela(parcelas.getParcelas());

        log.info("== Importando despesa parcelada automatica ==");
        this.importarDespesaParcelada(request);

        //TODO - Avaliar possibilidade de consolidar a despesa de forma automatica.
        log.info("== Finalizando orquestração de geração automatica de despesas parceladas e importação ==");
    }

    private void validarPreCondicoes(DespesasParceladasRequest request) {
        log.info("Validando se a despesa e detalhe de despesa informados existem na base de dados para o usuário logado.");
        importarLancamentosServices.validarDespesaEDetalheExistentes(request.getDetalheDespesasMensais().getIdDespesa(), request.getDetalheDespesasMensais().getIdDetalheDespesa(), AuthUtils.getUserId());

        log.info("Validando se já existe uma despesa parcelada com o mesmo título para o usuário logado.");
        this.validarTituloDespesaParcelada(request);

        log.info("Validacoes de pré-condições concluídas com sucesso.");
    }

    private void validarTituloDespesaParcelada(DespesasParceladasRequest request) {
        var isDespesaParceladaExistente = parceladasBusiness.obterDespesaParceladaPorNome(request.getNomeDespesaParcelada(), AuthUtils.getUserId());

        if (!ObjectUtils.isEmpty(isDespesaParceladaExistente)) {
            throw new ErroNegocioException("Ja existe uma despesa parcelada com o mesmo título, favor informar outro título para a despesa parcelada.");
        }
    }

    private DetalheDespesasParceladasResponse gerarFluxoParcelas(DespesasParceladasRequest request) {
        return parceladasBusiness.gerarFluxoParcelasV2(-1,
                request.getValorParcela(),
                request.getQtdeParcelas(),
                request.getVigenciaInicial(),
                AuthUtils.getUserId()
        );
    }

    private void criarDespesaParcelada(DespesasParceladasRequest request) throws ParseException {
        final var vigenciaInicial = convertStringToGenericDate(request.getVigenciaInicial());
        final var vigenciaFinal = retornaDataPersonalizada(retornaPrimeiroDiaMes(request.getVigenciaInicial()), (request.getQtdeParcelas() - 1));

        final var despesaRequest = DespesaParceladaDAO.builder()
                .idDespesaParcelada(request.getIdDespesaParcelada())
                .dsTituloDespesaParcelada(request.getNomeDespesaParcelada().toUpperCase())
                .dsMesVigIni(retornaMesToDate(vigenciaInicial).toString())
                .dsAnoVigIni(retornaAnoToDate(vigenciaInicial).toString())
                .dsVigenciaFin(convertDateToString_MMYYYY(vigenciaFinal))
                .nrTotalParcelas(request.getQtdeParcelas())
                .vlFatura(this.calcularValorTotalDespesa(request))
                .vlParcela(request.getValorParcela())
                .idFuncionario(AuthUtils.getUserId())
                .nrParcelasAdiantadas(0)
                .tpBaixado("N")
                .build();

        parceladasBusiness.gravarDespesaParcelada(despesaRequest);
    }

    private String calcularValorTotalDespesa(DespesasParceladasRequest request) {
        BigDecimal valorParcela = convertStringToDecimal(request.getValorParcela());

        BigDecimal calculo = valorParcela.multiply(
                BigDecimal.valueOf(request.getQtdeParcelas())
        );

        return calculo.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private void importarDespesaParcelada(DespesasParceladasRequest request) {
        final var detalheDespesasRequest = request.getDetalheDespesasMensais();

        importacaoBusiness.processarImportacaoDespesaParcelada(
                detalheDespesasRequest.getIdDespesa(),
                detalheDespesasRequest.getIdDetalheDespesa(),
                request.getIdDespesaParcelada(),
                detalheDespesasRequest.getIdConsolidacao(),
                AuthUtils.getUserId()
        );
    }
}
