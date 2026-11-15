
package tuti.daos.presentacion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.Link;
import jakarta.validation.Valid;
import tuti.daos.dto.FacturaRequestDto;
import tuti.daos.dto.FacturaResponseDto;
import tuti.daos.entidades.EstadoFactura;
import tuti.daos.servicios.FacturaService;

@RestController
@RequestMapping("/facturas")
@Tag(name = "Facturas", description = "Gestión de facturas y pagos de alquileres")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @PostMapping
    @Operation(summary = "Emitir una factura")
    public ResponseEntity<FacturaResponseDto> emitirFactura(@Valid @RequestBody FacturaRequestDto request) {
        FacturaResponseDto facturaDto = facturaService.emitirFactura(request);
        
        // Agregar enlaces HATEOAS requeridos
        facturaDto.add(linkTo(methodOn(FacturaController.class).consultarFactura(facturaDto.getId())).withSelfRel());
        facturaDto.add(Link.of("/contratos/" + facturaDto.getContratoId(), "contrato"));

        return ResponseEntity.status(HttpStatus.CREATED).body(facturaDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar una factura por ID")
    public ResponseEntity<FacturaResponseDto> consultarFactura(@PathVariable Long id) {
        FacturaResponseDto facturaDto = facturaService.obtenerPorId(id);
        
        facturaDto.add(linkTo(methodOn(FacturaController.class).consultarFactura(id)).withSelfRel());
        facturaDto.add(Link.of("/contratos/" + facturaDto.getContratoId(), "contrato"));
        
        return ResponseEntity.ok(facturaDto);
    }

    @GetMapping
    @Operation(summary = "Listar y filtrar facturas")
    public ResponseEntity<List<FacturaResponseDto>> listarFacturas(
            @RequestParam(required = false) Long contratoId,
            @RequestParam(required = false) String periodo,
            @RequestParam(required = false) EstadoFactura estado) {
        
        List<FacturaResponseDto> facturas = facturaService.listarFiltradas(contratoId, periodo, estado);
        
        for (FacturaResponseDto dto : facturas) {
            dto.add(linkTo(methodOn(FacturaController.class).consultarFactura(dto.getId())).withSelfRel());
            dto.add(Link.of("/contratos/" + dto.getContratoId(), "contrato"));
        }
        
        return ResponseEntity.ok(facturas);
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Anular una factura")
    public ResponseEntity<FacturaResponseDto> anularFactura(@PathVariable Long id) {
        FacturaResponseDto facturaDto = facturaService.anularFactura(id);
        
        facturaDto.add(linkTo(methodOn(FacturaController.class).consultarFactura(id)).withSelfRel());
        facturaDto.add(Link.of("/contratos/" + facturaDto.getContratoId(), "contrato"));
        
        return ResponseEntity.ok(facturaDto);
    }
}