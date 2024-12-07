package github.com.voidGustavoNunes.projetoLocadora.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import github.com.voidGustavoNunes.exception.RegistroNotFoundException;
import github.com.voidGustavoNunes.projetoLocadora.model.Dependente;
import github.com.voidGustavoNunes.projetoLocadora.model.Socio;
import github.com.voidGustavoNunes.projetoLocadora.repository.DependenteRepository;
import github.com.voidGustavoNunes.projetoLocadora.repository.SocioRepository;

@Service
public class DependenteService extends GenericServiceImpl<Dependente, DependenteRepository>{

    protected DependenteService(DependenteRepository repository) {
        super(repository);
    }

    public List<Dependente> getAllDependentesOrdenados() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "nome"));

    }

    @Autowired
    private SocioRepository socioRepository;

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
        // Valida associação ao sócio
        if (entity.getId() == null) {
            throw new IllegalArgumentException("O dependente deve estar associado a um sócio.");
        }
        validarInclusao(entity.getSocioId());
    }

    public void validarInclusao(Long socioId) {
        Socio socio = socioRepository.findById(socioId)
                .orElseThrow(() -> new RegistroNotFoundException(socioId));

        if (socio.getDependentes().size() >= 3) {
            throw new IllegalArgumentException("Sócio já possui três dependentes ativos.");
        }
    }

    

    // @Transactional
    // public List<Dependente> criarDependentesParaCliente(@Valid List<Dependente> dependentes, Long clienteId) {
    //     Cliente cliente = clienteService.buscarPorId(clienteId);
    
    //     // Associa cada dependente ao cliente e salva no repositório
    //     dependentes.forEach(dependente -> saveValidation(dependente)); //verificar recursividade
    //     dependentes.forEach(dependente -> dependente.setCliente(cliente));
        
    //     List<Dependente> novosDependentes = repository.saveAll(dependentes);


    
    //     // Adiciona os dependentes ao cliente usando o serviço
    //     clienteService.adicionarDependentes(clienteId, novosDependentes);
    
    //     return novosDependentes;
    // }
    
    
}
