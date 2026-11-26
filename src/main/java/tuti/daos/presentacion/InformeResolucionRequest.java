package tuti.daos.presentacion;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;
import tuti.daos.entidades.InformeResolucion;

public class InformeResolucionRequest {

    @NotNull(message = "El identificador del incidente es obligatorio.")
    private Long incidenteId;

    @NotBlank(message = "La descripción del trabajo realizado es obligatoria.")
    private String descripcionTrabajo;

    @NotNull(message = "El costo es obligatorio (informar 0 si no corresponde).")
    @DecimalMin(value = "0.0", inclusive = true, message = "El costo debe ser mayor o igual a cero.")
    private BigDecimal costo;

    private String proveedor;

    @NotBlank(message = "El responsable o técnico es obligatorio.")
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