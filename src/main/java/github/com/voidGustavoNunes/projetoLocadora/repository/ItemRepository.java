package github.com.voidGustavoNunes.projetoLocadora.repository;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import github.com.voidGustavoNunes.projetoLocadora.model.Item;
import github.com.voidGustavoNunes.projetoLocadora.model.Titulo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
@CrossOrigin("http://localhost:4200")
@RepositoryRestResource(collectionResourceRel = "item", path="items", exported = false)
public interface ItemRepository extends JpaRepository<Item, Long>{
    
    List<Item> findAll();
    boolean existsByTitulo(Titulo titulo);
    Optional<Item> findByNumeroSerie(String numeroSerie);
    List<Item> findByTitulo(Titulo titulo);

}
