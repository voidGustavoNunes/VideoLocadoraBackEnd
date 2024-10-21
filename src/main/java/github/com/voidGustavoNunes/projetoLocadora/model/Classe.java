package github.com.voidGustavoNunes.projetoLocadora.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name="classe")
public class Classe{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;


    @NotNull(message = "O nome não pode ser menos que 1 caractere e maior que 100.")
    @Size(min = 1, max = 100)
    private String nome;

    private LocalDate dataDevolucao;

    private BigDecimal valor;
}
