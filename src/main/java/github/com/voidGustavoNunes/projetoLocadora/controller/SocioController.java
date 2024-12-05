package github.com.voidGustavoNunes.projetoLocadora.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import github.com.voidGustavoNunes.projetoLocadora.model.Socio;
import github.com.voidGustavoNunes.projetoLocadora.service.GenericService;
import github.com.voidGustavoNunes.projetoLocadora.service.LocacaoService;
import github.com.voidGustavoNunes.projetoLocadora.service.SocioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Validated
@RestController
@RequestMapping("/socios")
@Tag(name = "Socios", description = "Operações relacionadas aos socios")
public class SocioController extends GenericController<Socio>{

    @Autowired
    private SocioService socioService;
    
    protected SocioController(GenericService<Socio> genericService) {
        super(genericService);
    }

    @Operation(summary = "Socios Ordenados", description = "Método que gera uma lista de sócios e os ordena em ordem alfabética")
    @GetMapping("/socios")
    public List<Socio> getSociosOrdenados() {
        return socioService.getAllSociosOrdenados();
    }

}
