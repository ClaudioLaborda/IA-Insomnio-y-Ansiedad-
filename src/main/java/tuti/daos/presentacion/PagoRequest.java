package tuti.daos.presentacion;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.*;
import tuti.daos.enums.MediosPagos;


// PagoRequest representa el contrato de entrada. Recibe la data y lo convierte en obj Java


public class PagoRequest {
	
	
	@NotNull private Long facturaId;
	private LocalDate fechaPago;
	
	@NotNull private MediosPagos medioPago;
	
	@NotNull @Positive private BigDecimal importePagado;
	
	@NotNull @PositiveOrZero private BigDecimal Interes;

	
	
	
	// Getters
	public Long getFacturaId() {
		return facturaId;
	}

	public LocalDate getFechaPago() {
		return fechaPago;
	}

	public MediosPagos getMedioPago() {
		return medioPago;
	}

	public BigDecimal getImportePagado() {
		return importePagado;
	}

	public BigDecimal getInteres() {
		return Interes;
	}

	
	
	
	// Setters
	public void setFacturaId(Long facturaId) {
		this.facturaId = facturaId;
	}

	public void setFechaPago(LocalDate fechaPago) {
		this.fechaPago = fechaPago;
	}

	public void setMedioPago(MediosPagos medioPago) {
		this.medioPago = medioPago;
	}

	public void setImportePagado(BigDecimal importePagado) {
		this.importePagado = importePagado;
	}

	public void setInteres(BigDecimal interes) {
		Interes = interes;
	}

	
	
	
	
}