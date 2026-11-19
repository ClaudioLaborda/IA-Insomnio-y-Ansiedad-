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

        String estadoActual = incidente.getEstado();
        if (!"En proceso".equalsIgnoreCase(estadoActual)) {
            throw new Excepcion(
                    "incidenteId",
                    "No se puede resolver el incidente porque su estado actual es '" + estadoActual + "' (debe estar 'En proceso').",
                    HttpStatus.CONFLICT.value()
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
}