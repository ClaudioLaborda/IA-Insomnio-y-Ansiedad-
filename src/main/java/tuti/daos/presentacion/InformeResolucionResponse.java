package tuti.daos.presentacion;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import tuti.daos.entidades.InformeResolucion;



public class InformeResolucionResponse extends RepresentationModel<InformeResolucionResponse> {

    private Long id;
    private Long incidenteId;		// solo el id, no el objeto Incidente (evita el proxy lazy)
    private LocalDateTime fechaHoraResolucion;
    private String descripcionTrabajo;
    private BigDecimal costo;
    private String proveedor;
    private String responsableTecnico;
    private String observaciones;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIncidenteId() { return incidenteId; }
    public void setIncidenteId(Long incidenteId) { this.incidenteId = incidenteId; }
    public LocalDateTime getFechaHoraResolucion() { return fechaHoraResolucion; }
    public void setFechaHoraResolucion(LocalDateTime fechaHoraResolucion) { this.fechaHoraResolucion = fechaHoraResolucion; }
    public String getDescripcionTrabajo() { return descripcionTrabajo; }
    public void setDescripcionTrabajo(String descripcionTrabajo) { this.descripcionTrabajo = descripcionTrabajo; }
    public BigDecimal getCosto() { return costo; }
    public void setCosto(BigDecimal costo) { this.costo = costo; }
    public String getProveedor() { return proveedor; }
    public void setProveedor(String proveedor) { this.proveedor = proveedor; }
    public String getResponsableTecnico() { return responsableTecnico; }
    public void setResponsableTecnico(String responsableTecnico) { this.responsableTecnico = responsableTecnico; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    
    public static InformeResolucionResponse fromEntity(InformeResolucion informe) {
        InformeResolucionResponse r = new InformeResolucionResponse();
        r.setId(informe.getId());
        // getId() sobre el proxy NO lo inicializa: Hibernate ya conoce el id
        r.setIncidenteId(informe.getIncidente() != null ? informe.getIncidente().getId() : null);
        r.setFechaHoraResolucion(informe.getFechaHoraResolucion());
        r.setDescripcionTrabajo(informe.getDescripcionTrabajo());
        r.setCosto(informe.getCosto());
        r.setProveedor(informe.getProveedor());
        r.setResponsableTecnico(informe.getResponsableTecnico());
        r.setObservaciones(informe.getObservaciones());
        return r;
    }
    
}