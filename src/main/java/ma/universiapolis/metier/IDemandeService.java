package ma.universiapolis.metier;

import ma.universiapolis.entities.Demande;
import java.time.LocalDateTime;
import java.util.List;

public interface IDemandeService {
    Demande addDemande(Demande d);
    List<Demande> getDemandesOfClient(Long idClient);
    List<Demande> getDemandesByTypeAndPeriod(String type, LocalDateTime from, LocalDateTime to);
    long countByClientId(Long idClient);
}
