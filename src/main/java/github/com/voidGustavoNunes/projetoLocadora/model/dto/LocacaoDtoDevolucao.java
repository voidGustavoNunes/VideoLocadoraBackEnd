package github.com.voidGustavoNunes.projetoLocadora.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import github.com.voidGustavoNunes.projetoLocadora.model.Item;
import github.com.voidGustavoNunes.projetoLocadora.model.Socio;
import github.com.voidGustavoNunes.projetoLocadora.model.enums.StatusLocacao;
import lombok.Data;

@Data
public class LocacaoDtoDevolucao {
    private Long id;
    private Socio socio;
    private Item item;
    private LocalDate dataLocacao;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoEfetiva;
    private BigDecimal valor;
    private StatusLocacao status;
    private BigDecimal multa; 
}
