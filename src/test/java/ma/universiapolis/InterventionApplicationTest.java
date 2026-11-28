package ma.universiapolis;

import jakarta.persistence.EntityManager;
import ma.universiapolis.dao.*;
import ma.universiapolis.entities.*;
import ma.universiapolis.metier.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class InterventionApplicationTest {
    @Autowired IClientService clients;
    @Autowired IDemandeService demandes;
    @Autowired DemandeRepository demandeRepository;
    @Autowired EmployeeRepository employeeRepository;
    @Autowired EntityManager entityManager;

    @Test
    void donneesInitiales() {
        assertThat(clients.getAllClients()).hasSize(2);
        assertThat(clients.getClientById(1L).getNom_client()).isEqualTo("EL IDRISSI Ghizlane");
        assertThat(clients.getClientById(2L).getNom_client()).isEqualTo("KAMALI Mohamed");
        assertThat(demandes.getDemandesOfClient(1L)).isEmpty();
        assertThat(demandes.getDemandesOfClient(2L)).extracting(Demande::getReference).containsExactly("SRM120/2026");
        assertThat(demandes.countByClientId(2L)).isEqualTo(1);
    }

    @Test
    @Transactional
    void filtrerParTypeEtPeriode() {
        LocalDateTime debut = LocalDateTime.of(2026, 1, 22, 0, 0);
        Client client = clients.getClientById(2L);
        demandes.addDemande(Demande.builder().reference("FIN").type_demande("Provisoire")
            .date_demande(debut.plusDays(1)).client(client).build());
        demandes.addDemande(Demande.builder().reference("AUTRE").type_demande("Definitive")
            .date_demande(debut).client(client).build());
        demandes.addDemande(Demande.builder().reference("HORS").type_demande("Provisoire")
            .date_demande(debut.plusDays(2)).client(client).build());
        assertThat(demandes.getDemandesByTypeAndPeriod("Provisoire", debut, debut.plusDays(1)))
            .extracting(Demande::getReference).containsExactlyInAnyOrder("SRM120/2026", "FIN");
        assertThat(demandes.getDemandesByTypeAndPeriod("Provisoire", debut, debut)).hasSize(1);
        assertThat(demandes.getDemandesByTypeAndPeriod("Absent", debut, debut.plusDays(1))).isEmpty();
        assertThat(demandes.countByClientId(2L)).isEqualTo(4);
    }

    @Test
    @Transactional
    void relationsEtSuppressionClient() {
        Client client = clients.addClient(Client.builder().nom_client("Test").adresse("Laayoune").tele(600000001).build());
        Long clientId = client.getId();
        Employee employe = employeeRepository.save(Employee.builder().nom_employee("Employé test").grade("Technicien").salaire(5000).build());
        Long employeeId = employe.getId();
        for (String ref : List.of("T1", "T2")) {
            demandes.addDemande(Demande.builder().reference(ref).client(client)
                .employees(new ArrayList<>(List.of(employe))).build());
        }
        entityManager.flush();
        entityManager.clear();
        assertThat(employeeRepository.findById(employeeId).orElseThrow().getDemandes()).hasSize(2);
        assertThat(clients.getClientById(clientId).getDemandes()).hasSize(2);
        clients.deleteClient(clientId);
        entityManager.flush();
        entityManager.clear();
        assertThat(demandes.getDemandesOfClient(clientId)).isEmpty();
        assertThat(employeeRepository.findById(employeeId)).isPresent();
        assertThat(employeeRepository.findById(employeeId).orElseThrow().getDemandes()).isEmpty();
    }

    @Test
    void clientInexistant() {
        assertThatThrownBy(() -> clients.getClientById(999999L)).isInstanceOf(ResponseStatusException.class);
        assertThatThrownBy(() -> clients.deleteClient(999999L)).isInstanceOf(ResponseStatusException.class);
        assertThat(demandes.countByClientId(999999L)).isZero();
    }

    @Test
    void demandeSansClientValide() {
        assertThatThrownBy(() -> demandes.addDemande(Demande.builder().reference("INVALIDE").build()))
            .isInstanceOf(ResponseStatusException.class);
        assertThatThrownBy(() -> demandes.addDemande(Demande.builder()
            .client(Client.builder().id(999999L).build()).build()))
            .isInstanceOf(ResponseStatusException.class);
    }
}
