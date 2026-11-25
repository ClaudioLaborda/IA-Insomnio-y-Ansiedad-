
package tuti.daos.servicios;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.transaction.annotation.Transactional;
import tuti.daos.accesoDatos.ContratoRepository;
import tuti.daos.accesoDatos.FacturaRepository;
import tuti.daos.dto.FacturaRequestDto;
import tuti.daos.dto.FacturaResponseDto;
import tuti.daos.entidades.Contrato;
import tuti.daos.entidades.EstadoContrato;
import tuti.daos.entidades.EstadoFactura;
import tuti.daos.entidades.Factura;

@Service
@Transactional
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private ContratoRepository contratoRepository;

    @Override
    public FacturaResponseDto emitirFactura(FacturaRequestDto request) {
    	// 1. Validar existencia y estado del contrato
        Contrato contrato = contratoRepository.findById(request.getContratoId())
                .orElseThrow(() -> new EntityNotFoundException("El contrato no existe"));
                
        // Validación correcta usando el enum EstadoContrato
        if (contrato.getEstado() != EstadoContrato.ACTIVO) {
            throw new IllegalStateException("El contrato no se encuentra activo para poder facturar.");
        }

        // 2. Validar que el período tenga al menos un día en la vigencia del contrato
        validarVigenciaPeriodo(contrato, request.getPeriodo());

        // 3. Determinar si es Facturación Mensual o Adicional
        boolean esFacturacionMensual = (request.getImporte() == null) || request.getImporte().compareTo(BigDecimal.ZERO)== 0;

        if (esFacturacionMensual) {
            boolean existeMensualActiva = facturaRepository.existsByContratoIdAndPeriodoFacturadoAndEstadoNot(
                    request.getContratoId(), request.getPeriodo(), EstadoFactura.ANULADA
            );
            
            if (existeMensualActiva) {
                throw new IllegalStateException("Ya existe una factura mensual activa para este contrato y período.");
            }
            
            request.setImporte(contrato.getImporteMensual());
            if (request.getConcepto() == null || request.getConcepto().isBlank()|| "string".equalsIgnoreCase(request.getConcepto().trim()) ) {
                request.setConcepto("Alquiler correspondiente al período " + request.getPeriodo());
            }
        }

        // 4. Calcular o asignar Fecha de Emisión
        LocalDate fechaEmision = request.getFechaEmision() != null ? request.getFechaEmision() : LocalDate.now();

        // 5. Calcular o asignar Fecha de Vencimiento
        LocalDate fechaVencimiento = request.getFechaVencimiento() != null 
                ? request.getFechaVencimiento() 
                : calcularFechaVencimiento(request.getPeriodo(), contrato.getDiaVencimientoMensual());

        if (fechaEmision.isAfter(fechaVencimiento)) {
            throw new IllegalArgumentException("La fecha de emisión no puede ser posterior a la fecha de vencimiento.");
        }

        // 6. Construir y persistir la entidad
        Factura factura = new Factura();
        factura.setContratoId(request.getContratoId());
        factura.setPeriodoFacturado(request.getPeriodo());
        factura.setConcepto(request.getConcepto());
        factura.setFechaEmision(fechaEmision);
        factura.setFechaVencimiento(fechaVencimiento);
        factura.setImporte(request.getImporte());
        factura.setEstado(EstadoFactura.PENDIENTE);

        Factura guardada = facturaRepository.save(factura);
        return mapearADto(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public FacturaResponseDto obtenerPorId(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Factura no encontrada"));
        
        actualizarEstadoDinamicoIfNeeded(factura);
        return mapearADto(factura);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacturaResponseDto> listarFiltradas(Long contratoId, String periodo, EstadoFactura estado) {
        List<Factura> facturas = facturaRepository.findByFilters(contratoId, periodo, estado);
        
        return facturas.stream().map(f -> {
            actualizarEstadoDinamicoIfNeeded(f);
            return mapearADto(f);
        }).collect(Collectors.toList());
    }

    @Override
    public FacturaResponseDto anularFactura(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Factura no encontrada"));

        if (factura.getEstado() == EstadoFactura.PAGADA) {
            throw new IllegalStateException("No se puede anular una factura que ya se encuentra pagada.");
        }
        if (factura.getEstado() == EstadoFactura.ANULADA) {
            throw new IllegalStateException("La factura ya se encuentra anulada.");
        }

        factura.setEstado(EstadoFactura.ANULADA);
        Factura actualizada = facturaRepository.save(factura);
        return mapearADto(actualizada);
    }
    
    
    // metodo de Nico
    
    @Override
    public void marcarComoPagada(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Factura no encontrada"));

        actualizarEstadoDinamicoIfNeeded(factura);

        if (factura.getEstado() != EstadoFactura.PENDIENTE && factura.getEstado() != EstadoFactura.VENCIDA) {
            throw new IllegalStateException("Solo se puede pagar una factura pendiente o vencida.");
        }

        factura.setEstado(EstadoFactura.PAGADA);
        facturaRepository.save(factura);
    }
    
    // ----- ///

    // --- Métodos Privados Auxiliares ---

    private LocalDate calcularFechaVencimiento(String periodo, int diaVencimientoMensual) {
        String[] partes = periodo.split("-");
        int anio = Integer.parseInt(partes[0]);
        int mes = Integer.parseInt(partes[1]);

        YearMonth yearMonth = YearMonth.of(anio, mes);
        int ultimoDiaDelMes = yearMonth.lengthOfMonth();
        int diaElegido = Math.min(diaVencimientoMensual, ultimoDiaDelMes);
        
        return LocalDate.of(anio, mes, diaElegido);
    }

    private void validarVigenciaPeriodo(Contrato contrato, String periodo) {
        String[] partes = periodo.split("-");
        int anio = Integer.parseInt(partes[0]);
        int mes = Integer.parseInt(partes[1]);

        LocalDate inicioPeriodo = LocalDate.of(anio, mes, 1);
        LocalDate finPeriodo = YearMonth.of(anio, mes).atEndOfMonth();

        LocalDate fechaFinContrato = contrato.getFechaInicio().plusMonths(contrato.getDuracionMeses()).minusDays(1);

        boolean tieneAlMenosUnDia = !(inicioPeriodo.isAfter(fechaFinContrato) || finPeriodo.isBefore(contrato.getFechaInicio()));
        
        if (!tieneAlMenosUnDia) {
            throw new IllegalArgumentException("El período facturado no está comprendido dentro de la vigencia del contrato.");
        }
    }

    private void actualizarEstadoDinamicoIfNeeded(Factura factura) {
        if (factura.getEstado() == EstadoFactura.PENDIENTE) {
            if (LocalDate.now().isAfter(factura.getFechaVencimiento())) {
                factura.setEstado(EstadoFactura.VENCIDA);
                facturaRepository.save(factura);
            }
        }
    }

    private FacturaResponseDto mapearADto(Factura factura) {
        FacturaResponseDto dto = new FacturaResponseDto();
        
        dto.setId(factura.getId());
        dto.setContratoId(factura.getContratoId());
        dto.setPeriodoFacturado(factura.getPeriodoFacturado());
        dto.setConcepto(factura.getConcepto());
        dto.setFechaEmision(factura.getFechaEmision());
        dto.setFechaVencimiento(factura.getFechaVencimiento());
        dto.setImporte(factura.getImporte());
        
        // Esta línea es la que falta o hay que revisar:
        dto.setEstado(factura.getEstado() != null
                ? factura.getEstado().name()
                : null);

        return dto;
    }
}