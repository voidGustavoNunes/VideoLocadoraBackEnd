package github.com.voidGustavoNunes.projetoLocadora.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import github.com.voidGustavoNunes.projetoLocadora.model.Ator;
import github.com.voidGustavoNunes.projetoLocadora.repository.AtorRepository;

@Service
public class AtorService extends GenericServiceImpl<Ator, AtorRepository >{

    
    protected AtorService(AtorRepository repository) {
        super(repository);
        //TODO Auto-generated constructor stub
    }


    @Autowired
    private AtorRepository atorRepository;


    // Retorna a lista de atores ordenada pelo nome
    public List<Ator> getAllAtoresOrdenados() {
        return atorRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }
}
