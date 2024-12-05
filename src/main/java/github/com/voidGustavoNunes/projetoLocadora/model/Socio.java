package github.com.voidGustavoNunes.projetoLocadora.model;

import java.util.List;

import jakarta.persistence.CascadeType;
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

    @OneToMany(mappedBy = "socio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dependente> dependentes;

}
