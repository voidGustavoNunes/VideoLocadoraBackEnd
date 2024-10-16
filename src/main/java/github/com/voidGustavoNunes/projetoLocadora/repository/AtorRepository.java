package github.com.voidGustavoNunes.projetoLocadora.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;

import github.com.voidGustavoNunes.projetoLocadora.model.Ator;

@Repository
@CrossOrigin("http://localhost:4200")
@RepositoryRestResource(collectionResourceRel = "ator", path="atores")
public interface AtorRepository extends JpaRepository<Ator, Long>{

    // Método que retorna todos os atores em ordem alfabética
    List<Ator> findAll(Sort sort);
}
