package tuti.daos.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tuti.daos.accesoDatos.IncidenteRepository;
import tuti.daos.entidades.Incidente;
import tuti.daos.presentacion.IncidenteRequest;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IncidenteService {

    @Autowired
    private IncidenteRepository incidenteRepository;

    @Transactional
    public Incidente registrarIncidente(IncidenteRequest request) {
        validarDatosAlta(request);

        LocalDateTime ahora = LocalDateTime.now();

        Incidente incidente = new Incidente();
        incidente.setContratoId(request.getContratoId());
        incidente.setTitulo(request.getTitulo().trim());
        incidente.setDescripcion(request.getDescripcion().trim());
        incidente.setCategoria(parsearCategoria(request.getCategoria()));
        incidente.setPrioridad(parsearPrioridad(request.getPrioridad()));
        incidente.setFechaHoraAlta(ahora);
        incidente.setEstado(Incidente.EstadoIncidente.ABIERTO);

        return incidenteRepository.save(incidente);
    }

    @Transactional
    public Incidente cambiarEstado(Long id, String nuevoEstadoStr) {
        if (nuevoEstadoStr == null || nuevoEstadoStr.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe indicar el nuevo estado.");
        }

        Incidente.EstadoIncidente nuevoEstado = parsearEstado(nuevoEstadoStr);

        Incidente incidente = incidenteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Incidente no encontrado con ID: " + id));

        Incidente.EstadoIncidente estadoActual = incidente.getEstado();
        validarTransicion(estadoActual, nuevoEstado);

        incidente.setEstado(nuevoEstado);

        return incidenteRepository.save(incidente);
    }

    @Transactional(readOnly = true)
    public Incidente consultarPorId(Long id) {
        return incidenteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Incidente no encontrado con ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Incidente> listar(Long contratoId, String estadoStr, String categoriaStr, String prioridadStr) {
        Incidente.EstadoIncidente estado = (estadoStr != null && !estadoStr.isBlank()) ? parsearEstado(estadoStr) : null;
        Incidente.CategoriaIncidente categoria = (categoriaStr != null && !categoriaStr.isBlank()) ? parsearCategoria(categoriaStr) : null;
        Incidente.PrioridadIncidente prioridad = (prioridadStr != null && !prioridadStr.isBlank()) ? parsearPrioridad(prioridadStr) : null;

        return incidenteRepository.filtrarIncidentes(contratoId, estado, categoria, prioridad);
    }

    private void validarDatosAlta(IncidenteRequest request) {
        if (request == null || request.getContratoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El contrato asociado es obligatorio.");
        }
        if (request.getTitulo() == null || request.getTitulo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El título es obligatorio.");
        }
        if (request.getDescripcion() == null || request.getDescripcion().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La descripción es obligatoria.");
        }
        if (request.getCategoria() == null || request.getCategoria().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La categoría es obligatoria.");
        }
        if (request.getPrioridad() == null || request.getPrioridad().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La prioridad es obligatoria.");
        }
    }

    private void validarTransicion(Incidente.EstadoIncidente actual, Incidente.EstadoIncidente nuevo) {
        if (actual == Incidente.EstadoIncidente.CANCELADO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Los incidentes cancelados no admiten nuevas transiciones.");
        }

        boolean valido = switch (actual) {
            case ABIERTO -> nuevo == Incidente.EstadoIncidente.EN_PROCESO || nuevo == Incidente.EstadoIncidente.CANCELADO;
            case EN_PROCESO -> nuevo == Incidente.EstadoIncidente.RESUELTO || nuevo == Incidente.EstadoIncidente.CANCELADO;
            case RESUELTO -> nuevo == Incidente.EstadoIncidente.REABIERTO;
            case REABIERTO -> nuevo == Incidente.EstadoIncidente.EN_PROCESO;
            default -> false;
        };

        if (!valido) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Transición de estado no permitida desde " + actual + " hacia " + nuevo);
        }
    }

    private Incidente.CategoriaIncidente parsearCategoria(String valor) {
        try {
            return Incidente.CategoriaIncidente.valueOf(valor.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoría inválida. Valores permitidos: PLOMERIA, ELECTRICIDAD, GAS, GENERAL.");
        }
    }

    private Incidente.PrioridadIncidente parsearPrioridad(String valor) {
        try {
            return Incidente.PrioridadIncidente.valueOf(valor.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Prioridad inválida. Valores permitidos: BAJA, MEDIA, ALTA.");
        }
    }

    private Incidente.EstadoIncidente parsearEstado(String valor) {
        try {
            return Incidente.EstadoIncidente.valueOf(valor.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado inválido. Valores permitidos: ABIERTO, EN_PROCESO, RESUELTO, CANCELADO, REABIERTO.");
        }
    }
}