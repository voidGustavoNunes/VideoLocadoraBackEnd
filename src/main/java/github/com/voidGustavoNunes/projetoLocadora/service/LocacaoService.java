package github.com.voidGustavoNunes.projetoLocadora.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import github.com.voidGustavoNunes.exception.BusinessException;
import github.com.voidGustavoNunes.exception.RegistroNotFoundException;
import github.com.voidGustavoNunes.projetoLocadora.model.Cliente;
import github.com.voidGustavoNunes.projetoLocadora.model.Item;
import github.com.voidGustavoNunes.projetoLocadora.model.Locacao;
import github.com.voidGustavoNunes.projetoLocadora.model.dto.LocacaoDTO;
import github.com.voidGustavoNunes.projetoLocadora.model.enums.StatusLocacao;
import github.com.voidGustavoNunes.projetoLocadora.repository.ClienteRepository;
import github.com.voidGustavoNunes.projetoLocadora.repository.ItemRepository;
import github.com.voidGustavoNunes.projetoLocadora.repository.LocacaoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;


@Service
public class LocacaoService extends GenericServiceImpl<Locacao, LocacaoRepository> {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private LocacaoRepository repository;

    protected LocacaoService(LocacaoRepository repository) {
        super(repository);
    }

    @Override
    public void saveValidation(Locacao entity) throws RegistroNotFoundException {
        if (repository.existsByClienteIdAndStatus(entity.getCliente().getId(), StatusLocacao.ABERTA)) {
            throw new BusinessException(entity.getCliente().getId(), "Cliente possui locações em débito.");
        }

        if (repository.findByItemIdAndDataLocacao(entity.getItem().getId(), entity.getDataLocacao()).isPresent()) {
            throw new BusinessException(entity.getItem().getId(), "Item já locado na data especificada.");
        }

        if (!entity.getDataDevolucaoPrevista().isAfter(entity.getDataLocacao())) {
            throw new BusinessException(entity.getId(), "A data de devolução prevista deve ser maior que a data de locação.");
        }

        entity.setStatus(StatusLocacao.ABERTA);
    }

    @Transactional
    public Locacao criar(@Valid @NotNull LocacaoDTO dto) {
        // Valida e obtém cliente e item
        var cliente = validarCliente(dto.getClienteId());
        var item = validarItem(dto.getItemId());

        // Cria nova entidade Locacao com base no DTO
        Locacao locacao = new Locacao();
        locacao.setCliente(cliente);
        locacao.setItem(item);
        locacao.setDataLocacao(LocalDate.now());
        locacao.setDataDevolucaoPrevista(dto.getDataDevolucaoPrevista());
        locacao.setValor(dto.getValor());
        locacao.setStatus(StatusLocacao.ABERTA);

        // Valida e salva locação
        saveValidation(locacao);
        return repository.save(locacao);
    }

    private Cliente validarCliente(Long clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RegistroNotFoundException(clienteId));
    }

    private Item validarItem(Long itemId) {
        var item = itemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessException(itemId, "Item não encontrado!"));

        if (!repository.itemDisponivel(itemId, LocalDate.now())) {
            throw new BusinessException(itemId, "Item indisponível no momento.");
        }
        return item;
    }

    public Locacao buscarLocacaoPorNumeroSerie(Integer numeroSerie) {

        return repository.findByItemNumeroSerie(numeroSerie);
    }
    
    @Transactional
    public Locacao atualizar(@NotNull Long id, @Valid @NotNull LocacaoDTO dto) {
        // Verifica se a locação existe
        Locacao locacaoExistente = repository.findById(id)
            .orElseThrow(() -> new RegistroNotFoundException(id));

        // Valida cliente
        Cliente cliente = validarCliente(dto.getClienteId());

        // Valida item
        Item item = validarItem(dto.getItemId());

        // Atualiza os campos permitidos
        locacaoExistente.setCliente(cliente);
        locacaoExistente.setItem(item);
        locacaoExistente.setDataDevolucaoPrevista(dto.getDataDevolucaoPrevista());
        locacaoExistente.setValor(dto.getValor());

        // Mantém a data de locação e status inalterados
        return repository.save(locacaoExistente);
    }
}
