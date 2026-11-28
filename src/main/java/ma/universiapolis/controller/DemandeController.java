package ma.universiapolis.controller;

import ma.universiapolis.entities.Demande;
import ma.universiapolis.metier.IDemandeService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/srmlsh")
public class DemandeController {
    private final IDemandeService demandeService;

    public DemandeController(IDemandeService demandeService) {
        this.demandeService = demandeService;
    }

    @GetMapping("/demande/client/{id}")
    public List<Demande> getDemandesOfClient(@PathVariable Long id) {
        return demandeService.getDemandesOfClient(id);
    }
}
