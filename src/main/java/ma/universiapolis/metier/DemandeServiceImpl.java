package ma.universiapolis.metier;

import ma.universiapolis.dao.ClientRepository;
import ma.universiapolis.dao.DemandeRepository;
import ma.universiapolis.entities.Demande;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class DemandeServiceImpl implements IDemandeService {
    private final DemandeRepository demandeRepository;
    private final ClientRepository clientRepository;

    public DemandeServiceImpl(DemandeRepository demandeRepository, ClientRepository clientRepository) {
        this.demandeRepository = demandeRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public Demande addDemande(Demande d) {
        if (d.getClient() == null || d.getClient().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le client est obligatoire");
        }
        d.setClient(clientRepository.findById(d.getClient().getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client introuvable")));
        d.setId(null);
        return demandeRepository.save(d);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Demande> getDemandesOfClient(Long idClient) {
        return demandeRepository.findByClientId(idClient);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Demande> getDemandesByTypeAndPeriod(String type, LocalDateTime from, LocalDateTime to) {
        return demandeRepository.findByTypeAndPeriod(type, from, to);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByClientId(Long idClient) {
        return demandeRepository.countByClientId(idClient);
    }
}
