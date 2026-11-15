
package tuti.daos.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;



@Entity
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contrato_id", nullable = false)
    private Long contratoId;

    @Column(name = "periodo_facturado", nullable = false, length = 7) // Formato AAAA-MM
    private String periodoFacturado;

    @Column(columnDefinition = "TEXT")
    private String concepto;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(nullable = false)
    private BigDecimal importe;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoFactura estado;

    

    // getter y setter
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

	public EstadoFactura getEstado() {
		return estado;
	}

	public void setEstado(EstadoFactura estado) {
		this.estado = estado;
	}

	public Factura(Long id, Long contratoId, String periodoFacturado, String concepto, LocalDate fechaEmision,
			LocalDate fechaVencimiento, BigDecimal importe, EstadoFactura estado) {
		super();
		this.id = id;
		this.contratoId = contratoId;
		this.periodoFacturado = periodoFacturado;
		this.concepto = concepto;
		this.fechaEmision = fechaEmision;
		this.fechaVencimiento = fechaVencimiento;
		this.importe = importe;
		this.estado = estado;
	}

	public Factura() {
		super();
	}




	
    
}


