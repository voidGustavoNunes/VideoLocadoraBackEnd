package github.com.voidGustavoNunes.projetoLocadora.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import github.com.voidGustavoNunes.projetoLocadora.model.Dependente;

@Repository
@CrossOrigin("http://localhost:4200")
@RepositoryRestResource(collectionResourceRel = "dependente", path="dependentes", exported = false)
public interface DependenteRepository extends JpaRepository<Dependente, Long>{
    
    List<Dependente> findAll(Sort sort);
    boolean existsByNome(String nome);

    @Query("SELECT d FROM Dependente d WHERE d.socio.id = :socioId AND d.ativo = true")
    List<Dependente> findByIdWithActiveDependents(@Param("socioId") Long socioId);
}
