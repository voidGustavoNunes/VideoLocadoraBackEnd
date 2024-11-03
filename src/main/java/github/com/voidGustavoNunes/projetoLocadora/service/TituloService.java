package github.com.voidGustavoNunes.projetoLocadora.service;

import java.lang.reflect.Field;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

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
public class TituloService extends GenericServiceImpl<Titulo, TituloRepository>{

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
        this.saveValidation(entityAtualizada);
        Titulo entityExistente = repository.findById(id)
                .orElseThrow(() -> new RegistroNotFoundException(id));
        
        if (entityExistente != null) {
            try {
                Class<?> clazz = entityExistente.getClass();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object value = field.get(entityAtualizada);
                    

                    if (field.getName().equals("atores") && value != null) {
                        List<Ator> novosAtores = (List<Ator>) value;
                        

                        List<Ator> atoresAntigos = ((Titulo) entityExistente).getAtores();
                        for (Ator ator : atoresAntigos) {
                            ator.getTitulos().remove(entityExistente);
                        }
                        

                        for (Ator ator : novosAtores) {
                            ator.getTitulos().add((Titulo) entityExistente);
                        }
                        

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

    public List<Titulo> getAllTitulosOrdenados() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }
    

    @Override
    public Titulo criar(@Valid @NotNull Titulo entity) {
        this.saveValidation(entity);
        for (Ator ator : entity.getAtores()) {
            if(ator.getTitulos() == null){
                ator.setTitulos(new ArrayList<>());
            }
            ator.getTitulos().add(entity);  
        }
        return repository.save(entity);
    }

}
