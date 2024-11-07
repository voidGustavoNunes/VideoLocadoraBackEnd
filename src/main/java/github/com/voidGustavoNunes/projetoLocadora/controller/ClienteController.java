package github.com.voidGustavoNunes.projetoLocadora.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;

import github.com.voidGustavoNunes.projetoLocadora.model.Cliente;
import github.com.voidGustavoNunes.projetoLocadora.model.Dependente;
import github.com.voidGustavoNunes.projetoLocadora.service.ClienteService;
import github.com.voidGustavoNunes.projetoLocadora.service.DependenteService;
import github.com.voidGustavoNunes.projetoLocadora.service.GenericService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "Operações relacionadas aos Clientes")
public class ClienteController extends GenericController<Cliente>{
    
    protected ClienteController(GenericService<Cliente> genericService) {
        super(genericService);
    }

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private DependenteService dependenteService;


    @Operation(summary = "Clientes Ordenados", description = "Método que gera uma lista de clientes e os ordena em ordem alfabética")
    // Endpoint que retorna os diretores em ordem alfabética
    @GetMapping("/clientes")
    public List<Cliente> getClientesOrdenados() {
        return clienteService.getAllClientesOrdenados();
    }

    @PostMapping("/{clienteId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Dependente criarDependente(@PathVariable Long clienteId, @RequestBody @Valid Dependente dependente) {
        return dependenteService.criarDependenteParaCliente(dependente, clienteId);
    }

}
