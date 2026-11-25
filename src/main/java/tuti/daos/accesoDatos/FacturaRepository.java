package tuti.daos.accesoDatos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tuti.daos.entidades.EstadoFactura;
import tuti.daos.entidades.Factura;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
	
	
    // Método para validar si ya existe una factura mensual activa para el contrato y período
    boolean existsByContratoIdAndPeriodoFacturadoAndEstadoNot(
            Long contratoId, 
            String periodoFacturado, 
            EstadoFactura estado
    );


    // Consulta personalizada para el filtrado flexible (contrato, período o estado)
    @Query("SELECT f FROM Factura f WHERE " +
           "(:contratoId IS NULL OR f.contratoId = :contratoId) AND " +
           "(:periodo IS NULL OR f.periodoFacturado = :periodo) AND " +
           "(:estado IS NULL OR f.estado = :estado)")
    List<Factura> findByFilters(
            @Param("contratoId") Long contratoId, 
            @Param("periodo") String periodo, 
            @Param("estado") EstadoFactura estado
    );
}