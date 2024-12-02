package github.com.voidGustavoNunes.projetoLocadora.controller;

import github.com.voidGustavoNunes.projetoLocadora.model.Locacao;
import github.com.voidGustavoNunes.projetoLocadora.model.dto.LocacaoDTO;
import github.com.voidGustavoNunes.projetoLocadora.model.dto.LocacaoDtoDevolucao;
import github.com.voidGustavoNunes.projetoLocadora.service.DevolucaoService;
import github.com.voidGustavoNunes.projetoLocadora.service.LocacaoService;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devolucao")
public class DevolucaoController {

    @Autowired
    private LocacaoService locacaoService;

    @Autowired
    private DevolucaoService devolucaoService;

    // Endpoint para buscar locação com base no número de série do item
    @GetMapping("/locacao")
    public ResponseEntity<?> buscarLocacao(@RequestParam Integer numeroSerie) {
        Locacao locacao = locacaoService.buscarLocacaoPorNumeroSerie(numeroSerie);
        if (locacao == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item não está locado.");
        }

        // Calcular multa, se houver (não é necessário no método de busca)
        BigDecimal multa = devolucaoService.calcularMulta(locacao);
        locacao.setValor(locacao.getValor().add(multa)); // Se houver multa, soma ao valor

        // Cria o DTO e preenche os dados
        LocacaoDtoDevolucao locacaoDTO = new LocacaoDtoDevolucao();
        locacaoDTO.setMulta(BigDecimal.ZERO); //inicializa como 0 para ter um valor default

        locacaoDTO.setId(locacao.getId());
        locacaoDTO.setCliente(locacao.getCliente());
        locacaoDTO.setItem(locacao.getItem());
        locacaoDTO.setDataLocacao(locacao.getDataLocacao());
        locacaoDTO.setDataDevolucaoPrevista(locacao.getDataDevolucaoPrevista());
        locacaoDTO.setDataDevolucaoEfetiva(locacao.getDataDevolucaoEfetiva());
        locacaoDTO.setValor(locacao.getValor());
        locacaoDTO.setStatus(locacao.getStatus());
        locacaoDTO.setMulta(multa);
        
        return ResponseEntity.ok(locacaoDTO);
    }

    // Endpoint para efetuar a devolução do item
    @PostMapping("/efetuar")
    public ResponseEntity<?> efetuarDevolucao(@RequestParam Integer numeroSerie) {
        Locacao locacao = locacaoService.buscarLocacaoPorNumeroSerie(numeroSerie);
        if (locacao == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item não está locado.");
        }

        // Verificar se o item já foi devolvido
        if (locacao.getDataDevolucaoEfetiva() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Item já foi devolvido.");
        }

        // Chama o serviço para efetuar a devolução, o que inclui calcular a multa, se houver
        Locacao locacaoAtualizada = devolucaoService.efetuarDevolucao(numeroSerie);

        // Retorna a locação atualizada com a devolução e a multa aplicada
        return ResponseEntity.ok(locacaoAtualizada);
    }
}
