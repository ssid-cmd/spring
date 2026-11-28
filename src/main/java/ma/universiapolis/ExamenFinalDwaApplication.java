package ma.universiapolis;

import ma.universiapolis.entities.Client;
import ma.universiapolis.entities.Demande;
import ma.universiapolis.metier.IClientService;
import ma.universiapolis.metier.IDemandeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.time.LocalDateTime;

@SpringBootApplication
public class ExamenFinalDwaApplication implements CommandLineRunner {
    private final IClientService clientService;
    private final IDemandeService demandeService;

    public ExamenFinalDwaApplication(IClientService clientService, IDemandeService demandeService) {
        this.clientService = clientService;
        this.demandeService = demandeService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ExamenFinalDwaApplication.class, args);
    }

    @Override
    public void run(String... args) {
        clientService.addClient(Client.builder()
            .nom_client("EL IDRISSI Ghizlane")
            .adresse("Hay el fida 1, agadir")
            .tele(600000000)
            .build());
        Client client = clientService.addClient(Client.builder()
            .nom_client("KAMALI Mohamed")
            .adresse("Hay el Raha , Laayoune")
            .tele(600000000)
            .build());
        demandeService.addDemande(Demande.builder()
            .reference("SRM120/2026")
            .date_demande(LocalDateTime.of(2026, 1, 22, 0, 0))
            .type_demande("Provisoire")
            .description("Première demande")
            .client(client)
            .build());
    }
}
