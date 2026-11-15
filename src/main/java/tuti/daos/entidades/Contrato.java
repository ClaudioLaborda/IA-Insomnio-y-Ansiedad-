
package tuti.daos.entidades;


import java.math.BigDecimal;
import java.time.LocalDate;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "contratos")
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propiedad_id", nullable = false)
    private Propiedad propiedad;

    @Column(name = "inquilino_nombre", nullable = false)
    private String inquilinoNombre; 
    
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "duracion_meses", nullable = false)
    private int duracionMeses; 

    @Column(name = "importe_mensual", nullable = false, precision = 12, scale = 2)
    private BigDecimal importeMensual; 

    @Column(name = "dia_vencimiento_mensual", nullable = false)
    private int diaVencimientoMensual; 
    
    @Column(nullable = false)
    private boolean activo = true;

    

    // 1. Agrega este atributo junto a los demás campos de la clase
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    
    private EstadoContrato estado = EstadoContrato.ACTIVO; // o el valor por defecto que prefieras

    // 2. Agrega el getter y setter correspondientes
    public EstadoContrato getEstado() {
        return estado;
    }

    public void setEstado(EstadoContrato estado) {
        this.estado = estado;
    }

   
    
    //Constructores
    
 // Constructor vacío (Obligatorio para JPA/Hibernate)
    public Contrato() {
        super();
    }

    // Tu constructor con parámetros existente
    public Contrato(Long id, Propiedad propiedad, String inquilinoNombre, LocalDate fechaInicio, int duracionMeses,
            BigDecimal importeMensual, int diaVencimientoMensual, boolean activo) {
        super();
        this.id = id;
        this.propiedad = propiedad;
        this.inquilinoNombre = inquilinoNombre;
        this.fechaInicio = fechaInicio;
        this.duracionMeses = duracionMeses;
        this.importeMensual = importeMensual;
        this.diaVencimientoMensual = diaVencimientoMensual;
        this.activo = activo;
    
    }
    
    // Getters, Setters
    
    
    public Long getId() {
		return id;
	}	

	
	public void setId(Long id) {
		this.id = id;
	}

	public Propiedad getPropiedad() {
		return propiedad;
	}

	public void setPropiedad(Propiedad propiedad) {
		this.propiedad = propiedad;
	}

	public String getInquilinoNombre() {
		return inquilinoNombre;
	}

	public void setInquilinoNombre(String inquilinoNombre) {
		this.inquilinoNombre = inquilinoNombre;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public int getDuracionMeses() {
		return duracionMeses;
	}

	public void setDuracionMeses(int duracionMeses) {
		this.duracionMeses = duracionMeses;
	}

	public BigDecimal getImporteMensual() {
		return importeMensual;
	}

	public void setImporteMensual(BigDecimal importeMensual) {
		this.importeMensual = importeMensual;
	}

	public int getDiaVencimientoMensual() {
		return diaVencimientoMensual;
	}

	public void setDiaVencimientoMensual(int diaVencimientoMensual) {
		this.diaVencimientoMensual = diaVencimientoMensual;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	
    
}