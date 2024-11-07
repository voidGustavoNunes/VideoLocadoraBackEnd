package github.com.voidGustavoNunes.projetoLocadora.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import github.com.voidGustavoNunes.exception.RegistroNotFoundException;
import github.com.voidGustavoNunes.projetoLocadora.model.Cliente;
import github.com.voidGustavoNunes.projetoLocadora.model.Dependente;
import github.com.voidGustavoNunes.projetoLocadora.repository.DependenteRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class DependenteService extends GenericServiceImpl<Dependente, DependenteRepository>{

    @Autowired
    private ClienteService clienteService;

    protected DependenteService(DependenteRepository repository) {
        super(repository);
    }

    public List<Dependente> getAllDependentesOrdenados() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "nome"));

    }

    @Override
    public void saveValidation(Dependente entity) throws RegistroNotFoundException {
        if (entity.getId() != null) {
            // Se o id não for nulo, estamos atualizando um registro existente
            Dependente existingDependente = repository.findById(entity.getId())
                    .orElseThrow(() -> new RegistroNotFoundException(entity.getId()));
            
            // Verifica se o nome está sendo alterado
            if (!existingDependente.getNome().equals(entity.getNome())) {
                // Verifica se o novo nome já existe
                if (repository.existsByNome(entity.getNome())) {
                    throw new IllegalArgumentException("Já existe um Dependente com o nome: " + entity.getNome());
                }
            }
        } else {
            // Se o id for nulo, estamos criando um novo registro
            if (repository.existsByNome(entity.getNome())) {
                throw new IllegalArgumentException("Já existe um Dependente com o nome: " + entity.getNome());
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
    public Dependente criarDependenteParaCliente(@Valid Dependente dependente, Long clienteId) {
        Cliente cliente = clienteService.buscarPorId(clienteId);
        dependente.setCliente(cliente); // Associa o dependente ao cliente
        Dependente novoDependente = repository.save(dependente);
        
        clienteService.adicionarDependente(clienteId, novoDependente); // Chama a lógica de adicionar dependente
        return novoDependente;
    }
    
}
