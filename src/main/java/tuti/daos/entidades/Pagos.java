package tuti.daos.entidades;


import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.persistence.*;
import tuti.daos.enums.MediosPagos;


@Entity
@Table(name = "pago", uniqueConstraints = @UniqueConstraint(name = "uk_pago_factura", columnNames = "factura_id"))
public class Pagos {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ID;
	
	/*
	@Column(name = "factura_id", nullable = false)
	private Factura factura;
	*/
	
	@Column (nullable = false)
	private LocalDate fechaPago;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MediosPagos medioPago;
	
	@Column (nullable = false, precision=12 , scale = 2)
	private BigDecimal importePagado;
	
	@Column (nullable = false, precision=12 , scale = 2)
	private BigDecimal interes;

	
	
	
	
	// Setters
	
	public void setID(Long iD) {
		ID = iD;
	}

	/*
	public void setFactura(Factura factura) {
		this.factura = factura;
	}
	*/

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
	public Long getID() {
		return ID;
	}

	/*
	public Factura getFactura() {
		return factura;
	}
	*/
	
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
