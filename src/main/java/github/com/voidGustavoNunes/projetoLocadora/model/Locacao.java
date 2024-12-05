package github.com.voidGustavoNunes.projetoLocadora.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import github.com.voidGustavoNunes.projetoLocadora.model.enums.StatusLocacao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(name = "locacao", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"item_id", "data_locacao"}, name = "unique_locacao_item_data")
})
public class Locacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "socio_id", nullable = false)
    private Socio socio;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "data_locacao", nullable = false)
    private LocalDate dataLocacao;

    @Column(name = "data_devolucao_prevista", nullable = false)
    private LocalDate dataDevolucaoPrevista;

    @Column(name = "data_devolucao_efetiva")
    private LocalDate dataDevolucaoEfetiva;

    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusLocacao status;
}
