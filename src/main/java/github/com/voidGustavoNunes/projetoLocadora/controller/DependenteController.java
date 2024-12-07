package github.com.voidGustavoNunes.projetoLocadora.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import github.com.voidGustavoNunes.projetoLocadora.model.Dependente;
import github.com.voidGustavoNunes.projetoLocadora.service.DependenteService;
import github.com.voidGustavoNunes.projetoLocadora.service.GenericService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Validated
@RestController
@RequestMapping("/dependentes")
@Tag(name = "Dependentes", description = "Operações relacionadas aos Dependentes")
public class DependenteController extends GenericController<Dependente>{
    
    protected DependenteController(GenericService<Dependente> genericService) {
        super(genericService);
    }

    @Autowired
    private DependenteService dependenteService;

    @Operation(summary = "Dependentes Ordenados", description = "Método que gera uma lista de dependentes e os ordena em ordem alfabética")
    // Endpoint que retorna os diretores em ordem alfabética
    @GetMapping("/dependentes")
    public List<Dependente> getDependentesOrdenados() {
        return dependenteService.getAllDependentesOrdenados();
    }


}
