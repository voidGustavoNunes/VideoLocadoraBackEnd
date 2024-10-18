package github.com.voidGustavoNunes.projetoLocadora.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

import github.com.voidGustavoNunes.exception.RegistroNotFoundException;
import github.com.voidGustavoNunes.projetoLocadora.model.Diretor;
import github.com.voidGustavoNunes.projetoLocadora.repository.DiretorRepository;

@Service
public class DiretorService extends GenericServiceImpl<Diretor, DiretorRepository >{
    
    protected DiretorService(DiretorRepository repository) {
        super(repository);
    }

        // Retorna a lista de diretores ordenada pelo nome
    public List<Diretor> getAllDiretoresOrdenados() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }


    @Override
    public void saveValidation(Diretor entity) throws RegistroNotFoundException {
        // Verifica se o diretor já existe no banco
        if (entity.getId() != null) {
            // Se o id não for nulo, estamos atualizando um registro existente
            Diretor existingDiretor = repository.findById(entity.getId())
                    .orElseThrow(() -> new RegistroNotFoundException(entity.getId()));
            
            // Verifica se o nome está sendo alterado
            if (!existingDiretor.getNome().equals(entity.getNome())) {
                // Verifica se o novo nome já existe
                if (repository.existsByNome(entity.getNome())) {
                    throw new IllegalArgumentException("Já existe um diretor com o nome: " + entity.getNome());
                }
            }
        } else {
            // Se o id for nulo, estamos criando um novo registro
            if (repository.existsByNome(entity.getNome())) {
                throw new IllegalArgumentException("Já existe um diretor com o nome: " + entity.getNome());
            }
        }
    }

    
}
