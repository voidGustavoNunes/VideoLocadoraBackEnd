package github.com.voidGustavoNunes.projetoLocadora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import github.com.voidGustavoNunes.projetoLocadora.model.Ator;
import github.com.voidGustavoNunes.projetoLocadora.model.Diretor;
import github.com.voidGustavoNunes.projetoLocadora.service.AtorService;
import github.com.voidGustavoNunes.projetoLocadora.service.DiretorService;

import java.util.List;

@Controller
public class DiretorController {
    @Autowired
    private DiretorService diretorService;

    // Endpoint que retorna os diretores em ordem alfabética
    @GetMapping("/diretores")
    public List<Diretor> getDiretoresOrdenados() {
        return diretorService.getAllDiretoresOrdenados();
    }
}
