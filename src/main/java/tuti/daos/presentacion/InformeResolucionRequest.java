package tuti.daos.presentacion;

import java.math.BigDecimal;
import tuti.daos.entidades.InformeResolucion;
import jakarta.validation.constraints.*;

public class InformeResolucionRequest {
    @NotNull
    private Long incidenteId; 
    @NotBlank
    private String descripcionTrabajo;
    @NotNull
    @Min(value = 0)
    private BigDecimal costo;
    private String proveedor;
    @NotBlank
    private String responsableTecnico;
    private String observaciones; 

    public InformeResolucion toEntity() {
        InformeResolucion informe = new InformeResolucion();
        informe.setDescripcionTrabajo(this.descripcionTrabajo);
        informe.setCosto(this.costo);
        informe.setProveedor(this.proveedor);
        informe.setResponsableTecnico(this.responsableTecnico);
        informe.setObservaciones(this.observaciones);
        return informe;
    }

    public Long getIncidenteId() { return incidenteId; }
    public void setIncidenteId(Long incidenteId) { this.incidenteId = incidenteId; }
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
}