package github.com.voidGustavoNunes.projetoLocadora.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import github.com.voidGustavoNunes.projetoLocadora.model.Ator;
import github.com.voidGustavoNunes.projetoLocadora.model.Titulo;

@Repository
@CrossOrigin("http://localhost:4200")
@RepositoryRestResource(collectionResourceRel = "titulo", path="titulos", exported = false)
public interface TituloRepository extends JpaRepository<Titulo,Long>{
    List<Titulo> findAll(Sort sort);

    boolean existsByNome(String nome);


    List<Titulo> findByNomeContainingIgnoreCase(String nome);

    List<Titulo> findByCategoriaIgnoreCase(String categoria);

    @Query("SELECT t FROM Titulo t JOIN t.atores a WHERE a.nome = :nomeAtor")
    List<Titulo> findByAtorNome(@Param("nomeAtor") String nomeAtor);

    @Query("SELECT t.id, t.nome, COUNT(i) AS quantidade FROM Titulo t " +
       "LEFT JOIN Item i ON t.id = i.titulo.id " +
       "GROUP BY t.id, t.nome")
    List<Object[]> getTitulosComQuantidade();


}
