package github.com.voidGustavoNunes.projetoLocadora.service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import github.com.voidGustavoNunes.exception.RegistroNotFoundException;
import github.com.voidGustavoNunes.projetoLocadora.model.Ator;
import github.com.voidGustavoNunes.projetoLocadora.model.Titulo;
import github.com.voidGustavoNunes.projetoLocadora.repository.TituloRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Service
public class TituloService extends GenericServiceImpl<Titulo, TituloRepository> {

    protected TituloService(TituloRepository repository) {
        super(repository);

    }

    @Override
    public void saveValidation(Titulo entity) throws RegistroNotFoundException {
        if (entity.getId() != null) {
            // Se o id não for nulo, estamos atualizando um registro existente
            Titulo existingTitulo = repository.findById(entity.getId())
                    .orElseThrow(() -> new RegistroNotFoundException(entity.getId()));

            // Verifica se o nome está sendo alterado
            if (!existingTitulo.getNome().equals(entity.getNome())) {
                // Verifica se o novo nome já existe
                if (repository.existsByNome(entity.getNome())) {
                    throw new IllegalArgumentException("Já existe um Titulo com o nome: " + entity.getNome());
                }
            }
        } else {
            // Se o id for nulo, estamos criando um novo registro
            if (repository.existsByNome(entity.getNome())) {
                throw new IllegalArgumentException("Já existe um Titulo com o nome: " + entity.getNome());
            }
        }
        if (entity.getAno() != null) {
            int anoAtual = Year.now().getValue();
            if (entity.getAno() > anoAtual) {
                throw new IllegalArgumentException("O ano não pode ser maior que o ano atual: " + anoAtual);
            }
        }
    }

    @Override
    public Titulo atualizar(@NotNull @Positive Long id, @Valid @NotNull Titulo entityAtualizada) {
        this.saveValidation(entityAtualizada); // Validação personalizada
        entityAtualizada = this.retornaTituloAtualizado(entityAtualizada); // Atualizações específicas
        Titulo entityExistente = repository.findById(id)
                .orElseThrow(() -> new RegistroNotFoundException(id));

        if (entityExistente != null) {
            try {
                Class<?> clazz = entityExistente.getClass();
                List<Field> fields = new ArrayList<>();
                Class<?> currentClass = clazz;

                // Inclui campos da classe e superclasses
                while (currentClass != null) {
                    fields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
                    currentClass = currentClass.getSuperclass();
                }

                for (Field field : fields) {
                    field.setAccessible(true);
                    Object value = field.get(entityAtualizada);
                    if (value != null) {
                        // Atualiza a lista de atores, garantindo que não seja nula
                        if (field.getName().equals("atores") && value instanceof List) {
                            List<Ator> novosAtores = (List<Ator>) value;
                            for (Ator ator : novosAtores) {
                                if (ator.getTitulos() == null) {
                                    ator.setTitulos(new ArrayList<>());
                                }
                                ator.getTitulos().add(entityExistente);
                            }
                        }
                        field.set(entityExistente, value);
                    }
                }
                return repository.save(entityExistente);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Erro ao atualizar a entidade: " + e.getMessage());
            }
        } else {
            throw new RegistroNotFoundException(id);
        }
    }

    private Titulo retornaTituloAtualizado(@NotNull Titulo titulo) {
        // Normalizar campos de texto (exemplo: remover espaços extras)
        if (titulo.getNome() != null) {
            titulo.setNome(titulo.getNome().trim());
        }

        // Garantir que a lista de atores esteja inicializada
        if (titulo.getAtores() != null) {
            List<Ator> atoresAtualizados = new ArrayList<>();
            for (Ator ator : titulo.getAtores()) {
                if (ator.getNome() != null) {
                    ator.setNome(ator.getNome().trim()); // Normalizar nome do ator
                }
                if (ator.getTitulos() == null) {
                    ator.setTitulos(new ArrayList<>()); // Inicializar lista de títulos, se necessário
                }
                ator.getTitulos().add(titulo); // Adicionar o título atual ao ator
                atoresAtualizados.add(ator);
            }
            titulo.setAtores(atoresAtualizados); // Atualizar a lista de atores
        }

        // Garantir que outros campos sejam consistentes (exemplo: ano de lançamento não
        // pode ser futuro)
        if (titulo.getAno() != null) {
            int anoAtual = LocalDate.now().getYear();
            if (titulo.getAno() > anoAtual) {
                throw new IllegalArgumentException("O ano de lançamento não pode ser no futuro.");
            }
        }

        return titulo;
    }

    public List<Titulo> getAllTitulosOrdenados() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }

    @Override
    public Titulo criar(@Valid @NotNull Titulo entity) {
        this.saveValidation(entity);
        for (Ator ator : entity.getAtores()) {
            if (ator.getTitulos() == null) {
                ator.setTitulos(new ArrayList<>());
            }
            ator.getTitulos().add(entity);
        }
        return repository.save(entity);
    }

    public List<Titulo> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Titulo> buscarPorCategoria(String categoria) {
        return repository.findByCategoriaIgnoreCase(categoria);
    }

    public List<Titulo> buscarPorAtor(String nomeAtor) {
        return repository.findByAtorNome(nomeAtor);
    }

    public List<Map<String, Object>> listarTitulosComQuantidade() {
        List<Object[]> resultados = repository.getTitulosComQuantidade();
        return resultados.stream()
                .map(obj -> Map.of(
                        "id", obj[0],
                        "nome", obj[1],
                        "quantidade", obj[2]))
                .collect(Collectors.toList());
    }

}
