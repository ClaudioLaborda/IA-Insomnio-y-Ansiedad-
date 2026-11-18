package tuti.daos.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tuti.daos.entidades.Incidente;

import java.util.List;

@Repository
public interface IncidenteRepository extends JpaRepository<Incidente, Long> {

    @Query("SELECT i FROM Incidente i WHERE " +
           "(:contratoId IS NULL OR i.contratoId = :contratoId) AND " +
           "(:estado IS NULL OR i.estado = :estado) AND " +
           "(:categoria IS NULL OR i.categoria = :categoria) AND " +
           "(:prioridad IS NULL OR i.prioridad = :prioridad)")
    List<Incidente> filtrarIncidentes(
            @Param("contratoId") Long contratoId,
            @Param("estado") Incidente.EstadoIncidente estado,
            @Param("categoria") Incidente.CategoriaIncidente categoria,
            @Param("prioridad") Incidente.PrioridadIncidente prioridad
    );
}