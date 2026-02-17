package br.com.esc.backend.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação para marcar métodos que devem ser reexecutados automaticamente
 * em caso de deadlock no SQL Server.
 *
 * O aspecto DeadlockRetryAspect intercepta métodos com esta anotação e
 * implementa retry automático com backoff exponencial.
 *
 * Configuração padrão:
 * - Máximo de 3 tentativas
 * - Delay inicial de 100ms
 * - Backoff multiplicador de 2 (100ms, 200ms, 400ms)
 *
 * @author ESC Backend Team
 * @since 1.0.25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RetryOnDeadlock {
}

