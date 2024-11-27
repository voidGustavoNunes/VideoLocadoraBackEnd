package github.com.voidGustavoNunes.projetoLocadora.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import github.com.voidGustavoNunes.projetoLocadora.model.Dependente;
import github.com.voidGustavoNunes.projetoLocadora.model.Socio;
import github.com.voidGustavoNunes.projetoLocadora.repository.DependenteRepository;
import github.com.voidGustavoNunes.projetoLocadora.repository.SocioRepository;

@Service
public class SocioService extends GenericServiceImpl<Socio, SocioRepository>{

    protected SocioService(SocioRepository repository) {
        super(repository);
    }

    @Autowired
    private DependenteRepository dependenteRepository;

    public List<Dependente> buscarSocioComDependentesAtivos(Long socioId) {
        List<Dependente> dependentes;
        dependentes = dependenteRepository.findByIdWithActiveDependents(socioId);
        return dependentes;
    }

    @Override
    public void saveValidation(Socio socio) {

        List<Dependente> dependentes = buscarSocioComDependentesAtivos(socio.getId());

        if (!socio.isAtivo()) { ///VERIFICAR NO FRONT
            for (Dependente dependente : dependentes) {
                dependente.setAtivo(false);
                dependenteRepository.save(dependente); // Persistir a mudança
            }
        }

        if (socio.getCpf() == null || socio.getCpf().isEmpty()) {
            throw new IllegalArgumentException("CPF não pode ser vazio.");
        }

        Optional<Socio> existing = repository.findById(socio.getId());
        if (existing.isPresent() && !existing.get().getCpf().equals(socio.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado para outro sócio.");
        }
        if(dependentes.size() >= 3){
            throw new IllegalArgumentException("Sócio não pode ter mais de 3 dependentes ativos.");
        }
        
    }
    
}
