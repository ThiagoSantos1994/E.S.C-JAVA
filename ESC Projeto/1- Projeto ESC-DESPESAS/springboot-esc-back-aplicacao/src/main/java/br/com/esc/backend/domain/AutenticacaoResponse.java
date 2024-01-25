package br.com.esc.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AutenticacaoResponse {
    private Integer idLogin;
    private String mensagem;
    private String nomeUsuario;
    private String autenticacao;
    private boolean autorizado;
}
