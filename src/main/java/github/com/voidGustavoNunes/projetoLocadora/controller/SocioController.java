package github.com.voidGustavoNunes.projetoLocadora.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import github.com.voidGustavoNunes.projetoLocadora.model.Socio;
import github.com.voidGustavoNunes.projetoLocadora.service.GenericService;
import io.swagger.v3.oas.annotations.tags.Tag;

@Validated
@RestController
@RequestMapping("/socios")
@Tag(name = "Socios", description = "Operações relacionadas aos socios")
public class SocioController extends GenericController<Socio>{
    
    protected SocioController(GenericService<Socio> genericService) {
        super(genericService);
    }
}
