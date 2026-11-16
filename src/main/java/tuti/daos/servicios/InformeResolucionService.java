package tuti.daos.servicios;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tuti.daos.accesoDatos.HistorialEstadoIncidenteRepository;
import tuti.daos.accesoDatos.IncidenteRepository;
import tuti.daos.accesoDatos.InformeResolucionRepository;
import tuti.daos.entidades.HistorialEstadoIncidente;
import tuti.daos.entidades.Incidente;
import tuti.daos.entidades.InformeResolucion;

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
    public InformeResolucion registrarResolucion(Long incidenteId, InformeResolucion informe) {
        Incidente incidente = incidenteRepo.findById(incidenteId)
                .orElseThrow(() -> new IllegalArgumentException("El incidente con ID " + incidenteId + " no existe."));

        String estadoActual = incidente.getEstado();
        if (!"En proceso".equalsIgnoreCase(estadoActual)) {
            throw new IllegalStateException(
                "No se puede resolver el incidente porque su estado actual es '" + estadoActual + "' (debe estar 'En proceso')."
            );
        }

        // Misma fecha y hora exacta para el informe y la transición en el historial
        LocalDateTime fechaResolucion = LocalDateTime.now();

        // 1. Actualizar estado del incidente
        incidente.setEstado("Resuelto");
        incidenteRepo.save(incidente);

        // 2. Registrar transición en el historial de estados
        HistorialEstadoIncidente historial = new HistorialEstadoIncidente();
        historial.setIncidente(incidente);
        historial.setEstadoAnterior(estadoActual);
        historial.setEstadoNuevo("Resuelto");
        historial.setFechaHora(fechaResolucion);
        historialRepo.save(historial);

        // 3. Guardar el informe de resolución
        informe.setIncidente(incidente);
        informe.setFechaHoraResolucion(fechaResolucion);
        return informeRepo.save(informe);
    }

    public InformeResolucion buscarPorId(Long id) {
        return informeRepo.findById(id).orElse(null);
    }

    public List<InformeResolucion> listarInformes(Long incidenteId) {
        if (incidenteId != null) {
            return informeRepo.findByIncidenteId(incidenteId);
        }
        return informeRepo.findAll();
    }
}