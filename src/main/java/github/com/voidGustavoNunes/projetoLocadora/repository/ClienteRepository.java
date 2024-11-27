package github.com.voidGustavoNunes.projetoLocadora.repository;

import github.com.voidGustavoNunes.projetoLocadora.model.Cliente;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@CrossOrigin("http://localhost:4200")
@RepositoryRestResource(collectionResourceRel = "cliente", path="clientes", exported = false)
public interface ClienteRepository extends JpaRepository<Cliente, Long>{
    
    List<Cliente> findAll(Sort sort);
    boolean existsByNome(String nome);

}
