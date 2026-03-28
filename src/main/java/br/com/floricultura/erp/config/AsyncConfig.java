package br.com.floricultura.erp.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;
@Configuration
@EnableAsync // Habilita o suporte a métodos assíncronos
public class AsyncConfig {
    @Bean(name = "taskExecutor") // Define um Executor customizado
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); // Número mínimo de threads ativas
        executor.setMaxPoolSize(5);  // Número máximo de threads
        executor.setQueueCapacity(500); // Capacidade da fila de tarefas
        executor.setThreadNamePrefix("EmailSender-"); // Prefixo para os nomes das threads
        executor.initialize();
        return executor;
    }
}
