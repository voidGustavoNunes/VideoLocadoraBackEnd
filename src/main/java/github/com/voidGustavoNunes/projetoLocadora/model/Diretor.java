package github.com.voidGustavoNunes.projetoLocadora.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="diretor")
public class Diretor extends GenericModel{
    private String nome;
}
