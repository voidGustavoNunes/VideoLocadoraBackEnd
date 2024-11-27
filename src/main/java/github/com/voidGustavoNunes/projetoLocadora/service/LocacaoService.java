package github.com.voidGustavoNunes.projetoLocadora.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import github.com.voidGustavoNunes.exception.BusinessException;
import github.com.voidGustavoNunes.exception.RegistroNotFoundException;
import github.com.voidGustavoNunes.projetoLocadora.model.Locacao;
import github.com.voidGustavoNunes.projetoLocadora.model.enums.StatusLocacao;
import github.com.voidGustavoNunes.projetoLocadora.repository.ClienteRepository;
import github.com.voidGustavoNunes.projetoLocadora.repository.ItemRepository;
import github.com.voidGustavoNunes.projetoLocadora.repository.LocacaoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Service
public class LocacaoService extends GenericServiceImpl<Locacao, LocacaoRepository>{

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ItemRepository itemRepository;

    protected LocacaoService(LocacaoRepository repository) {
        super(repository);
    }
    
    @Override
    public void saveValidation(Locacao entity) throws RegistroNotFoundException {
        // Valida se o cliente possui locações em débito
        if (repository.existsByClienteIdAndStatus(entity.getCliente().getId(), StatusLocacao.ABERTA)) {
            throw new IllegalArgumentException("Cliente possui locações em débito.");
        }

        // Valida se já existe locação vigente para o item
        if (repository.findByItemIdAndDataLocacao(entity.getItem().getId(), entity.getDataLocacao()).isPresent()) {
            throw new IllegalArgumentException("Item já locado na data especificada.");
        }

        // Valida as datas
        if (!entity.getDataDevolucaoPrevista().isAfter(entity.getDataLocacao())) {
            throw new IllegalArgumentException("A data de devolução prevista deve ser maior que a data de locação.");
        }

        entity.setStatus(StatusLocacao.ABERTA);
    }

    @Override
    public Locacao criar(@Valid @NotNull Locacao entity) {
        this.saveValidation(entity);
        Locacao locacao = criarLocacao(entity.getCliente().getId(), entity.getItem().getId());
        return repository.save(locacao);
    }
    

    @Transactional
    private Locacao criarLocacao(Long clienteId, Long itemId) {
        // Verifica se o cliente existe e está sem débitos
        var cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RegistroNotFoundException(clienteId));

        if (repository.clientePossuiDebitos(clienteId)) {
            throw new BusinessException(clienteId, "O cliente possui débitos pendentes.");
        }

        // Verifica se o item está disponível
        var item = itemRepository.findById(itemId)
            .orElseThrow(() -> new BusinessException(itemId, "Item não encontrado!"));

        if (!repository.itemDisponivel(itemId, LocalDate.now())) {
            throw new BusinessException(itemId,"Item indisponível no momento.");
        }

        // Obtém informações da classe do título
        var classe = item.getTitulo().getClasse();
        if (classe == null) {
            throw new BusinessException(item.getId(),"Classe do título não encontrada.");
        }

        // Calcula a data de devolução prevista e o valor da locação
        var dataAtual = LocalDate.now();
        var dataDevolucaoClasse = classe.getDataDevolucao();
        if (dataDevolucaoClasse == null || dataDevolucaoClasse.isBefore(dataAtual)) {
            throw new BusinessException(classe.getId(),"A data de devolução na classe é inválida.");
        }
    
        // Calcula o prazo restante com base na data de devolução e no dia atual
        var diasRestantes = java.time.temporal.ChronoUnit.DAYS.between(dataAtual, dataDevolucaoClasse);
    
        if (diasRestantes <= 0) {
            throw new BusinessException(classe.getId(),"O prazo de devolução da classe já expirou.");
        }
    
        // Define a data de devolução prevista com base nos dias restantes
        var dataDevolucaoPrevista = dataAtual.plusDays(diasRestantes);

        // Cria e salva a locação
        Locacao locacao = new Locacao();
        locacao.setCliente(cliente);
        locacao.setItem(item);
        locacao.setDataLocacao(dataAtual);
        locacao.setDataDevolucaoPrevista(dataDevolucaoPrevista);
        locacao.setStatus(StatusLocacao.ABERTA);

        return locacao;
    }
    
}
