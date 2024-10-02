package github.com.voidGustavoNunes.projetoLocadora.repository;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("http://localhost:4200")
@RepositoryRestResource(collectionResourceRel = "diretor", path="diretores")
public interface DiretorRepository {
    
}
