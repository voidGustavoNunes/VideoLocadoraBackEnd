package github.com.voidGustavoNunes.projetoLocadora.model;

import java.time.LocalDate;

import github.com.voidGustavoNunes.projetoLocadora.model.enums.Sexo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
@Entity
@Table(name="cliente")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotNull(message = "O nome não pode ser menos que 1 caractere e maior que 100.")
    @Size(min = 1, max = 100)
    private String nome;

    private String cpf;

    @NotNull(message = "O campo sexo não pode ser nulo.")
    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @NotNull(message = "A data de nascimento é obrigatória.")
    private LocalDate dataNascimento;

    // @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    // private List<Dependente> dependentes;

    private boolean ativo;

    private boolean ehDependente;
}
