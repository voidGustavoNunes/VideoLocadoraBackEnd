package github.com.voidGustavoNunes.projetoLocadora.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import github.com.voidGustavoNunes.projetoLocadora.model.Locacao;
import github.com.voidGustavoNunes.projetoLocadora.service.GenericService;
import io.swagger.v3.oas.annotations.tags.Tag;

@Validated
@RestController
@RequestMapping("/locacoes")
@Tag(name = "Locacoes", description = "Operações relacionadas as locacoes")
public class LocacaoController extends GenericController<Locacao>{
    
    protected LocacaoController(GenericService<Locacao> genericService) {
        super(genericService);
    }


   
}
