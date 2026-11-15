package tuti.daos.accesoDatos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tuti.daos.entidades.Propiedad;

@Repository
public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {

    // Si necesitas listar todas las propiedades disponibles o por tipo:
    List<Propiedad> findByDisponible(boolean disponible);

}