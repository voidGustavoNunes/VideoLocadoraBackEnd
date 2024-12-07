package github.com.voidGustavoNunes.projetoLocadora.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import github.com.voidGustavoNunes.projetoLocadora.model.Cliente;
import github.com.voidGustavoNunes.projetoLocadora.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Controller
@Validated
@RestController
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "Operações relacionadas aos clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(summary = "Ativar", description = "Método que ativa uma entidade")
    @PutMapping("/ativar/{id}")
    public Cliente ativar(@PathVariable @NotNull @Positive Long id) {
        return clienteService.ativar(id);
    }

    @Operation(summary = "Desativar", description = "Método que desativa uma entidade")
    @PutMapping("/desativar/{id}")
    public Cliente desativar(@PathVariable @NotNull @Positive Long id) {
        return clienteService.desativar(id);
    }

    @Operation(summary = "Listar", description = "Método que lista todas os clientes")
    @GetMapping
    public List<Cliente> listar() {
        return clienteService.findAll();
    }
}
