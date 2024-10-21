package github.com.voidGustavoNunes.projetoLocadora.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import github.com.voidGustavoNunes.exception.RegistroNotFoundException;
import github.com.voidGustavoNunes.projetoLocadora.model.Item;
import github.com.voidGustavoNunes.projetoLocadora.repository.ItemRepository;

@Service
public class ItemService extends GenericServiceImpl<Item, ItemRepository>{

    protected ItemService(ItemRepository repository) {
        super(repository);
    }

    @Override
    public void saveValidation(Item entity) throws RegistroNotFoundException {
        // Validação para garantir que a dataDevolucao seja hoje ou uma data futura
        if (entity.getDataAquisicao() != null && entity.getDataAquisicao().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data de devolução deve ser hoje ou uma data anterior.");
        }
    }

    public List<Item> getAllItensOrdenados() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "titulo.nome"));
    }
    
}
