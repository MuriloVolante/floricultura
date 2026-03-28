package br.com.floricultura.erp.services;
import br.com.floricultura.erp.model.Cliente;
import br.com.floricultura.erp.repository.ClienteRepository;
import br.com.floricultura.erp.async.WelcomeEmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final WelcomeEmailSender emailSender; // Injeção do componente de envio de e-mail
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }
    public Cliente salvar(Cliente cliente) {
        Cliente savedCliente = clienteRepository.save(cliente);
        emailSender.sendWelcomeEmail(savedCliente); // Chamada assíncrona para enviar e-mail
        return savedCliente;
    }
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }
}
