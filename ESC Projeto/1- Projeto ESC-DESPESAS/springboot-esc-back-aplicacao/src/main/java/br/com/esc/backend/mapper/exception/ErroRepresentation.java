package br.com.esc.backend.mapper.exception;

import lombok.Data;

@Data
public class ErroRepresentation {
    private Integer codigo;
    private String mensagem;

    @Override
    public String toString() {
        return "ErroRepresentation{" +
                "codigo=" + codigo +
                ", mensagem='" + mensagem + '\'' +
                '}';
    }
}
