package ma.universiapolis.dao;

import ma.universiapolis.entities.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface DemandeRepository extends JpaRepository<Demande, Long> {
    List<Demande> findByClientId(Long idClient);
    long countByClientId(Long idClient);

    @Query("select d from Demande d where d.type_demande = :type and d.date_demande between :from and :to")
    List<Demande> findByTypeAndPeriod(@Param("type") String type,
        @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
