package github.com.voidGustavoNunes.projetoLocadora.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name="dependente")
public class Dependente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotNull(message = "O nome não pode ser menos que 1 caractere e maior que 100.")
    @Size(min = 1, max = 100)
    private String nome;

    @NotNull(message = "O campo sexo não pode ser nulo.")
    private boolean sexo;
    
    @NotNull(message = "A data de nascimento é obrigatória.")
    private LocalDate dataNascimento;

    @ManyToOne 
    @JoinColumn(name = "cliente_id", nullable = false)
    @NotNull(message = "Cliente não pode ser vazio")
    private Cliente cliente;
}
