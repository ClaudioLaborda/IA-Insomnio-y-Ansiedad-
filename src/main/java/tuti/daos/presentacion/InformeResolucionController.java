package tuti.daos.presentacion;

import java.util.List;
import jakarta.validation.Valid;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import tuti.daos.entidades.InformeResolucion;
import tuti.daos.excepciones.Excepcion;
import tuti.daos.servicios.InformeResolucionService;

@RestController
@RequestMapping("/informes-resolucion")
public class InformeResolucionController {

    private final InformeResolucionService service;

    public InformeResolucionController(InformeResolucionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<InformeResolucionResponse> resolverIncidente(
            @Valid @RequestBody InformeResolucionRequest request) throws Excepcion {
        InformeResolucion informe = service.registrarResolucion(request.getIncidenteId(), request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResource(informe));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InformeResolucionResponse> consultarInforme(@PathVariable Long id) throws Excepcion {
        InformeResolucion informe = service.buscarPorId(id);
        return ResponseEntity.ok(toResource(informe));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<InformeResolucionResponse>> listarInformes(
            @RequestParam(required = false) Long incidenteId) {

        List<InformeResolucionResponse> informes = service.listarInformes(incidenteId).stream()
                .map(this::toResource)
                .toList();

        CollectionModel<InformeResolucionResponse> collectionModel = CollectionModel.of(informes);
        collectionModel.add(linkTo(methodOn(this.getClass()).listarInformes(incidenteId)).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    private InformeResolucionResponse toResource(InformeResolucion informe) {
        InformeResolucionResponse resource = InformeResolucionResponse.fromEntity(informe);

        try {
            // Enlace HATEOAS 1: self
            resource.add(linkTo(methodOn(this.getClass())
                    .consultarInforme(informe.getId())).withSelfRel());
        } catch (Excepcion e) {
            // methodOn solo arma la URL; no lanza la excepción en ejecución
        }

        // Enlace HATEOAS 2: incidente asociado (usa el id del DTO, no el proxy)
        if (resource.getIncidenteId() != null) {
            String incidenteUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/incidentes/{id}")
                    .buildAndExpand(resource.getIncidenteId())
                    .toUriString();
            resource.add(Link.of(incidenteUrl, "incidente"));
        }

        return resource;
    }
}