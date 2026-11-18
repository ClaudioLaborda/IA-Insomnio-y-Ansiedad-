package tuti.daos.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tuti.daos.entidades.Incidente;
import tuti.daos.servicios.IncidenteService;

import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/incidentes")
public class IncidenteController {

    @Autowired
    private IncidenteService incidenteService;

    @PostMapping
    public ResponseEntity<?> registrarIncidente(@RequestBody IncidenteRequest request) {
        try {
            Incidente nuevo = incidenteService.registrarIncidente(request);
            agregarEnlacesHateoas(nuevo);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("error", e.getReason()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> consultarIncidente(@PathVariable Long id) {
        try {
            Incidente incidente = incidenteService.consultarPorId(id);
            agregarEnlacesHateoas(incidente);
            return ResponseEntity.ok(incidente);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("error", e.getReason()));
        }
    }

    @GetMapping
    public ResponseEntity<?> listarIncidentes(
            @RequestParam(required = false) Long contratoId,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String prioridad) {
        try {
            List<Incidente> lista = incidenteService.listar(contratoId, estado, categoria, prioridad);
            lista.forEach(this::agregarEnlacesHateoas);
            return ResponseEntity.ok(lista);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("error", e.getReason()));
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestBody IncidenteRequest request) {
        try {
            Incidente actualizado = incidenteService.cambiarEstado(id, request != null ? request.getEstado() : null);
            agregarEnlacesHateoas(actualizado);
            return ResponseEntity.ok(actualizado);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("error", e.getReason()));
        }
    }

    private void agregarEnlacesHateoas(Incidente incidente) {
        incidente.add(linkTo(methodOn(IncidenteController.class).consultarIncidente(incidente.getId())).withSelfRel());
        incidente.add(linkTo(IncidenteController.class).slash("../contratos/" + incidente.getContratoId()).withRel("contrato"));
    }
}