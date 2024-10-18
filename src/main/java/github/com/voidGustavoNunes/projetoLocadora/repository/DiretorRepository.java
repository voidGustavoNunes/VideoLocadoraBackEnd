package github.com.voidGustavoNunes.projetoLocadora.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;

import github.com.voidGustavoNunes.projetoLocadora.model.Diretor;

@Repository
@CrossOrigin("http://localhost:4200")
@RepositoryRestResource(collectionResourceRel = "diretor", path="diretores", exported = false)
public interface DiretorRepository extends JpaRepository<Diretor, Long>{
    

    List<Diretor> findAll(Sort sort);

    boolean existsByNome(String nome);
}
