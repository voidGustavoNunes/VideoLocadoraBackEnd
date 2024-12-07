package github.com.voidGustavoNunes.projetoLocadora.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import github.com.voidGustavoNunes.projetoLocadora.model.Socio;
import github.com.voidGustavoNunes.projetoLocadora.service.GenericService;
import github.com.voidGustavoNunes.projetoLocadora.service.SocioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping("/socios")
@Tag(name = "Socios", description = "Operações relacionadas aos socios")
public class SocioController extends GenericController<Socio>{

    @Autowired
    private SocioService socioService;
    
    protected SocioController(GenericService<Socio> genericService) {
        super(genericService);
    }

    @Operation(summary = "Socios Ordenados", description = "Método que gera uma lista de sócios e os ordena em ordem alfabética")
    @GetMapping("/socios")
    public List<Socio> getSociosOrdenados() {
        return socioService.getAllSociosOrdenados();
    }

    // @Operation(summary = "Dependentes a partir de um sócio", description = "Método que gera uma lista de dependentes a partir do id de um sócio")
    // @GetMapping("/{socioId}/dependentes")
    // public List<Dependente> getDependentesBySocioId(@PathVariable @NotNull @Positive Long socioId) {
    //     return socioService.getDependentesBySocioId(socioId);
    // }

    @Override
    @Operation(summary = "Excluir", description = "Método que exclui pernamentemente uma entidade")
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable @NotNull @Positive Long id) {
        socioService.excluir(id);
    }

    // @Operation(summary = "Ativar", description = "Método que ativa uma entidade")
    // @PutMapping("/ativar/{id}")
    // public Cliente ativar(@PathVariable @NotNull @Positive Long id) {
    //     socioService.ativar(id);
    // }

    // @Operation(summary = "Desativar", description = "Método que desativa uma entidade")
    // @PutMapping("/desativar/{id}")
    // public void desativar(@PathVariable @NotNull @Positive Long id) {
    //     socioService.desativar(id);
    // }


}
