package tuti.daos.presentacion;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
    public ResponseEntity<EntityModel<InformeResolucion>> resolverIncidente(
            @RequestBody InformeResolucionRequest request) { 
        
        InformeResolucion informe = service.registrarResolucion(request.getIncidenteId(), request.toEntity());
        
        EntityModel<InformeResolucion> resource = EntityModel.of(informe);
        resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass())
                .consultarInforme(informe.getId())).withSelfRel());
                
        return ResponseEntity.status(HttpStatus.CREATED).body(resource); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<InformeResolucion>> consultarInforme(@PathVariable Long id) {
        return null; // A implementar en el futuro
    }
}