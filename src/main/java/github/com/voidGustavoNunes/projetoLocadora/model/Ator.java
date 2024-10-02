package github.com.voidGustavoNunes.projetoLocadora.model;

import jakarta.persistence.Entity;
import lombok.Data;

import jakarta.persistence.Table;

@Data
@Entity
@Table(name="ator")
public class Ator extends GenericModel{
    
    private String nome;
}
