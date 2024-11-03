package github.com.voidGustavoNunes.projetoLocadora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import github.com.voidGustavoNunes.projetoLocadora.model.Item;
import github.com.voidGustavoNunes.projetoLocadora.model.Titulo;
import github.com.voidGustavoNunes.projetoLocadora.model.dto.ItemDTO;
import github.com.voidGustavoNunes.projetoLocadora.repository.TituloRepository;
import github.com.voidGustavoNunes.projetoLocadora.service.ItemService;
import jakarta.validation.Valid;

import java.util.List;

@Validated
@RestController
@RequestMapping("/itens")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private TituloRepository tituloRepository;

    @GetMapping
    public List<Item> listar() {
        return itemService.listar();
    }

    @GetMapping("/{id}")
    public Item buscarPorId(@PathVariable Long id) {
        return itemService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Item criar(@RequestBody @Valid ItemDTO itemDTO) {
        Item item = mapToEntity(itemDTO);
        return itemService.criar(item);
    }

    @PutMapping("/{id}")
    public Item atualizar(@PathVariable Long id, @RequestBody @Valid ItemDTO itemDTO) {
        Item item = mapToEntity(itemDTO);
        return itemService.atualizar(id, item);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        itemService.excluir(id);
    }


    private Item mapToEntity(ItemDTO itemDTO) {
        Item item = new Item();
        item.setNumeroSerie(itemDTO.getNumeroSerie());
        item.setDataAquisicao(itemDTO.getDataAquisicao());

        Titulo titulo = tituloRepository.findById(itemDTO.getTituloId())
                .orElseThrow(() -> new IllegalArgumentException("Título não encontrado para o ID fornecido."));
        item.setTitulo(titulo);

        item.setTipo(itemDTO.getTipo());
        return item;
    }
}
