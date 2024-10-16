package github.com.voidGustavoNunes.projetoLocadora.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

import github.com.voidGustavoNunes.projetoLocadora.model.Diretor;
import github.com.voidGustavoNunes.projetoLocadora.repository.DiretorRepository;

@Service
public class DiretorService{
    
    @Autowired
    private DiretorRepository diretorRepository;

        // Retorna a lista de diretores ordenada pelo nome
    public List<Diretor> getAllDiretoresOrdenados() {
        return diretorRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }

    
}
