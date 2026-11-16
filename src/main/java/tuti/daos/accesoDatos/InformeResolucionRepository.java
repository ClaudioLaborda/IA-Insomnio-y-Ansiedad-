package tuti.daos.accesoDatos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tuti.daos.entidades.InformeResolucion;

@Repository
public interface InformeResolucionRepository extends JpaRepository<InformeResolucion, Long> {
    List<InformeResolucion> findByIncidenteId(Long incidenteId);
}