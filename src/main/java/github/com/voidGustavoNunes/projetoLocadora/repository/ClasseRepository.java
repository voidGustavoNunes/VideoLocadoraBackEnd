package github.com.voidGustavoNunes.projetoLocadora.repository;

import org.springframework.web.bind.annotation.CrossOrigin;

import github.com.voidGustavoNunes.projetoLocadora.model.Classe;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@CrossOrigin("http://localhost:4200")
@RepositoryRestResource(collectionResourceRel = "classe", path="classes", exported = false)
public interface ClasseRepository extends JpaRepository<Classe, Long>{


    List<Classe> findAll(Sort sort);
    boolean existsByNome(String nome);

}
