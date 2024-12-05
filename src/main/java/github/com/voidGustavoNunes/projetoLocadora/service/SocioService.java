package github.com.voidGustavoNunes.projetoLocadora.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import github.com.voidGustavoNunes.exception.RegistroNotFoundException;
import github.com.voidGustavoNunes.projetoLocadora.model.Dependente;
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
    private DependenteService dependenteService;

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

        // System.out.println("Validando o Sócio: " + socio);
        if (socio.getId() != null) {
            // Se o id não for nulo, estamos atualizando um registro existente
            final Long socioId = socio.getId();
            Socio existingSocio= repository.findById(socioId)
                    .orElseThrow(() -> new RegistroNotFoundException(socioId));
            
            // Verifica se o nome está sendo alterado
            if (!existingSocio.getNome().equals(socio.getNome())) {
                // Verifica se o novo nome já existe
                if (repository.existsByNome(socio.getNome())) {
                    throw new IllegalArgumentException("Já existe um Sócio com o nome: " + socio.getNome());
                }
            }
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
        if (socio.getDataNascimento() != null) {
            LocalDate hoje = LocalDate.now();
            if (socio.getDataNascimento().isAfter(hoje)) {
                throw new IllegalArgumentException("A data de nascimento não pode ser no futuro.");
            }
        }

        if (socio.getId()!= null && !socio.isAtivo()) {
             ///VERIFICAR NO FRONT
            if(dependentes.size() >= 3){
                throw new IllegalArgumentException("Sócio não pode ter mais de 3 dependentes ativos.");
            }else{
                for (Dependente dependente : dependentes) {
                    dependente.setAtivo(false);
                    // dependenteRepository.save(dependente); // Persistir a mudança
                }
            }

        }

        if (socio.getCpf() == null || socio.getCpf().isEmpty()) {
            throw new IllegalArgumentException("CPF não pode ser vazio.");
        }
        if(socio.getId() != null && !socio.getDependentes().isEmpty()){
            socio = adicionarDependentes(socio.getId(), socio.getDependentes());
        }
        
    }

    @Override
    public void excluir(@NotNull @Positive Long id) {
        if (locacaoRepository.findBySocioId(id)) {
            throw new IllegalStateException("Não é possível excluir Sócio com locações.");
        }else{
            repository.delete(this.repository.findById(id).orElseThrow(() -> new RegistroNotFoundException(id)));
        }
    }

    public Socio adicionarDependentes(@NotNull @Positive Long socioId, @Valid @NotNull List<Dependente> dependentes) {
        Socio socio = repository.findById(socioId)
                .orElseThrow(() -> new RegistroNotFoundException(socioId));
        
        List<Dependente> novosDependentes = new ArrayList<>();

        for (Dependente dependente : dependentes) {
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
            dependenteNovo.setSocio(socio);
            dependenteNovo.setAtivo(true);

            novosDependentes.add(dependenteNovo);
        }
        socio.setDependentes(new ArrayList<>());
        socio.setDependentes(novosDependentes);

        return socio;

        // // Use dependenteService to save each new dependent
        // for (Dependente dependente : novosDependentes) {
        //     if(dependente.getId() != null){
        //         dependenteService.atualizar(dependente.getId(), dependente);
        //     }
        //     else{
        //         dependenteService.criar(dependente);
        //     }
        // }

        // if (socio.getDependentes() == null) {
        //     socio.setDependentes(new ArrayList<>());
        // }
        //socio.getDependentes().addAll(novosDependentes);

        // repository.save(socio);
    }
    
    
}
