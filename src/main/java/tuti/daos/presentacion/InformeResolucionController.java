package tuti.daos.presentacion;

import java.util.List;
import jakarta.validation.Valid;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            
            EntityModel<InformeResolucion> resource = EntityModel.of(informe);
            resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass())
                    .consultarInforme(informe.getId())).withSelfRel());
                    
            return ResponseEntity.status(HttpStatus.CREATED).body(resource); 
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<InformeResolucion>> consultarInforme(@PathVariable Long id) {
        InformeResolucion informe = service.buscarPorId(id);
        if (informe == null) {
            return ResponseEntity.notFound().build();
        }
        
        EntityModel<InformeResolucion> resource = EntityModel.of(informe);
        resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass())
                .consultarInforme(informe.getId())).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<InformeResolucion>>> listarInformes(
            @RequestParam(required = false) Long incidenteId) {
        
        List<InformeResolucion> informes = service.listarInformes(incidenteId);
        
        List<EntityModel<InformeResolucion>> informesModel = informes.stream()
            .map(informe -> {
                EntityModel<InformeResolucion> resource = EntityModel.of(informe);
                resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass())
                        .consultarInforme(informe.getId())).withSelfRel());
                return resource;
            }).toList();

        CollectionModel<EntityModel<InformeResolucion>> collectionModel = CollectionModel.of(informesModel);
        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass())
                .listarInformes(incidenteId)).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }
}