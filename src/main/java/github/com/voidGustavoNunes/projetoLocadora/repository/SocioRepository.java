package github.com.voidGustavoNunes.projetoLocadora.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import github.com.voidGustavoNunes.projetoLocadora.model.Socio;

@Repository
@CrossOrigin("http://localhost:4200")
@RepositoryRestResource(collectionResourceRel = "socio", path="socios", exported = false)
public interface SocioRepository extends JpaRepository<Socio, Long> {

    List<Socio> findAll(Sort sort);
    boolean existsByNome(String nome);

    

}
