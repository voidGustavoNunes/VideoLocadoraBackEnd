package github.com.voidGustavoNunes.projetoLocadora.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import github.com.voidGustavoNunes.exception.BusinessException;
import github.com.voidGustavoNunes.exception.RegistroNotFoundException;
import github.com.voidGustavoNunes.projetoLocadora.model.Classe;
import github.com.voidGustavoNunes.projetoLocadora.model.Cliente;
import github.com.voidGustavoNunes.projetoLocadora.model.Item;
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
        var cliente = validarCliente(clienteId);
        var item = validarItem(itemId);

        var classe = item.getTitulo().getClasse();
        if (classe == null) {
            throw new BusinessException(item.getId(), "Classe do título não encontrada.");
        }

        var dataDevolucaoPrevista = calcularDataDevolucaoPrevista(classe);
        var valorAlocacao = calcularValorLocacao(classe);

        // Cria e retorna a locação
        Locacao locacao = new Locacao();
        locacao.setCliente(cliente);
        locacao.setItem(item);
        locacao.setDataLocacao(LocalDate.now());
        locacao.setDataDevolucaoPrevista(dataDevolucaoPrevista);
        locacao.setStatus(StatusLocacao.ABERTA);
        locacao.setValor(valorAlocacao);

        return locacao;
    }

    private Cliente validarCliente(Long clienteId) {
        var cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RegistroNotFoundException(clienteId));

        if (repository.clientePossuiDebitos(clienteId)) {
            throw new BusinessException(clienteId, "O cliente possui débitos pendentes.");
        }
        return cliente;
    }

    private Item validarItem(Long itemId) {
        var item = itemRepository.findById(itemId)
            .orElseThrow(() -> new BusinessException(itemId, "Item não encontrado!"));

        if (!repository.itemDisponivel(itemId, LocalDate.now())) {
            throw new BusinessException(itemId, "Item indisponível no momento.");
        }
        return item;
    }

    private LocalDate calcularDataDevolucaoPrevista(Classe classe) {
        var dataAtual = LocalDate.now();
        var dataDevolucaoClasse = classe.getDataDevolucao();
        if (dataDevolucaoClasse == null || dataDevolucaoClasse.isBefore(dataAtual)) {
            throw new BusinessException(classe.getId(), "A data de devolução na classe é inválida.");
        }

        var diasRestantes = java.time.temporal.ChronoUnit.DAYS.between(dataAtual, dataDevolucaoClasse);
        if (diasRestantes <= 0) {
            throw new BusinessException(classe.getId(), "O prazo de devolução da classe já expirou.");
        }

        return dataAtual.plusDays(diasRestantes);
    }

    private BigDecimal calcularValorLocacao(Classe classe) {
        return classe.getValor();
    }

    public Locacao buscarLocacaoPorNumeroSerie(String numeroSerie) {
        return repository.findByItemNumeroSerie(numeroSerie);
    }
    
}
