package github.com.voidGustavoNunes.projetoLocadora.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import github.com.voidGustavoNunes.projetoLocadora.model.Cliente;
import github.com.voidGustavoNunes.projetoLocadora.service.ClienteService;
import github.com.voidGustavoNunes.projetoLocadora.service.DependenteService;
import github.com.voidGustavoNunes.projetoLocadora.service.GenericService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    // @PostMapping("/{clienteId}")
    // @ResponseStatus(HttpStatus.CREATED)
    // public List<Dependente> criarDependente(@PathVariable Long clienteId, @RequestBody @Valid List<Dependente> dependente) {
    //     return dependenteService.criarDependentesParaCliente(dependente, clienteId);
    // }

}
