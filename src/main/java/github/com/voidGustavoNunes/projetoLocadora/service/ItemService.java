package github.com.voidGustavoNunes.projetoLocadora.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import github.com.voidGustavoNunes.exception.RegistroNotFoundException;
import github.com.voidGustavoNunes.projetoLocadora.model.Classe;
import github.com.voidGustavoNunes.projetoLocadora.model.Item;
import github.com.voidGustavoNunes.projetoLocadora.model.Titulo;
import github.com.voidGustavoNunes.projetoLocadora.model.dto.ItemDTO;
import github.com.voidGustavoNunes.projetoLocadora.repository.ClasseRepository;
import github.com.voidGustavoNunes.projetoLocadora.repository.ItemRepository;
import github.com.voidGustavoNunes.projetoLocadora.repository.TituloRepository;

@Service
public class ItemService extends GenericServiceImpl<Item, ItemRepository>{

    @Autowired
    private TituloRepository tituloRepository;
    
    @Autowired
    private ClasseRepository classeRepository;

    protected ItemService(ItemRepository repository) {
            
        super(repository);
    }

    public List<Item> getAllItems() {
        return repository.findAll();
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
            throw new IllegalArgumentException("A data de aquisição deve ser hoje ou uma data anterior.");
        }

        if (entity.getTitulo().getId() == null) {
            throw new RegistroNotFoundException(entity.getTitulo().getId());
        }
    }

    public List<Item> getAllItensOrdenados() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "titulo.nome"));
    }

    // Método para obter o título associado a um item pelo ID do título
    public Titulo getTituloByItemId(Long tituloId) {
        return tituloRepository.findById(tituloId)
                .orElseThrow(() -> new RegistroNotFoundException(tituloId));
    }

    // Método para obter a classe associada ao título pelo ID da classe
    public Classe getClasseByTituloId(Long classeId) {
        return classeRepository.findById(classeId)
                .orElseThrow(() -> new RegistroNotFoundException(classeId));
    }

    // Método para obter a classe associada a um item diretamente
    public Classe getClasseByItemId(Long itemId) {
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new RegistroNotFoundException(itemId));
        Titulo titulo = item.getTitulo();
        if (titulo.getClasse() == null) {
            throw new RegistroNotFoundException(item.getId());
        }
        return titulo.getClasse();
    }
    
}
