package tuti.daos.servicios;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tuti.daos.entidades.InformeResolucion;
import tuti.daos.entidades.Incidente;
import tuti.daos.accesoDatos.InformeResolucionRepository;
import java.time.LocalDateTime;

@Service
public class InformeResolucionService {

    private final InformeResolucionRepository repository;

    public InformeResolucionService(InformeResolucionRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public InformeResolucion registrarResolucion(Long incidenteId, InformeResolucion informe) {
        // Acá luego buscaremos el incidente real en la BD. Por ahora armamos un mock para que guarde.
        Incidente incidenteMock = new Incidente();
        incidenteMock.setId(incidenteId);
        
        informe.setIncidente(incidenteMock);
        informe.setFechaHoraResolucion(LocalDateTime.now());
        
        return repository.save(informe);
    }
}
