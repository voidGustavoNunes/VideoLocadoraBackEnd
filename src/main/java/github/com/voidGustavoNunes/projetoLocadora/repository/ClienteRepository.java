package github.com.voidGustavoNunes.projetoLocadora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import github.com.voidGustavoNunes.projetoLocadora.model.Cliente;

@Repository
@CrossOrigin("http://localhost:4200")
@RepositoryRestResource(collectionResourceRel = "item", path="items", exported = false)
public interface ClienteRepository extends JpaRepository<Cliente, Long>{
    
}
