package br.com.floricultura.erp.services;
import br.com.floricultura.erp.model.Produto;
import br.com.floricultura.erp.repository.ProdutoRepository;
import br.com.floricultura.erp.async.StockAlertWorker; // Adicionar esta importação
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository repository;
    @Autowired // Adicionar esta anotação para injeção de dependência
    private StockAlertWorker stockAlertWorker; // Injeção do worker de alerta de estoque
    public List<Produto> listarTodos() {
        return repository.findAll();
    }
    public Produto salvar(Produto produto) {
        Produto savedProduto = repository.save(produto);
        stockAlertWorker.checkStockLevel(savedProduto); // Chamada assíncrona para verificar estoque
        return savedProduto;
    }
    public Optional<Produto> buscarPorId(Long id) {
        return repository.findById(id);
    }
    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
