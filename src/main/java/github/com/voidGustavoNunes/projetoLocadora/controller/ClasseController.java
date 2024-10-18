package github.com.voidGustavoNunes.projetoLocadora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import github.com.voidGustavoNunes.projetoLocadora.model.Classe;
import github.com.voidGustavoNunes.projetoLocadora.service.ClasseService;
import github.com.voidGustavoNunes.projetoLocadora.service.GenericService;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Validated
@RestController
@RequestMapping("/classes")
@Tag(name = "Classes", description = "Operações relacionadas às classes")
public class ClasseController extends GenericController<Classe>{
    
    protected ClasseController(GenericService<Classe> genericService) {
        super(genericService);
    }

    @Autowired
    private ClasseService classeService;

    @GetMapping("/classes")
    public List<Classe> getClassesOrdenados() {
        return classeService.getAllClassesOrdenados();
    }
}
