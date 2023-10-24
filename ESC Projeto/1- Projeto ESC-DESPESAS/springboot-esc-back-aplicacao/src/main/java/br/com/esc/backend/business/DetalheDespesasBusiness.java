package br.com.esc.backend.business;

import br.com.esc.backend.domain.DetalheDespesasMensaisDTO;
import br.com.esc.backend.repository.AplicacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

@Slf4j
@RequiredArgsConstructor
public class DetalheDespesasBusiness {

    private final AplicacaoRepository repository;

    public DetalheDespesasMensaisDTO obterDetalheDespesaMensal(Integer idDespesa, Integer idDetalheDespesa, Integer idFuncionario) {
        var despesaMensal = repository.getDespesasMensais(idDespesa, idFuncionario, idDetalheDespesa);

        if (despesaMensal.size() <= 0) {
            return new DetalheDespesasMensaisDTO();
        }

        var detalheDespesasMensaisList = repository.getDetalheDespesasMensais(idDespesa, idDetalheDespesa, idFuncionario);
        return DetalheDespesasMensaisDTO.builder()
                .sizeDetalheDespesaMensalVB(detalheDespesasMensaisList.size())
                .despesaMensal(repository.getDespesasMensais(idDespesa, idFuncionario, idDetalheDespesa).get(0))
                .detalheDespesaMensal(detalheDespesasMensaisList)
                .build();
    }
}
