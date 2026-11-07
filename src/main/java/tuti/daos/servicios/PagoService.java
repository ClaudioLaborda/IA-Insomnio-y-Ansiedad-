package tuti.daos.servicios;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import tuti.daos.accesoDatos.PagoRepository;
import tuti.daos.excepciones.*;
import tuti.daos.presentacion.*;
import tuti.daos.entidades.Pagos;
import tuti.daos.entidades.Factura;


// Service lleva toda la logica del proceso

@Service
public class PagoService {

	// Creamos dos objetos que van a mantener la "comunicacion" entre repository y FacturasService
    private final PagoRepository pagoRepository;	// sirve para guardar y leer la BD
    private final FacturaService facturaService;	// sirve para consultar y modificar las facturas

    // Definimos el constructor para los obj de pagoRepository y facturaService
    public PagoService(PagoRepository pagoRepository, FacturaService facturaService) {
        this.pagoRepository = pagoRepository;
        this.facturaService = facturaService;
    }

    
    
    
    @Transactional	// Se encarga del manejo de transacciones
    public PagoResponse registrar(PagoRequest req) {
        FacturaInfo factura = facturaService.obtener(req.getFacturaId());          // 404

        
        if (!factura.estado().equals("PENDIENTE") && !factura.estado().equals("VENCIDA")) {
            throw new ConflictoNegocioException("La factura está " + factura.estado());   // 409
        }
        
        if (pagoRepository.existsByFacturaId(factura.id())) {
            throw new ConflictoNegocioException("La factura ya tiene un pago");           // 409
        }
        
        

        LocalDate fecha = req.getFechaPago() != null ? req.getFechaPago() : LocalDate.now();
        if (fecha.isBefore(factura.fechaEmision()) || fecha.isAfter(LocalDate.now())) {
            throw new DatosInvalidosException("Fecha de pago fuera de rango");            // 400
        }
        

        BigDecimal esperado = factura.importe().add(req.getInteres());
        if (req.getImportePagado().compareTo(esperado) != 0) {
            throw new ConflictoNegocioException("El importe pagado debe ser importe de la factura + interés");
        }

        
        
        Pagos pago = new Pagos();
        pago.setFacturaId(factura.id());
        pago.setFechaPago(fecha);
        pago.setMedioPago(req.getMedioPago());
        pago.setImportePagado(req.getImportePagado());
        pago.setInteres(req.getInteres());

        
        try {
            pago = pagoRepository.saveAndFlush(pago);       // flush: el UNIQUE se verifica acá
        } catch (DataIntegrityViolationException e) {
            throw new ConflictoNegocioException("La factura ya tiene un pago");
        }

        facturaService.marcarComoPagada(factura.id());      // dentro de la misma transacción
        return toResponse(pago);
    }

    @Transactional(readOnly = true)
    public PagoResponse obtener(Long id) {
        return pagoRepository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pago inexistente"));
    }

    @Transactional(readOnly = true)
    public List<PagoResponse> listar(Long facturaId) {
        List<Pagos> pagos = (facturaId == null)
                ? pagoRepository.findAll()
                : pagoRepository.findByFacturaId(facturaId).map(List::of).orElse(List.of());
        return pagos.stream().map(this::toResponse).toList();
    }

    private PagoResponse toResponse(Pagos p) {
        PagoResponse r = new PagoResponse();
        r.setId(p.getId());
        r.setFacturaId(p.getFacturaId());
        r.setFechaPago(p.getFechaPago());
        r.setMedioPago(p.getMedioPago());
        r.setImportePagado(p.getImportePagado());
        r.setInteres(p.getInteres());
        return r;
    }
}