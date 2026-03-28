package br.com.floricultura.erp.jobs;
import br.com.floricultura.erp.model.Colaborador;
import br.com.floricultura.erp.repository.ColaboradorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;
@Component
@RequiredArgsConstructor
@Slf4j
public class ColaboradorScheduler {
    private final ColaboradorRepository colaboradorRepository;
    // Executa todo dia às 08:00 da manhã (segundos minutos horas dia-do-mês mês dia-da-semana)
    @Scheduled(fixedRate = 1800000)
    public void verificarAniversariantes() {
        log.info("Iniciando verificação de aniversariantes do dia...");
        LocalDate hoje = LocalDate.now();
        List<Colaborador> aniversariantes = colaboradorRepository.findByDataNascimentoMonthAndDay(hoje.getMonthValue(), hoje.getDayOfMonth());
        if (aniversariantes.isEmpty()) {
            log.info("Nenhum colaborador aniversariante hoje.");
        } else {
            aniversariantes.forEach(colaborador -> {
                log.info("🎉 Parabéns, {}! Aniversário de {} hoje.", colaborador.getNome(), colaborador.getNome());
                // Aqui poderia ser implementada a lógica de envio de e-mail de felicitações, notificação interna, etc.
            });
        }
        log.info("Verificação de aniversariantes concluída.");
    }
    // Executa a cada 10 segundos para simular verificação de contratos
    @Scheduled(fixedRate = 1800000)
    public void verificarStatusContratos() {
        log.info("Iniciando verificação de status de contratos de colaboradores...");
        // Simula a busca por contratos próximos do vencimento ou com status pendente
        List<Colaborador> colaboradoresComContratosPendentes = colaboradorRepository.findAll(); // Simulação
        colaboradoresComContratosPendentes.stream()
                .filter(c -> c.getDataContratacao() != null && c.getDataContratacao().plusYears(1).isBefore(LocalDate.now())) // Exemplo: contratos com mais de 1 ano
                .forEach(colaborador -> {
                    log.warn("⚠️ Contrato do colaborador {} (ID: {}) pode precisar de atenção. Data de contratação: {}", colaborador.getNome(), colaborador.getId(), colaborador.getDataContratacao());
                    // Lógica para notificar RH, gerar relatórios, etc.
                });
        log.info("Verificação de status de contratos concluída.");
    }
}
