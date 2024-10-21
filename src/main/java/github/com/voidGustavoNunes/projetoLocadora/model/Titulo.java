package github.com.voidGustavoNunes.projetoLocadora.model;


import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name="titulo")
public class Titulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotNull(message = "O nome não pode ser menos que 1 caractere e maior que 100.")
    @Size(min = 1, max = 100)
    private String nome;

    @ManyToMany
    @JoinTable(
        name = "titulo_ator", 
        joinColumns = @JoinColumn(name = "titulo_id"), 
        inverseJoinColumns = @JoinColumn(name = "ator_id")
    )
    @NotEmpty(message = "Um título deve ter pelo menos um ator.")
    private List<Ator> atores;

    @ManyToOne 
    @JoinColumn(name = "diretor_id")
    @NotNull(message= "Diretor não pode ser vazio.")
    private Diretor diretor;

    @NotNull(message= "Ano não pode ser vazio.")
    @Min(value = 1800, message = "O ano deve ser maior ou igual a 1800.")
    private Integer ano;

    @NotNull(message= "Sinopse não pode ser vazia.")
    private String sinopse;

    @NotNull(message= "Um título deve possui categoria.")
    private String categoria;

    @ManyToOne
    @JoinColumn(name = "classe_id")
    @NotNull(message= "Classe não pode ser vazia.")
    private Classe classe;
}
