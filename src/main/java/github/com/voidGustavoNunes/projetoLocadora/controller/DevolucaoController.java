package github.com.voidGustavoNunes.projetoLocadora.controller;

import github.com.voidGustavoNunes.projetoLocadora.model.Locacao;
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
    public ResponseEntity<?> buscarLocacao(@RequestParam String numeroSerie) {
        Locacao locacao = locacaoService.buscarLocacaoPorNumeroSerie(numeroSerie);
        if (locacao == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item não está locado.");
        }

        // Calcular multa, se houver (não é necessário no método de busca)
        BigDecimal multa = devolucaoService.calcularMulta(locacao);
        
        // Retorna a locação com a multa calculada, se houver
        locacao.setValor(locacao.getValor().add(multa)); // Se houver multa, soma ao valor
        return ResponseEntity.ok(locacao);
    }

    // Endpoint para efetuar a devolução do item
    @PostMapping("/efetuar")
    public ResponseEntity<?> efetuarDevolucao(@RequestBody String numeroSerie) {
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
