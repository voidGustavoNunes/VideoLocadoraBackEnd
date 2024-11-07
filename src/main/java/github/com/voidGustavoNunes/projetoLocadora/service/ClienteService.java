package github.com.voidGustavoNunes.projetoLocadora.service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import github.com.voidGustavoNunes.exception.RegistroNotFoundException;
import github.com.voidGustavoNunes.projetoLocadora.model.Ator;
import github.com.voidGustavoNunes.projetoLocadora.model.Cliente;
import github.com.voidGustavoNunes.projetoLocadora.model.Dependente;
import github.com.voidGustavoNunes.projetoLocadora.model.Titulo;
import github.com.voidGustavoNunes.projetoLocadora.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Service
public class ClienteService extends GenericServiceImpl<Cliente, ClienteRepository>{

    protected ClienteService(ClienteRepository repository) {
        super(repository);
    }

    public List<Cliente> getAllClientesOrdenados() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }

    @Override
    public void saveValidation(Cliente entity) throws RegistroNotFoundException {
        if (entity.getId() != null) {
            // Se o id não for nulo, estamos atualizando um registro existente
            Cliente existingCliente = repository.findById(entity.getId())
                    .orElseThrow(() -> new RegistroNotFoundException(entity.getId()));
            
            // Verifica se o nome está sendo alterado
            if (!existingCliente.getNome().equals(entity.getNome())) {
                // Verifica se o novo nome já existe
                if (repository.existsByNome(entity.getNome())) {
                    throw new IllegalArgumentException("Já existe um Cliente com o nome: " + entity.getNome());
                }
            }
        } else {
            // Se o id for nulo, estamos criando um novo registro
            if (repository.existsByNome(entity.getNome())) {
                throw new IllegalArgumentException("Já existe um Cliente com o nome: " + entity.getNome());
            }
        }
        if (entity.getDataNascimento() != null) {
            LocalDate hoje = LocalDate.now();
            if (entity.getDataNascimento().isAfter(hoje)) {
                throw new IllegalArgumentException("A data de nascimento não pode ser no futuro.");
            }
        }
    }

    @Transactional
    public Cliente adicionarDependente(@NotNull @Positive Long clienteId, @Valid @NotNull Dependente dependente) {
        Cliente cliente = repository.findById(clienteId)
                .orElseThrow(() -> new RegistroNotFoundException(clienteId));

        dependente.setCliente(cliente);  // Associa o dependente ao cliente

        if (cliente.getDependentes().contains(dependente)) {
            throw new IllegalArgumentException("Este dependente já está associado a este cliente.");
        }

        cliente.getDependentes().add(dependente);  // Adiciona o dependente à lista

        return repository.save(cliente);
    }
    
}
