package github.com.voidGustavoNunes.projetoLocadora.model;

import java.time.LocalDate;

import github.com.voidGustavoNunes.projetoLocadora.model.enums.Tipo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name="item")
public class Item {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotNull(message = "Numero de série não pode ser vazio")
    private Integer numeroSerie;

    @ManyToOne 
    @JoinColumn(name = "titulo_id")
    @NotNull(message = "Tìtulo não pode ser vazio")
    private Titulo titulo;

    @NotNull(message = "Data de aquisição não pode ser vazio")
    private LocalDate dataAquisicao;

    @NotNull(message = "Tipo não pode ser vazio")
    private Tipo tipo;
}
