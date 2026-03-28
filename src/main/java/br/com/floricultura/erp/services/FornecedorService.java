package br.com.floricultura.erp.services;
import br.com.floricultura.erp.model.Fornecedor;
import br.com.floricultura.erp.repository.FornecedorRepository;
import br.com.floricultura.erp.async.CnpjValidatorAsync; // Adicionar esta importação
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class FornecedorService {
    @Autowired
    private FornecedorRepository fornecedorRepository;
    @Autowired // Adicionar esta anotação para injeção de dependência
    private CnpjValidatorAsync cnpjValidatorAsync; // Injeção do componente de validação assíncrona
    public Fornecedor salvar(Fornecedor fornecedor) {
        Fornecedor savedFornecedor = fornecedorRepository.save(fornecedor);
        cnpjValidatorAsync.validateCnpj(savedFornecedor); // Chamada assíncrona para validar CNPJ
        return savedFornecedor;
    }
    public List<Fornecedor> listarTodos() {
        return fornecedorRepository.findAll();
    }
    public Optional<Fornecedor> buscarPorId(Long id) {
        return fornecedorRepository.findById(id);
    }
    public Fornecedor atualizar(Long id, Fornecedor fornecedorDetalhes) {
        return fornecedorRepository.findById(id)
                .map(fornecedorExistente -> {
                    fornecedorExistente.setNome(fornecedorDetalhes.getNome());
                    fornecedorExistente.setCnpj(fornecedorDetalhes.getCnpj());
                    fornecedorExistente.setEndereco(fornecedorDetalhes.getEndereco());
                    fornecedorExistente.setCep(fornecedorDetalhes.getCep());
                    fornecedorExistente.setTelefone(fornecedorDetalhes.getTelefone());
                    return fornecedorRepository.save(fornecedorExistente);
                }).orElse(null);
    }
    public boolean excluir(Long id) {
        if (fornecedorRepository.existsById(id)) {
            fornecedorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
