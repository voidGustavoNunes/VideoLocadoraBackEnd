package github.com.voidGustavoNunes.projetoLocadora.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="dependente")
public class Dependente extends Cliente{
    
    @ManyToOne
    @JoinColumn(name = "socio_id", nullable = false)
    private Socio socio;
}
