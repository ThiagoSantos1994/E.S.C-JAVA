package br.com.esc.backend.exception;

import br.com.esc.backend.mapper.exception.ErroRepresentation;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErroRepresentation> handlerException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(getErroRepresentation(e));
    }

    private static ErroRepresentation getErroRepresentation(Exception e) {
        log.error("ExceptionHandler | Erro ao realizar operação no backend: {}", e.getMessage());

        var erro = new ErroRepresentation();
        erro.setCodigo(HttpStatus.BAD_REQUEST.value());
        erro.setMensagem("Ocorreu um erro no servidor >>>> trace: " + e.getCause());

        return erro;
    }
}
