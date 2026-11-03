package tuti.daos.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tuti.daos.entidades.Incidente;

@Repository
public interface IncidenteRepository extends JpaRepository<Incidente, Long> {
}