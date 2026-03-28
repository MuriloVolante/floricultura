package br.com.floricultura.erp.async;
import br.com.floricultura.erp.model.Produto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
@Component
@Slf4j
public class StockAlertWorker {
    @Async
    public void checkStockLevel(Produto produto) {
        if (produto.getLimiteEstoqueMinimo() == null) {
            log.debug("WORKER DE ESTOQUE: Produto {} (ID: {}) não possui limite de estoque mínimo configurado.", produto.getDescricao(), produto.getId());
            return;
        }
        if (produto.getQuantidadeEstoque() != null && produto.getQuantidadeEstoque() < produto.getLimiteEstoqueMinimo()) {
            log.warn("🚨 ALERTA DE ESTOQUE BAIXO: Produto {} (ID: {}) está com {} unidades, abaixo do limite mínimo de {}.",
                    produto.getDescricao(), produto.getId(), produto.getQuantidadeEstoque(), produto.getLimiteEstoqueMinimo());
            // Em um cenário real, aqui poderia ser implementada a lógica para:
            // - Enviar e-mail para o gerente de estoque
            // - Gerar um pedido de compra automático
            // - Notificar o sistema de vendas
        } else {
            log.info("WORKER DE ESTOQUE: Produto {} (ID: {}) está com {} unidades, acima ou no limite mínimo de {}.",
                    produto.getDescricao(), produto.getId(), produto.getQuantidadeEstoque(), produto.getLimiteEstoqueMinimo());
        }
    }
}
