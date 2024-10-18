package github.com.voidGustavoNunes.projetoLocadora.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

import github.com.voidGustavoNunes.exception.RegistroNotFoundException;
import github.com.voidGustavoNunes.projetoLocadora.model.Classe;
import github.com.voidGustavoNunes.projetoLocadora.repository.ClasseRepository;

@Service
public class ClasseService extends GenericServiceImpl<Classe, ClasseRepository >{

    protected ClasseService(ClasseRepository repository) {
        super(repository);
    }

    
        // Retorna a lista de classes ordenada pelo nome
    public List<Classe> getAllClassesOrdenados() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }

    @Override
    public void saveValidation(Classe entity) throws RegistroNotFoundException {
        // Verifica se a classe já existe no banco
        if (entity.getId() != null) {
            // Se o id não for nulo, estamos atualizando um registro existente
            Classe existingClasse = repository.findById(entity.getId())
                    .orElseThrow(() -> new RegistroNotFoundException(entity.getId()));
            
            // Verifica se o nome está sendo alterado
            if (!existingClasse.getNome().equals(entity.getNome())) {
                // Verifica se o novo nome já existe
                if (repository.existsByNome(entity.getNome())) {
                    throw new IllegalArgumentException("Já existe uma classe com o nome: " + entity.getNome());
                }
            }
        } else {
            // Se o id for nulo, estamos criando um novo registro
            if (repository.existsByNome(entity.getNome())) {
                throw new IllegalArgumentException("Já existe uma classe com o nome: " + entity.getNome());
            }
        }
    }
}
