package github.com.voidGustavoNunes.projetoLocadora.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import github.com.voidGustavoNunes.exception.RegistroNotFoundException;
import github.com.voidGustavoNunes.projetoLocadora.model.Item;
import github.com.voidGustavoNunes.projetoLocadora.model.Titulo;
import github.com.voidGustavoNunes.projetoLocadora.model.dto.ItemDTO;
import github.com.voidGustavoNunes.projetoLocadora.repository.ItemRepository;
import github.com.voidGustavoNunes.projetoLocadora.repository.TituloRepository;

@Service
public class ItemService extends GenericServiceImpl<Item, ItemRepository>{

    @Autowired
    private TituloRepository tituloRepository;

    protected ItemService(ItemRepository repository) {
            
        super(repository);
    }

    public Item criar(ItemDTO itemDTO) {
        Item item = new Item();
        item.setNumeroSerie(itemDTO.getNumeroSerie());
        item.setDataAquisicao(itemDTO.getDataAquisicao());
        item.setTipo(itemDTO.getTipo());


        Titulo titulo = tituloRepository.findById(itemDTO.getTituloId())
                .orElseThrow(() -> new RegistroNotFoundException(itemDTO.getTituloId()));
        
        item.setTitulo(titulo);

        return repository.save(item);
    }

    @Override
    public void saveValidation(Item entity) throws RegistroNotFoundException {

        if (entity.getDataAquisicao() != null && entity.getDataAquisicao().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data de devolução deve ser hoje ou uma data anterior.");
        }

        if (entity.getTitulo().getId() == null) {
            throw new RegistroNotFoundException(entity.getTitulo().getId());
        }
    }

    public List<Item> getAllItensOrdenados() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "titulo.nome"));
    }
    
}
