package github.com.voidGustavoNunes.projetoLocadora.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name="dependente")
public class Dependente extends Cliente{
    
    @NotNull
    private Long socioId;


    
}
