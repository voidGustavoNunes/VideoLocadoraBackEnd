package github.com.voidGustavoNunes.projetoLocadora.service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import github.com.voidGustavoNunes.exception.RegistroNotFoundException;
import github.com.voidGustavoNunes.projetoLocadora.model.Dependente;
import github.com.voidGustavoNunes.projetoLocadora.model.Locacao;
import github.com.voidGustavoNunes.projetoLocadora.model.Socio;
import github.com.voidGustavoNunes.projetoLocadora.repository.DependenteRepository;
import github.com.voidGustavoNunes.projetoLocadora.repository.LocacaoRepository;
import github.com.voidGustavoNunes.projetoLocadora.repository.SocioRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


@Service
public class SocioService extends GenericServiceImpl<Socio, SocioRepository>{

    protected SocioService(SocioRepository repository) {
        super(repository);
    }

    @Autowired
    LocacaoRepository locacaoRepository;

    @Autowired
    private DependenteRepository dependenteRepository;

    @Autowired
    private ClienteService clienteService;

    public List<Dependente> buscarSocioComDependentesAtivos(Long socioId) {
        List<Dependente> dependentes;
        dependentes = dependenteRepository.findByIdWithActiveDependents(socioId);
        return dependentes;
    }

    public List<Socio> getAllSociosOrdenados() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }

    @Override
    public void saveValidation(Socio socio) {
        List<Dependente> dependentes = buscarSocioComDependentesAtivos(socio.getId());
    
        if (socio.getId() != null) {
            // Se o id não for nulo, estamos atualizando um registro existente
            final Long socioId = socio.getId();
            Socio existingSocio = repository.findById(socioId)
                    .orElseThrow(() -> new RegistroNotFoundException(socioId));
    
            // Verifica se o nome está sendo alterado
            if (!existingSocio.getNome().equals(socio.getNome())) {
                // Verifica se o novo nome já existe
                if (repository.existsByNome(socio.getNome())) {
                    throw new IllegalArgumentException("Já existe um Sócio com o nome: " + socio.getNome());
                }
            }
    
            // Verifica se o CPF está sendo alterado
            Optional<Socio> existing = repository.findById(socio.getId());
            if (existing.isPresent() && !existing.get().getCpf().equals(socio.getCpf())) {
                throw new IllegalArgumentException("CPF já cadastrado para outro sócio.");
            }
    
        } else {
            // Se o id for nulo, estamos criando um novo registro
            if (repository.existsByNome(socio.getNome())) {
                throw new IllegalArgumentException("Já existe um Sócio com o nome: " + socio.getNome());
            }
        }
    
        // Valida a data de nascimento
        if (socio.getDataNascimento() != null) {
            LocalDate hoje = LocalDate.now();
            if (socio.getDataNascimento().isAfter(hoje)) {
                throw new IllegalArgumentException("A data de nascimento não pode ser no futuro.");
            }
        }
    
        // Verifica dependentes quando o sócio está inativo
        if (socio.getId() != null && !socio.isAtivo()) {
            if (dependentes.size() >= 3) {
                throw new IllegalArgumentException("Sócio não pode ter mais de 3 dependentes ativos.");
            }
        }
    
        // Valida o CPF
        if (socio.getCpf() == null || socio.getCpf().isEmpty()) {
            throw new IllegalArgumentException("CPF não pode ser vazio.");
        }
    

    }

    public Socio retornaSocioAtualizado(Socio socio) {
        if (socio.getId() != null && socio.getDependentes() != null && !socio.getDependentes().isEmpty()) {
            socio = adicionarDependentes(socio.getId(), socio.getDependentes());
        }
        return socio;
    }
    

    @Override
    public void excluir(@NotNull @Positive Long id) {
        List<Locacao> locacoes = locacaoRepository.findBySocioId(id);

        if (!locacoes.isEmpty()) {
            throw new IllegalStateException("Não é possível excluir Sócio com locações.");
        }else{
            clienteService.excluirCliente(id);
        }
    }


    @Override
    public Socio atualizar(@NotNull @Positive Long id, @Valid @NotNull Socio entityAtualizada) {
        this.saveValidation(entityAtualizada);
        entityAtualizada = this.retornaSocioAtualizado(entityAtualizada);
        Socio entityExistente = repository.findById(id)
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

    public Socio adicionarDependentes(@NotNull @Positive Long socioId, @Valid @NotNull List<Dependente> dependentes) {
        Socio socio = repository.findById(socioId)
                .orElseThrow(() -> new RegistroNotFoundException(socioId));
    
        List<Dependente> dependentesExistentes = dependenteRepository.findBySocioId(socioId);
        List<Dependente> novosDependentes = new ArrayList<>();
    
        for (Dependente dependente : dependentes) {
            // Verifica se o dependente já existe para o sócio
            boolean jaExiste = dependentesExistentes.stream()
                    .anyMatch(d -> d.getNome().equals(dependente.getNome()) && 
                                d.getDataNascimento().equals(dependente.getDataNascimento()));
    
            if (jaExiste) {
                continue; // Ignora dependentes duplicados
            }
    
            // Validações básicas
            if (dependente.getNome() == null || dependente.getNome().isEmpty()) {
                throw new IllegalArgumentException("O nome do dependente não pode ser vazio.");
            }
    
            if (dependente.getDataNascimento() == null || dependente.getDataNascimento().isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("A data de nascimento do dependente é inválida.");
            }
    
            Dependente dependenteNovo = new Dependente();
            dependenteNovo.setNome(dependente.getNome());
            dependenteNovo.setDataNascimento(dependente.getDataNascimento());
            dependenteNovo.setSexo(dependente.getSexo());
            dependenteNovo.setSocioId(socioId);
            dependenteNovo.setAtivo(true);
            dependenteNovo.setEhDependente(true);
    
            dependenteNovo = dependenteRepository.save(dependenteNovo);
            novosDependentes.add(dependenteNovo);
        }
    
        // Adiciona os novos dependentes sem sobrescrever os existentes
        dependentesExistentes.addAll(novosDependentes);
        socio.setDependentes(dependentesExistentes);
    
        return repository.save(socio);
    }
    
    // public List<Dependente> getDependentesBySocioId(@NotNull @Positive Long socioId) {
    //     Socio socio = repository.findById(socioId)
    //             .orElseThrow(() -> new RegistroNotFoundException(socioId));
    //     return socio.getDependentes();
    // }

    
}
