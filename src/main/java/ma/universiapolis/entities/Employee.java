package ma.universiapolis.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom_employee;
    private String grade;
    private int salaire;

    @ManyToMany(mappedBy = "employees")
    @JsonIgnore
    @Builder.Default
    private List<Demande> demandes = new ArrayList<>();
}
