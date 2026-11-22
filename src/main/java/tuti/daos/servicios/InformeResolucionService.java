package tuti.daos.servicios;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tuti.daos.entidades.InformeResolucion;
import tuti.daos.entidades.Incidente;
import tuti.daos.accesoDatos.InformeResolucionRepository;
import tuti.daos.accesoDatos.IncidenteRepository;
import java.time.LocalDateTime;

@Service
public class InformeResolucionService {

    private final InformeResolucionRepository informeRepo;
    private final IncidenteRepository incidenteRepo;

    public InformeResolucionService(InformeResolucionRepository informeRepo, IncidenteRepository incidenteRepo) {
        this.informeRepo = informeRepo;
        this.incidenteRepo = incidenteRepo;
    }

    @Transactional
    public InformeResolucion registrarResolucion(Long incidenteId, InformeResolucion informe) {
        Incidente incidente = incidenteRepo.findById(incidenteId)
                .orElseThrow(() -> new IllegalArgumentException("El incidente no existe."));

        if (!"En proceso".equalsIgnoreCase(incidente.getEstado())) {
            throw new IllegalStateException("El incidente no se encuentra en proceso.");
        }

        incidente.setEstado("Resuelto");
        informe.setIncidente(incidente);
        informe.setFechaHoraResolucion(LocalDateTime.now());
        
        incidenteRepo.save(incidente);
        return informeRepo.save(informe);
    }
    
    public InformeResolucion buscarPorId(Long id) {
        return informeRepo.findById(id).orElse(null);
    }
}