package tuti.daos.servicios;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import tuti.daos.accesoDatos.PagoRepository;
import tuti.daos.dto.FacturaResponseDto;
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

    // 409 Conflict - La soli no pudo concretarse
    
    
    @Transactional	// Se encarga del manejo de transacciones, hace que todo sea atomico
    public PagoResponse registrar(PagoRequest req) {	// Se crea la funcion, req funciona como intermediario entre la 
    	FacturaResponseDto factura = buscarFactura(req.getFacturaId());          // 404 - Le pide al servicio 1 la factura correspondiente, si no la encuentra, devuelve 404

        
        if (!factura.getEstado().equals("PENDIENTE") && !factura.getEstado().equals("VENCIDA")) {	// Si el estado de la factura es distinta a pendiente y distinta a vencia
            throw new ConflictoNegocioException("La factura está " + factura.getEstado());   // 409 - Devuelve un 409, y el estado de la factura 
        }
        
        if (pagoRepository.existsByFacturaId(factura.getId())) {	// Si la factura ya tiene un pago asociado, devuelve un 409 indicando que ya esta pago
            throw new ConflictoNegocioException("La factura ya tiene un pago");           // 409 - Se comenta que no se pudo efectuar el pago por que ya esta realizado
        }
        
        

        LocalDate fecha = req.getFechaPago() != null ? req.getFechaPago() : LocalDate.now();  // Si no se puede traer la fecha, se utiliza la de hoy
        if (fecha.isBefore(factura.getFechaEmision()) || fecha.isAfter(LocalDate.now())) {		// Si la fecha de pago es posterior o anterior, arroja un 400
            throw new DatosInvalidosException("Fecha de pago fuera de rango");            // 400 (Bad Request) No se puede procesar la peticion por que las fechas no coninciden
        }
        
        
        // Si accede hasta aca es que todo llego bien. Caso contrario, terminaria el procedimientoa antes
        

        BigDecimal impTotal = factura.getImporte().add(req.getInteres());	//Al importe de la factura se le añaden los intereses. 
        if (req.getImportePagado().compareTo(impTotal) != 0) {		// Si el importe pagado - el total es distinto a 0, arroja un conflicto
            throw new ConflictoNegocioException("El importe pagado debe ser importe de la factura + interés");	// Se devuelve el siguiente mensaje
        }

        
        // Se crea un obj con toda la información del pago
        Pagos pago = new Pagos();
        pago.setFacturaId(factura.getId());
        pago.setFechaPago(fecha);
        pago.setMedioPago(req.getMedioPago());
        pago.setImportePagado(req.getImportePagado());
        pago.setInteres(req.getInteres());

        
        // Se intenta guardar el pago 
        try {
            pago = pagoRepository.saveAndFlush(pago);       // flush: el UNIQUE se verifica acá
        } catch (DataIntegrityViolationException e) {
            throw new ConflictoNegocioException("La factura ya tiene un pago");
        }

        facturaService.marcarComoPagada(factura.getId());      // dentro de la misma transacción
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
    
    
 // Consulta la factura en el servicio de S01. Su servicio lanza EntityNotFoundException, aca se traduce a nuestra excepcion para que devuelva 404
    private FacturaResponseDto buscarFactura(Long id) {
        try {
            return facturaService.obtenerPorId(id);
        } catch (EntityNotFoundException e) {
            throw new RecursoNoEncontradoException("Factura inexistente");
        }
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