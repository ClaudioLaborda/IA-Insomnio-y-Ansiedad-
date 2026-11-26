package tuti.daos.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tuti.daos.entidades.HistorialEstadoIncidente;

@Repository
public interface HistorialEstadoIncidenteRepository extends JpaRepository<HistorialEstadoIncidente, Long> {
}