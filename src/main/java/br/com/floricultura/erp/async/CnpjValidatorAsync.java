package br.com.floricultura.erp.async;
import br.com.floricultura.erp.model.Fornecedor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
@Component
@Slf4j
public class CnpjValidatorAsync {
    @Async
    public void validateCnpj(Fornecedor fornecedor) {
        try {
            // Simula uma chamada a uma API externa de validação de CNPJ, que pode ser demorada
            Thread.sleep(2500); // 2.5 segundos de atraso
            boolean isValid = Math.random() > 0.1; // 90% de chance de ser válido
            if (isValid) {
                log.info("VALIDAÇÃO CNPJ ASSÍNCRONA: CNPJ do fornecedor {} (ID: {}) validado com sucesso.", fornecedor.getNome(), fornecedor.getId());
            } else {
                log.warn("VALIDAÇÃO CNPJ ASSÍNCRONA: CNPJ do fornecedor {} (ID: {}) inválido ou com problemas na API externa.", fornecedor.getNome(), fornecedor.getId());
                // Em um cenário real, poderia atualizar o status do fornecedor, enviar notificação, etc.
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Erro durante a validação assíncrona de CNPJ para o fornecedor {}: {}", fornecedor.getNome(), e.getMessage());
        }
    }
}
