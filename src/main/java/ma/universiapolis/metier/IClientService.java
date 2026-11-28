package ma.universiapolis.metier;

import ma.universiapolis.entities.Client;
import java.util.List;

public interface IClientService {
    Client addClient(Client c);
    Client getClientById(Long id);
    void deleteClient(Long id);
    List<Client> getAllClients();
}
