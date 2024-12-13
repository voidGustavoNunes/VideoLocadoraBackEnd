package github.com.voidGustavoNunes.projetoLocadora.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import github.com.voidGustavoNunes.exception.RegistroNotFoundException;
import github.com.voidGustavoNunes.projetoLocadora.model.Cliente;
import github.com.voidGustavoNunes.projetoLocadora.model.Dependente;
import github.com.voidGustavoNunes.projetoLocadora.model.Socio;
import github.com.voidGustavoNunes.projetoLocadora.repository.ClienteRepository;
import github.com.voidGustavoNunes.projetoLocadora.repository.DependenteRepository;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Service
public class ClienteService <T extends Cliente> {

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private DependenteRepository dependenteRepository;


    public T desativarCliente(Long id) {
        return alterarStatusCliente(id, false);
    }
    
    public T ativarCliente(Long id) {
        return alterarStatusCliente(id, true);
    }
    
    private T alterarStatusCliente(Long id, boolean status) {
        Cliente cliente = buscarPorId(id);

        if (cliente.isEhDependente() == false) {
            Socio socio = (Socio) cliente;
            socio.setAtivo(status);

            // Alterar o status dos dependentes
            for (Dependente dependente : socio.getDependentes()) {
                dependente.setAtivo(status);
            }

            repository.save(socio); // Salva o socio e seus dependentes
            return (T) socio;  // Cast para T
        } else if (cliente instanceof Dependente) {
            Dependente dependente = (Dependente) cliente;
            dependente.setAtivo(status);
            repository.save(dependente); // Salva apenas o dependente
            return (T) dependente;  // Cast para T
        }

        throw new RegistroNotFoundException(id); // Caso o cliente não seja encontrado ou não seja do tipo esperado
    }

    public void excluirCliente(Long id) {
        Cliente cliente = buscarPorId(id);
        if(cliente.isEhDependente() == false){
            List<Dependente> novosDependentes = new ArrayList<>();

            Socio socio = (Socio) cliente;
            List<Dependente> dependentes = socio.getDependentes();
            socio.setDependentes(novosDependentes); //remove os dependentes
            
            for(Dependente dependente : dependentes){
                dependenteRepository.delete(dependente); //deleta os dependentes
            }
            repository.delete(socio); //deleta o socio
        } else { //se for dependente apenas o deleta
            repository.delete(cliente);
        }
    }

    public Cliente buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RegistroNotFoundException(id));
    }


    public Cliente ativar(@NotNull @Positive Long id) {
        return ativarCliente(id);
    }
    
    public Cliente desativar(@NotNull @Positive Long id) {
        return desativarCliente(id);
    }

    public List<Cliente> findAll() {
        return repository.findAll();
    }

}
