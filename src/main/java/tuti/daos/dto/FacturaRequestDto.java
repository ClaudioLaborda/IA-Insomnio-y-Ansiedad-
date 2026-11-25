
package tuti.daos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class FacturaRequestDto {

	@NotNull(message = "El contrato es obligatorio")
	
    private Long contratoId;
    
    //@NotBlank(message = "El período es obligatorio")
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = "El formato debe ser AAAA-MM")
    private String periodo;
   
    
    private String concepto; // Opcional para facturación mensual, obligatorio/libre para adicionales
   
   
    private LocalDate fechaEmision; // Opcional (si viene vacía, se autocompleta)
   
  
    private LocalDate fechaVencimiento; // Opcional (si viene vacía, se calcula)
    
    
    
    private BigDecimal importe; // Opcional (si viene vacío, se toma del contrato)

    
    // Getters y Setters
	public Long getContratoId() {
		return contratoId;
	}

	public void setContratoId(Long contratoId) {
		this.contratoId = contratoId;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
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
    
      
}
