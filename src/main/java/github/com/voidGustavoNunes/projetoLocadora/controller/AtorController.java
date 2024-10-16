package github.com.voidGustavoNunes.projetoLocadora.controller;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import github.com.voidGustavoNunes.projetoLocadora.model.Ator;
import github.com.voidGustavoNunes.projetoLocadora.service.AtorService;

@Controller
public class AtorController{
    @Autowired
    private AtorService atorService;

    // Endpoint que retorna os atores em ordem alfabética
    @GetMapping("/atores")
    public List<Ator> getAtoresOrdenados() {
        return atorService.getAllAtoresOrdenados();
    }
}
