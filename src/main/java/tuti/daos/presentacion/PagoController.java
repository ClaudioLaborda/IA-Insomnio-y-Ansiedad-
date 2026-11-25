package tuti.daos.presentacion;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import tuti.daos.servicios.PagoService;

// "hub" de entrada y salida. Expone los endpoints REST
@RestController		// Indica que la clase funciona como REST
@RequestMapping("/pagos")	// Indica la ruta
public class PagoController {		// Clase principal

    private final PagoService pagoService;		// Variable que conecta con Service

    public PagoController(PagoService pagoService) {		// Constructor, el cual recibe el service
        this.pagoService = pagoService;		// Se le pasa la instacia Service
    }

    
    
    
    
    // POST /pagos -> registra un pago (201 + Location)
    @PostMapping
    public ResponseEntity<PagoResponse> registrar(@Valid @RequestBody PagoRequest req) {	
    	// ResponseEntity: Respuesta HTTP cuyo cuerpo es un PagoResponse
    	// Valid, antes de entrar al metodo, lo valida
    	// RequestBody Toma el json de la peticion y lo convierte en un obj
    	
        PagoResponse creado = pagoService.registrar(req);  // Crea un obj llamado "creado" se delega toda la logia al service y guarda lo que devuelve en el obj
        URI location = ServletUriComponentsBuilder.fromCurrentRequest() // Se arma la URL, fromCurrentRequest, toma parte de la peticion actual
                .path("/{id}")	// Aca se agrega el ID en el marcador
                .buildAndExpand(creado.getId())	// Se remplaza el ID por el ID real
                .toUri();	// Lo conviernte un URI
        
        
        return ResponseEntity.created(location).body(creado); 	// Devuelve con un codigo 201 (Create) y agrega el header locacion (URI). 
    }

    
    
    
    
    @GetMapping("/{id}")   // Se atiende la url /pagos/ID -> GET /pagos/327 
    public PagoResponse obtener(@PathVariable Long id) {	// Toma el valor de la URL y lo pasa como parametro
        return pagoService.obtener(id);   // Devuelve el pagoService. Si es correcto devuelve 200 OK
    }

    
    
    
    
    
    @GetMapping // atiende GET /pagos
    public List<PagoResponse> listar(@RequestParam(required = false) Long facturaId) { // Toma el parametro de la query String, lo que va despues del ?
        return pagoService.listar(facturaId);		// Devuelve una lista de con los ID de las facturas
    }
}
