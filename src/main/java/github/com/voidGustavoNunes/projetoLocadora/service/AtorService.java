package github.com.voidGustavoNunes.projetoLocadora.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

import github.com.voidGustavoNunes.exception.RegistroNotFoundException;
import github.com.voidGustavoNunes.projetoLocadora.model.Ator;
import github.com.voidGustavoNunes.projetoLocadora.repository.AtorRepository;

@Service
public class AtorService extends GenericServiceImpl<Ator, AtorRepository >{

    
    protected AtorService(AtorRepository repository) {
        super(repository);
    }


    // Retorna a lista de atores ordenada pelo nome
    public List<Ator> getAllAtoresOrdenados() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }


    @Override
    public void saveValidation(Ator entity) throws RegistroNotFoundException {
        // Verifica se o ator já existe no banco
        if (entity.getId() != null) {
            // Se o id não for nulo, estamos atualizando um registro existente
            Ator existingAtor = repository.findById(entity.getId())
                    .orElseThrow(() -> new RegistroNotFoundException(entity.getId()));
            
            // Verifica se o nome está sendo alterado
            if (!existingAtor.getNome().equals(entity.getNome())) {
                // Verifica se o novo nome já existe
                if (repository.existsByNome(entity.getNome())) {
                    throw new IllegalArgumentException("Já existe um ator com o nome: " + entity.getNome());
                }
            }
        } else {
            // Se o id for nulo, estamos criando um novo registro
            if (repository.existsByNome(entity.getNome())) {
                throw new IllegalArgumentException("Já existe um ator com o nome: " + entity.getNome());
            }
        }
    }
}
