package github.com.voidGustavoNunes.projetoLocadora.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import github.com.voidGustavoNunes.projetoLocadora.model.Locacao;
import github.com.voidGustavoNunes.projetoLocadora.model.dto.LocacaoDTO;
import github.com.voidGustavoNunes.projetoLocadora.service.LocacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Validated
@RestController
@RequestMapping("/locacoes")
@Tag(name = "Locacoes", description = "Operações relacionadas às locações")
public class LocacaoController {

    @Autowired
    private LocacaoService locacaoService;

    @Operation(summary = "Lista todas as locações", description = "Retorna uma lista com todas as locações cadastradas")
    @GetMapping
    public List<Locacao> listar() {
        return locacaoService.listar();
    }

    @Operation(summary = "Busca locação por ID", description = "Retorna uma locação específica pelo seu ID")
    @GetMapping("/{id}")
    public Locacao buscarPorId(@PathVariable @NotNull @Positive Long id) {
        return locacaoService.buscarPorId(id);
    }

    @Operation(summary = "Cria uma nova locação", description = "Cria uma locação com base nos dados fornecidos no DTO")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Locacao criar(@RequestBody @Valid LocacaoDTO dto) {
        return locacaoService.criar(dto);
    }

    @Operation(summary = "Atualiza uma locação", description = "Atualiza os dados de uma locação específica pelo seu ID")
    @PutMapping("/{id}")
    public Locacao atualizar(@PathVariable @NotNull @Positive Long id, @RequestBody @Valid LocacaoDTO dto) {
        return locacaoService.atualizar(id, dto);
    }

    @Operation(summary = "Exclui uma locação", description = "Exclui uma locação específica pelo seu ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable @NotNull @Positive Long id) {
        locacaoService.excluir(id);
    }
}
