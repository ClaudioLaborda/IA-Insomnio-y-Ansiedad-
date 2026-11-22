package tuti.daos.presentacion;

// PagoResponse representa el contrato de salida. Es lo que devolvemos al consultar

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.*;
import tuti.daos.enums.MediosPagos;


public class PagoResponse {
    private Long id;
    private Long facturaId;
    private LocalDate fechaPago;
    private MediosPagos medioPago;
    private BigDecimal importePagado;
    private BigDecimal interes;
	
    
    
    
    //Setters
    public void setId(Long id) {
		this.id = id;
	}
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
		this.interes = interes;
	}
	
	
	
	// Getters
	public Long getId() {
		return id;
	}
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
		return interes;
	}
    
    
    
}