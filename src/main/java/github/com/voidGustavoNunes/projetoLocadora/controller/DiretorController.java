package github.com.voidGustavoNunes.projetoLocadora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import github.com.voidGustavoNunes.projetoLocadora.model.Ator;
import github.com.voidGustavoNunes.projetoLocadora.model.Diretor;
import github.com.voidGustavoNunes.projetoLocadora.service.AtorService;
import github.com.voidGustavoNunes.projetoLocadora.service.DiretorService;
import github.com.voidGustavoNunes.projetoLocadora.service.GenericService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/diretores")
public class DiretorController extends GenericController<Diretor>{

    protected DiretorController(GenericService<Diretor> genericService) {
        super(genericService);
    }

    @Autowired
    private DiretorService diretorService;

    // Endpoint que retorna os diretores em ordem alfabética
    @GetMapping("/diretores")
    public List<Diretor> getDiretoresOrdenados() {
        return diretorService.getAllDiretoresOrdenados();
    }
}
