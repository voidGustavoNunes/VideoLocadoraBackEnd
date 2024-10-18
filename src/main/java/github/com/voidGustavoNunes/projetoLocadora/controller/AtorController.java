package github.com.voidGustavoNunes.projetoLocadora.controller;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import github.com.voidGustavoNunes.projetoLocadora.model.Ator;
import github.com.voidGustavoNunes.projetoLocadora.service.AtorService;
import io.swagger.v3.oas.annotations.tags.Tag;

@Validated
@RestController
@RequestMapping("/atores")
@Tag(name = "Atores", description = "Operações relacionadas aos atores")
public class AtorController extends GenericController<Ator>{

    protected AtorController(AtorService atorService) {
        super(atorService);
    }

    @Autowired
    private AtorService atorService;

    // Endpoint que retorna os atores em ordem alfabética
    @GetMapping("/atores")
    public List<Ator> getAtoresOrdenados() {
        return atorService.getAllAtoresOrdenados();
    }
}
