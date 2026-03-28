package br.com.floricultura.erp.async;
import br.com.floricultura.erp.model.Cargo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
@Component
@Slf4j
public class CargoAuditLogger {
    @Async
    public void logCargoCreation(Cargo cargo) {
        try {
            // Simula uma operação de I/O demorada, como enviar para um serviço externo de RH ou log de auditoria
            Thread.sleep(2000); // 2 segundos de atraso
            log.info("AUDITORIA ASSÍNCRONA: Novo cargo criado - ID: {}, Nome: {}", cargo.getId(), cargo.getNome());
            // Aqui poderia haver a lógica para integração com RH ou sistema de auditoria externa
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Erro durante o log de auditoria assíncrono para o cargo {}: {}", cargo.getNome(), e.getMessage());
        }
    }
}
