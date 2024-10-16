package github.com.voidGustavoNunes.projetoLocadora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import github.com.voidGustavoNunes.projetoLocadora.model.Ator;
import github.com.voidGustavoNunes.projetoLocadora.model.Classe;
import github.com.voidGustavoNunes.projetoLocadora.service.AtorService;
import github.com.voidGustavoNunes.projetoLocadora.service.ClasseService;

import java.util.List;

@Controller
public class ClasseController {
    
    @Autowired
    private ClasseService classeService;

    @GetMapping("/classes")
    public List<Classe> getClassesOrdenados() {
        return classeService.getAllClassesOrdenados();
    }
}
