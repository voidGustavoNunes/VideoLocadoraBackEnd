package github.com.voidGustavoNunes.projetoLocadora.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import github.com.voidGustavoNunes.projetoLocadora.service.GenericService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping("")
public abstract class GenericController<T> {

    private final GenericService<T> genericService;

    protected GenericController(GenericService<T> genericService) {
        this.genericService = genericService;
    }

    @GetMapping
    public List<T> listar() {
        return genericService.listar();
    }

    @GetMapping("/{id}")
    public T buscarPorId(@PathVariable @NotNull @Positive Long id) {
        return genericService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public T criar(@RequestBody @Valid @NotNull T entity) {
        return genericService.criar(entity);
    }

    @PutMapping("/{id}")
    public T atualizar(@PathVariable @NotNull @Positive Long id, @RequestBody @Valid @NotNull T entity) {
        return genericService.atualizar(id, entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable @NotNull @Positive Long id) {
        genericService.excluir(id);
    }
}