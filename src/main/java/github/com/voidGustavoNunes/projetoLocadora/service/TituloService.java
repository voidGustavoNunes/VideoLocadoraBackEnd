package github.com.voidGustavoNunes.projetoLocadora.service;

import java.time.Year;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import github.com.voidGustavoNunes.exception.RegistroNotFoundException;
import github.com.voidGustavoNunes.projetoLocadora.model.Titulo;
import github.com.voidGustavoNunes.projetoLocadora.repository.TituloRepository;

@Service
public class TituloService extends GenericServiceImpl<Titulo, TituloRepository>{

    protected TituloService(TituloRepository repository) {
        super(repository);

    }

    @Override
    public void saveValidation(Titulo entity) throws RegistroNotFoundException {
        if (entity.getId() != null) {
            // Se o id não for nulo, estamos atualizando um registro existente
            Titulo existingTitulo = repository.findById(entity.getId())
                    .orElseThrow(() -> new RegistroNotFoundException(entity.getId()));
            
            // Verifica se o nome está sendo alterado
            if (!existingTitulo.getNome().equals(entity.getNome())) {
                // Verifica se o novo nome já existe
                if (repository.existsByNome(entity.getNome())) {
                    throw new IllegalArgumentException("Já existe um Titulo com o nome: " + entity.getNome());
                }
            }
        } else {
            // Se o id for nulo, estamos criando um novo registro
            if (repository.existsByNome(entity.getNome())) {
                throw new IllegalArgumentException("Já existe um Titulo com o nome: " + entity.getNome());
            }
        }
        if (entity.getAno() != null) {
            int anoAtual = Year.now().getValue();
            if (entity.getAno() > anoAtual) {
                throw new IllegalArgumentException("O ano não pode ser maior que o ano atual: " + anoAtual);
            }
        }
    }

    public List<Titulo> getAllTitulosOrdenados() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }
    
}
