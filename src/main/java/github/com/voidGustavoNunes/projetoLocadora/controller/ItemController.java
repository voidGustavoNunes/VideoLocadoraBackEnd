package github.com.voidGustavoNunes.projetoLocadora.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import github.com.voidGustavoNunes.projetoLocadora.model.Item;
import github.com.voidGustavoNunes.projetoLocadora.service.GenericService;
import github.com.voidGustavoNunes.projetoLocadora.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Validated
@RestController
@RequestMapping("/itens")
@Tag(name = "Itens", description = "Operações relacionadas aos Itens")
public class ItemController extends GenericController<Item>{

    protected ItemController(GenericService<Item> genericService) {
        super(genericService);
    }

    @Autowired 
    private ItemService itemService;
    

    @Operation(summary = "Itens Ordenados", description = "Método que gera uma lista de itens e os ordena em ordem alfabética")
    @GetMapping("/itens")
    public List<Item> getItensOrdenados() {
        return itemService.getAllItensOrdenados();
    }
}
