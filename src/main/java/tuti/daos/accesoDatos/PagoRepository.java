package tuti.daos.accesoDatos;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import tuti.daos.entidades.Pagos;

public interface PagoRepository extends JpaRepository<Pagos, Long>{
	Optional<Pagos> findByFacturaId(Long facturaId);
	boolean existsByFacturaId(Long facturaId);
}