package github.com.voidGustavoNunes.projetoLocadora.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

import github.com.voidGustavoNunes.projetoLocadora.model.Ator;
import github.com.voidGustavoNunes.projetoLocadora.model.Classe;
import github.com.voidGustavoNunes.projetoLocadora.repository.AtorRepository;
import github.com.voidGustavoNunes.projetoLocadora.repository.ClasseRepository;

@Service
public class ClasseService extends GenericServiceImpl<Classe, ClasseRepository >{

    protected ClasseService(ClasseRepository repository) {
        super(repository);
    }

    @Autowired
    private ClasseRepository classeRepository;
    
        // Retorna a lista de classes ordenada pelo nome
    public List<Classe> getAllClassesOrdenados() {
        return classeRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }
}
