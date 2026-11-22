package tuti.daos.accesoDatos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tuti.daos.entidades.Contrato;
import tuti.daos.entidades.EstadoContrato;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    // Método para buscar contratos según su EstadoContrato (ej: ACTIVO)
    List<Contrato> findByEstado(EstadoContrato estado);

}