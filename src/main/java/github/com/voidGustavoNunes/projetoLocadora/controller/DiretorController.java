package github.com.voidGustavoNunes.projetoLocadora.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import github.com.voidGustavoNunes.projetoLocadora.model.Diretor;
import github.com.voidGustavoNunes.projetoLocadora.service.DiretorService;
import github.com.voidGustavoNunes.projetoLocadora.service.GenericService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Validated
@RestController
@RequestMapping("/diretores")
@Tag(name = "Diretores", description = "Operações relacionadas aos diretores")
public class DiretorController extends GenericController<Diretor>{

    protected DiretorController(GenericService<Diretor> genericService) {
        super(genericService);
    }

    @Autowired
    private DiretorService diretorService;

    @Operation(summary = "Diretores Ordenados", description = "Método que gera uma lista de diretores e os ordena em ordem alfabética")
    // Endpoint que retorna os diretores em ordem alfabética
    @GetMapping("/diretores")
    public List<Diretor> getDiretoresOrdenados() {
        return diretorService.getAllDiretoresOrdenados();
    }
}
