
package tuti.daos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.hateoas.RepresentationModel;

public class FacturaResponseDto extends RepresentationModel<FacturaResponseDto> {
    private Long id;
    private Long contratoId;
    private String periodoFacturado;
    private String concepto;
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;
    private BigDecimal importe;
    private String estado;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getContratoId() {
		return contratoId;
	}
	public void setContratoId(Long contratoId) {
		this.contratoId = contratoId;
	}
	public String getPeriodoFacturado() {
		return periodoFacturado;
	}
	public void setPeriodoFacturado(String periodoFacturado) {
		this.periodoFacturado = periodoFacturado;
	}
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	public LocalDate getFechaEmision() {
		return fechaEmision;
	}
	public void setFechaEmision(LocalDate fechaEmision) {
		this.fechaEmision = fechaEmision;
	}
	public LocalDate getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(LocalDate fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	
	
	public BigDecimal getImporte() {
		return importe;
	}
	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
    
    
    // Getters y Setters
    
    
}