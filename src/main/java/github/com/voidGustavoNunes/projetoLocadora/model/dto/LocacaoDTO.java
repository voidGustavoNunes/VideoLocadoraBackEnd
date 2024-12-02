package github.com.voidGustavoNunes.projetoLocadora.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocacaoDTO {
    @NotNull
    private Long clienteId;

    @NotNull
    private Long itemId;

    @NotNull
    private LocalDate dataDevolucaoPrevista;

    @NotNull
    private BigDecimal valor;

    private BigDecimal multa;

}
