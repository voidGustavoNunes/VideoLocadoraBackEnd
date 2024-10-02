package github.com.voidGustavoNunes.projetoLocadora.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="classe")
public class Classe extends GenericModel{
    private String nome;

    private LocalDate dataDevolucao;

    private BigDecimal valor;
}
