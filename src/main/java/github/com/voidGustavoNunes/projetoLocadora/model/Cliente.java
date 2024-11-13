package github.com.voidGustavoNunes.projetoLocadora.model;

import java.time.LocalDate;
import java.util.List;

import github.com.voidGustavoNunes.projetoLocadora.model.enums.Sexo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name="cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotNull(message = "O nome não pode ser menos que 1 caractere e maior que 100.")
    @Size(min = 1, max = 100)
    private String nome;

    @NotNull(message = "O endereço não pode ser vazio.")
    private String endereco;

    @NotNull(message = "O cpf é obrigatório.")
    @Size(min = 11, max = 11, message = "O CPF deve ter 11 dígitos.")
    private String cpf;

    @NotNull(message = "O campo sexo não pode ser nulo.")
    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @NotNull(message = "A data de nascimento é obrigatória.")
    private LocalDate dataNascimento;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Dependente> dependentes;
}
