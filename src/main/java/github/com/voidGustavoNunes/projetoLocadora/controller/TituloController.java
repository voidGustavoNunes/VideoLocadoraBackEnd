package github.com.voidGustavoNunes.projetoLocadora.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import github.com.voidGustavoNunes.projetoLocadora.model.Titulo;
import github.com.voidGustavoNunes.projetoLocadora.service.GenericService;
import github.com.voidGustavoNunes.projetoLocadora.service.TituloService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Validated
@RestController
@RequestMapping("/titulos")
@Tag(name = "Títulos", description = "Operações relacionadas aos títulos")
public class TituloController extends GenericController<Titulo>{

    protected TituloController(GenericService<Titulo> genericService) {
        super(genericService);
    }

    @Autowired
    private TituloService tituloService;
    

    @Operation(summary = "Titulos Ordenados", description = "Método que gera uma lista de titulos e os ordena em ordem alfabética")
    @GetMapping("/titulos")
    public List<Titulo> getTitulosOrdenados() {
        return tituloService.getAllTitulosOrdenados();
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar Titulos", description = "Método que busca títulos por nome, categoria ou ator")
    public ResponseEntity<List<Titulo>> buscarTitulos(
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) String categoria,
        @RequestParam(required = false) String ator
    ) {
        List<Titulo> titulos;

        if (nome != null && !nome.isEmpty()) {
            titulos = tituloService.buscarPorNome(nome);
        } else if (categoria != null && !categoria.isEmpty()) {
            titulos = tituloService.buscarPorCategoria(categoria);
        } else if (ator != null && !ator.isEmpty()) {
            titulos = tituloService.buscarPorAtor(ator);
        } else {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(titulos);
    }

    @GetMapping("/titulos-com-quantidade")
    public List<Map<String, Object>> listarTitulosComQuantidade() {
        return tituloService.listarTitulosComQuantidade();
    }
    
}
