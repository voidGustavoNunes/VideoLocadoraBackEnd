package github.com.voidGustavoNunes.projetoLocadora.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@Entity
@Table(name="ator")
public class Ator{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    
    @NotNull(message = "O nome não pode ser menos que 1 caractere e maior que 100.")
    @Size(min = 1, max = 100)
    private String nome;

    @JsonIgnore
    @ManyToMany(mappedBy = "atores")
    private List<Titulo> titulos = new ArrayList<>();
}
