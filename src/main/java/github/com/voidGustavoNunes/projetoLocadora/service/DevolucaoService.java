package github.com.voidGustavoNunes.projetoLocadora.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import github.com.voidGustavoNunes.exception.BusinessException;
import github.com.voidGustavoNunes.exception.RegistroNotFoundException;
import github.com.voidGustavoNunes.projetoLocadora.model.Item;
import github.com.voidGustavoNunes.projetoLocadora.model.Locacao;
import github.com.voidGustavoNunes.projetoLocadora.model.enums.StatusLocacao;
import github.com.voidGustavoNunes.projetoLocadora.repository.ItemRepository;
import github.com.voidGustavoNunes.projetoLocadora.repository.LocacaoRepository;

@Service
public class DevolucaoService {

    @Autowired
    private LocacaoRepository locacaoRepository;

    @Autowired
    private ItemRepository itemRepository;

    /**
     * Realiza a devolução de um item.
     *
     * @param numeroSerie Número de série do item sendo devolvido.
     * @return Locação atualizada com as informações da devolução.
     */
    public Locacao efetuarDevolucao(Integer numeroSerie) {
        // Busca o item pelo número de série
        Item item = itemRepository.findByNumeroSerie(numeroSerie)
                .orElseThrow(() -> new RegistroNotFoundException("Item não encontrado com o número de série: " + numeroSerie));

        // Busca a locação vigente para o item
        Locacao locacao = locacaoRepository.findByItemIdAndStatus(item.getId(), StatusLocacao.ABERTA)
                .orElseThrow(() -> new RegistroNotFoundException("O item informado não possui uma locação vigente."));

        // Verifica se a locação já foi paga
        if (locacao.getStatus() == StatusLocacao.PAGA) {
            throw new BusinessException( locacao.getId(), "Locação já foi paga e devolvida anteriormente");
        }

        // Valida se a data de devolução está no futuro
        LocalDate dataAtual = LocalDate.now();
        if (dataAtual.isAfter(locacao.getDataDevolucaoPrevista())) {
            // Calcula a multa caso a devolução seja atrasada
            BigDecimal multa = calcularMulta(locacao);
            locacao.setValor(locacao.getValor().add(multa));  // Adiciona a multa ao valor da locação
        }

        // Atualiza a locação com a data de devolução efetiva e status PAGA
        locacao.setDataDevolucaoEfetiva(dataAtual);
        locacao.setStatus(StatusLocacao.PAGA);
        locacaoRepository.save(locacao);

        return locacao;
    }

    /**
     * Calcula a multa devida para uma locação, se houver atraso.
     *
     * @param locacao Locação a ser analisada.
     * @return Valor da multa.
     */
    public BigDecimal calcularMulta(Locacao locacao) {
        if (LocalDate.now().isAfter(locacao.getDataDevolucaoPrevista())) {
            long diasAtraso = java.time.temporal.ChronoUnit.DAYS.between(
                    locacao.getDataDevolucaoPrevista(),
                    LocalDate.now());
            return BigDecimal.valueOf(5).multiply(BigDecimal.valueOf(diasAtraso)); // Multa de R$ 5 por dia de atraso
        }
        return BigDecimal.ZERO; // Sem multa caso não haja atraso
    }
}
