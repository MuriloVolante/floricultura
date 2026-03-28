package br.com.floricultura.erp.services;

import br.com.floricultura.erp.model.*;
import br.com.floricultura.erp.repository.*;
import br.com.floricultura.erp.async.StockAlertWorker; // Import do worker do Vinícius
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final MovimentacaoEstoqueRepository movimentacaoRepository; // Repositório da nova classe
    private final StockAlertWorker stockAlertWorker; // O "Alertador" assíncrono

    @Transactional
    public Pedido criarPedido(Pedido pedido) {
        pedido.setDataPedido(LocalDate.now());
        pedido.setStatus("FINALIZADO");

        BigDecimal total = BigDecimal.ZERO;

        // Primeiro loop: Preços e Total
        for (PedidoItem item : pedido.getItens()) {
            Produto p = produtoRepository.findById(item.getProduto().getId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado ID: " + item.getProduto().getId()));

            item.setValorUnitario(p.getValorVenda());
            item.setPedido(pedido);

            BigDecimal subtotal = p.getValorVenda().multiply(BigDecimal.valueOf(item.getQuantidade()));
            total = total.add(subtotal);
        }

        pedido.setValorTotal(total);
        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        // Segundo loop: Estoque e Movimentação (ONDE DAVA O ERRO)
        for (PedidoItem item : pedidoSalvo.getItens()) {
            Produto p = item.getProduto();

            // --- PROTEÇÃO CONTRA NULL ---
            int estoqueAtual = (p.getQuantidadeEstoque() != null) ? p.getQuantidadeEstoque() : 0;
            int quantidadeVendida = (item.getQuantidade() != null) ? item.getQuantidade() : 0;

            // Agora a conta nunca será com null
            p.setQuantidadeEstoque(estoqueAtual - quantidadeVendida);

            // Registro da Movimentação
            MovimentacaoEstoque mov = new MovimentacaoEstoque();
            mov.setDataHora(LocalDateTime.now());
            mov.setTipo("SAIDA_VENDA");
            mov.setQuantidade(quantidadeVendida);
            mov.setProduto(p);

            movimentacaoRepository.save(mov);
            produtoRepository.save(p);

            // Chama o worker do Vinícius
            stockAlertWorker.checkStockLevel(p);
        }

        return pedidoSalvo;
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }
}