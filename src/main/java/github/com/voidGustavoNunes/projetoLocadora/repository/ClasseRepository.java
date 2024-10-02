package github.com.voidGustavoNunes.projetoLocadora.repository;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@CrossOrigin("http://localhost:4200")
@RepositoryRestResource(collectionResourceRel = "classe", path="classes")
public interface ClasseRepository {
    
}
