package tuti.daos.presentacion;

import java.util.List;
import java.util.Map;
import jakarta.validation.Valid;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import tuti.daos.entidades.InformeResolucion;
import tuti.daos.servicios.InformeResolucionService;

@RestController
@RequestMapping("/informes-resolucion")
public class InformeResolucionController {

    private final InformeResolucionService service;

    public InformeResolucionController(InformeResolucionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> resolverIncidente(@Valid @RequestBody InformeResolucionRequest request) {
        try {
            InformeResolucion informe = service.registrarResolucion(request.getIncidenteId(), request.toEntity());
            return ResponseEntity.status(HttpStatus.CREATED).body(toResource(informe));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> consultarInforme(@PathVariable Long id) {
        InformeResolucion informe = service.buscarPorId(id);
        if (informe == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se encontró el informe de resolución con ID " + id));
        }
        return ResponseEntity.ok(toResource(informe));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<InformeResolucion>>> listarInformes(
            @RequestParam(required = false) Long incidenteId) {

        List<InformeResolucion> informes = service.listarInformes(incidenteId);

        List<EntityModel<InformeResolucion>> informesModel = informes.stream()
                .map(this::toResource)
                .toList();

        CollectionModel<EntityModel<InformeResolucion>> collectionModel = CollectionModel.of(informesModel);
        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass())
                .listarInformes(incidenteId)).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    private EntityModel<InformeResolucion> toResource(InformeResolucion informe) {
        EntityModel<InformeResolucion> resource = EntityModel.of(informe);

        // Enlace HATEOAS 1: self (consulta del propio informe)
        resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass())
                .consultarInforme(informe.getId())).withSelfRel());

        // Enlace HATEOAS 2: incidente (consulta del incidente asociado)
        String incidenteUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/incidentes/{id}")
                .buildAndExpand(informe.getIncidente().getId())
                .toUriString();
        resource.add(Link.of(incidenteUrl, "incidente"));

        return resource;
    }
}