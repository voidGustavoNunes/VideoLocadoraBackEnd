package github.com.voidGustavoNunes.projetoLocadora.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

import github.com.voidGustavoNunes.projetoLocadora.model.enums.Tipo;

@Data
public class ItemDTO {

    @NotNull(message = "Número de série é obrigatório")
    private Integer numeroSerie;

    @NotNull(message = "Data de aquisição é obrigatória")
    private LocalDate dataAquisicao;

    @NotNull(message = "ID do título é obrigatório")
    private Long tituloId;

    @NotNull(message = "Tipo é obrigatório")
    @Size(min = 3, max = 10, message = "Tipo deve ter entre 3 e 10 caracteres")
    private Tipo tipo;

}
