package tuti.daos.servicios;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuti.daos.accesoDatos.HistorialEstadoIncidenteRepository;
import tuti.daos.accesoDatos.IncidenteRepository;
import tuti.daos.accesoDatos.InformeResolucionRepository;
import tuti.daos.entidades.HistorialEstadoIncidente;
import tuti.daos.entidades.Incidente;
import tuti.daos.entidades.InformeResolucion;
import tuti.daos.excepciones.Excepcion;
import tuti.daos.presentacion.InformeResolucionResponse;

@Service
public class InformeResolucionService {

    private final InformeResolucionRepository informeRepo;
    private final IncidenteRepository incidenteRepo;
    private final HistorialEstadoIncidenteRepository historialRepo;

    public InformeResolucionService(
            InformeResolucionRepository informeRepo,
            IncidenteRepository incidenteRepo,
            HistorialEstadoIncidenteRepository historialRepo) {
        this.informeRepo = informeRepo;
        this.incidenteRepo = incidenteRepo;
        this.historialRepo = historialRepo;
    }

    @Transactional
    public InformeResolucion registrarResolucion(Long incidenteId, InformeResolucion informe) throws Excepcion {
        Incidente incidente = incidenteRepo.findById(incidenteId)
                .orElseThrow(() -> new Excepcion(
                        "incidenteId",
                        "El incidente con ID " + incidenteId + " no existe.",
                        HttpStatus.NOT_FOUND.value()
                ));

        Incidente.EstadoIncidente estadoActual = incidente.getEstado();
        if (estadoActual != Incidente.EstadoIncidente.EN_PROCESO) {
            throw new Excepcion(
                    "incidenteId",
                    "No se puede resolver el incidente porque su estado actual es '" + estadoActual + "' (debe estar 'EN_PROCESO').",
                    HttpStatus.CONFLICT.value()
            );
        }

        // Misma fecha y hora exacta para el informe y la transición en el historial
        LocalDateTime fechaResolucion = LocalDateTime.now();

        // 1. Actualizar estado del incidente
        incidente.setEstado(Incidente.EstadoIncidente.RESUELTO);
        incidenteRepo.save(incidente);

        // 2. Registrar transición en el historial de estados
        HistorialEstadoIncidente historial = new HistorialEstadoIncidente();
        historial.setIncidente(incidente);
        historial.setEstadoAnterior(estadoActual != null ? estadoActual.name() : null);
        historial.setEstadoNuevo(Incidente.EstadoIncidente.RESUELTO.name());
        historial.setFechaHora(fechaResolucion);
        historialRepo.save(historial);

        // 3. Guardar el informe de resolución
        informe.setIncidente(incidente);
        informe.setFechaHoraResolucion(fechaResolucion);
        return informeRepo.save(informe);
    }

    public InformeResolucion buscarPorId(Long id) throws Excepcion {
        return informeRepo.findById(id)
                .orElseThrow(() -> new Excepcion(
                        "id",
                        "No se encontró el informe de resolución con ID " + id,
                        HttpStatus.NOT_FOUND.value()
                ));
    }

    public List<InformeResolucion> listarInformes(Long incidenteId) {
        if (incidenteId != null) {
            return informeRepo.findByIncidenteId(incidenteId);
        }
        return informeRepo.findAll();
    }
    
    
    
 // Pasa la entidad a la respuesta. getIncidente().getId() no necesita cargar el incidente completo
    private InformeResolucionResponse toResponse(InformeResolucion i) {
        InformeResolucionResponse r = new InformeResolucionResponse();
        r.setId(i.getId());
        r.setIncidenteId(i.getIncidente().getId());
        r.setFechaHoraResolucion(i.getFechaHoraResolucion());
        r.setDescripcionTrabajo(i.getDescripcionTrabajo());
        r.setCosto(i.getCosto());
        r.setProveedor(i.getProveedor());
        r.setResponsableTecnico(i.getResponsableTecnico());
        r.setObservaciones(i.getObservaciones());
        return r;
    }
    
}