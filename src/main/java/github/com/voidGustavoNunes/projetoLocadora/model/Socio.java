package github.com.voidGustavoNunes.projetoLocadora.model;

import java.util.List;

import jakarta.persistence.CascadeType;
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

    @OneToMany(mappedBy = "socio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dependente> dependentes;

 
    public void adicionarDependente(Dependente dependente) {
        long dependentesAtivos = this.dependentes.stream().filter(Dependente::isAtivo).count();
        if (dependentesAtivos >= 3) {
            throw new IllegalStateException("O sócio já possui três dependentes ativos.");
        }
        dependente.setSocio(this);
        dependentes.add(dependente);
    }

}
