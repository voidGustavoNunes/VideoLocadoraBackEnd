package github.com.voidGustavoNunes.projetoLocadora.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import github.com.voidGustavoNunes.projetoLocadora.model.Locacao;
import github.com.voidGustavoNunes.projetoLocadora.model.enums.StatusLocacao;


@Repository
@CrossOrigin("http://localhost:4200")
@RepositoryRestResource(collectionResourceRel = "locacao", path="locacoes", exported = false)
public interface LocacaoRepository extends JpaRepository<Locacao, Long> {

    boolean findByClienteId(Long id);

    // Verifica se já existe uma locação para o item na data especificada
    Optional<Locacao> findByItemIdAndDataLocacao(Long itemId, LocalDate dataLocacao);

    // Verifica se um cliente possui locações em aberto
    boolean existsByClienteIdAndStatus(Long clienteId, StatusLocacao status);


    @Query("SELECT COUNT(l) > 0 FROM Locacao l WHERE l.cliente.id = :clienteId AND l.status = 'EM_ABERTO'")
    boolean clientePossuiDebitos(@Param("clienteId") Long clienteId);

    @Query("SELECT COUNT(l) = 0 FROM Locacao l WHERE l.item.id = :itemId AND l.status = 'EM_ABERTO' AND l.dataDevolucaoPrevista >= :dataAtual")
    boolean itemDisponivel(@Param("itemId") Long itemId, @Param("dataAtual") LocalDate dataAtual);

    Optional<Locacao> findByItemIdAndStatus(Long itemId, StatusLocacao status);

    Locacao findByItemNumeroSerie(Integer numeroSerie);


}
