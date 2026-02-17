package br.com.esc.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Aspecto AOP que intercepta métodos anotados com @RetryOnDeadlock
 * e implementa retry automático em caso de deadlock no SQL Server.
 *
 * Funcionamento:
 * 1. Detecta se a exceção é um deadlock (erro 1205 do SQL Server)
 * 2. Aguarda um tempo antes de retentar (com backoff exponencial)
 * 3. Retenta até 3 vezes antes de desistir
 * 4. Se não for deadlock, propaga a exceção imediatamente
 *
 * Exemplo de uso:
 * <pre>
 * {@code
 * @RetryOnDeadlock
 * @Transactional(rollbackFor = Exception.class)
 * public void meuMetodo() {
 *     // código que pode causar deadlock
 * }
 * }
 * </pre>
 *
 * @author ESC Backend Team
 * @since 1.0.25
 */
@Aspect
@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class DeadlockRetryAspect {

    private static final int MAX_RETRIES = 3;
    private static final long INITIAL_DELAY_MS = 100;
    private static final int BACKOFF_MULTIPLIER = 2;
    private static final String DEADLOCK_ERROR_MESSAGE = "deadlock";
    private static final int SQL_SERVER_DEADLOCK_ERROR_CODE = 1205;

    /**
     * Intercepta métodos anotados com @RetryOnDeadlock e implementa retry automático.
     *
     * @param joinPoint ponto de junção do método interceptado
     * @return resultado da execução do método
     * @throws Throwable exceção propagada se todas as tentativas falharem
     */
    @Around("@annotation(br.com.esc.backend.config.RetryOnDeadlock)")
    public Object retryOnDeadlock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getDeclaringClass().getSimpleName() + "." + method.getName();

        int attempt = 0;
        long delayMs = INITIAL_DELAY_MS;
        Throwable lastException = null;

        while (attempt < MAX_RETRIES) {
            attempt++;
            try {
                return joinPoint.proceed();
            } catch (Throwable ex) {
                if (isDeadlockException(ex)) {
                    lastException = ex;

                    if (attempt < MAX_RETRIES) {
                        log.warn("Deadlock detectado no método {} - Tentativa {}/{} - Aguardando {}ms antes de retentar...",
                                methodName, attempt, MAX_RETRIES, delayMs);

                        try {
                            Thread.sleep(delayMs);
                            delayMs *= BACKOFF_MULTIPLIER;
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            log.error("Thread interrompida durante retry de deadlock no método {}", methodName);
                            throw ex;
                        }
                    } else {
                        log.error("Número máximo de tentativas ({}) atingido para o método {} após deadlocks consecutivos",
                                MAX_RETRIES, methodName);
                    }
                } else {
                    // Não é deadlock, propaga a exceção imediatamente
                    throw ex;
                }
            }
        }

        // Se chegou aqui, todas as tentativas falharam com deadlock
        throw lastException;
    }

    /**
     * Verifica se a exceção é causada por um deadlock do SQL Server.
     *
     * Critérios de detecção:
     * - Mensagem contém "deadlock" ou "was deadlocked on lock resources"
     * - SQLException com código de erro 1205
     *
     * @param throwable exceção a ser verificada
     * @return true se for deadlock, false caso contrário
     */
    private boolean isDeadlockException(Throwable throwable) {
        Throwable current = throwable;

        while (current != null) {
            // Verifica a mensagem de erro
            String message = current.getMessage();
            if (message != null) {
                String lowerMessage = message.toLowerCase();
                if (lowerMessage.contains(DEADLOCK_ERROR_MESSAGE) ||
                        lowerMessage.contains("was deadlocked on lock resources")) {
                    return true;
                }
            }

            // Verifica se é SQLException com código 1205
            if (current instanceof java.sql.SQLException) {
                java.sql.SQLException sqlEx = (java.sql.SQLException) current;
                if (sqlEx.getErrorCode() == SQL_SERVER_DEADLOCK_ERROR_CODE) {
                    return true;
                }
            }

            current = current.getCause();
        }

        return false;
    }
}

