package github.com.voidGustavoNunes.projetoLocadora.model;

import java.time.LocalDate;

import github.com.voidGustavoNunes.projetoLocadora.model.enums.Sexo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.persistence.*;


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

    @NotNull(message = "O cpf é obrigatório.")
    @Size(min = 11, max = 11, message = "O CPF deve ter 11 dígitos.")
    private String cpf;

    @NotNull(message = "O campo sexo não pode ser nulo.")
    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @NotNull(message = "A data de nascimento é obrigatória.")
    private LocalDate dataNascimento;

    // @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    // private List<Dependente> dependentes;

    private boolean ativo;
}
