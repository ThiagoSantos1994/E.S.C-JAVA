package br.com.esc.back.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DespesasMensaisResponse {
    private List<ListaDespesasMensais> listaDespesasMensais;
}
