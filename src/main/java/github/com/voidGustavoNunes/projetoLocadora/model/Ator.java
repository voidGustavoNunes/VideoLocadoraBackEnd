package github.com.voidGustavoNunes.projetoLocadora.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import jakarta.persistence.Table;

@Data
@Entity
@Table(name="ator")
public class Ator{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    
    private String nome;
}
