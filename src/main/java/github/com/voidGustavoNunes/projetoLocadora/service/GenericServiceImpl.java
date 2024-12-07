package github.com.voidGustavoNunes.projetoLocadora.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import github.com.voidGustavoNunes.exception.RegistroNotFoundException;
import github.com.voidGustavoNunes.projetoLocadora.model.Ator;
import github.com.voidGustavoNunes.projetoLocadora.model.Titulo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public abstract class GenericServiceImpl<T, R extends JpaRepository<T, Long>> implements GenericService<T> {

    protected R repository;

    protected GenericServiceImpl(R repository) {
        this.repository = repository;
    }

    @Override
    public List<T> listar() {
        return repository.findAll();
    }

    @Override
    public T buscarPorId(@NotNull @Positive Long id) {
        return repository.findById(id).orElseThrow(() -> new RegistroNotFoundException(id));
    }

    @Override
    public T criar(@Valid @NotNull T entity) {
        this.saveValidation(entity);
        return repository.save(entity);
    }

    @Override
    public void excluir(@NotNull @Positive Long id) {
        repository.delete(this.repository.findById(id).orElseThrow(() -> new RegistroNotFoundException(id)));
    }

    @Override
    public T atualizar(@NotNull @Positive Long id, @Valid @NotNull T entityAtualizada) {
        this.saveValidation(entityAtualizada);
        T entityExistente = repository.findById(id)
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

                    // Verifica se o campo é 'atores' para atualizar o relacionamento bidirecional
                    if (field.getName().equals("atores") && value != null) {
                        List<Ator> novosAtores = (List<Ator>) value;

                        // Limpa os títulos antigos dos atores anteriores
                        List<Ator> atoresAntigos = ((Titulo) entityExistente).getAtores();
                        for (Ator ator : atoresAntigos) {
                            ator.getTitulos().remove(entityExistente);
                        }

                        // Atualiza o lado bidirecional com os novos atores
                        for (Ator ator : novosAtores) {
                            ator.getTitulos().add((Titulo) entityExistente);
                        }

                        // Define a nova lista de atores
                        field.set(entityExistente, novosAtores);
                    } else if (value != null) {
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




}
