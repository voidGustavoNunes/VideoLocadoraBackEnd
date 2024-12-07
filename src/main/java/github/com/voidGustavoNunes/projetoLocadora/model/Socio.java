package github.com.voidGustavoNunes.projetoLocadora.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Table(name="socio")
@Entity
public class Socio extends Cliente {

    @NotNull(message = "O endereço não pode ser vazio.")
    private String endereco;

    @NotNull(message = "O telefone não pode ser vazio.")
    private String telefone;

    @Column(name="dependentes")
    @OneToMany(mappedBy = "socioId") 
    private List<Dependente> dependentes;

}
