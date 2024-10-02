package github.com.voidGustavoNunes.projetoLocadora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import github.com.voidGustavoNunes.projetoLocadora.model.Ator;

@CrossOrigin("http://localhost:4200")
@RepositoryRestResource(collectionResourceRel = "ator", path="atores")
public interface AtorRepository extends JpaRepository<Ator, Long>{
    
}
