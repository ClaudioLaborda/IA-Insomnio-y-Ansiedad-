package tuti.daos.entidades;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contratos")
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relaciones de tu modelo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquilino_id")
    private Persona inquilino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propietario_id")
    private Persona propietario;

    // Campos agregados por Lucas (sin nullable = false para no romper filas existentes)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propiedad_id")
    private Propiedad propiedad;

    @Column(name = "inquilino_nombre")
    private String inquilinoNombre;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "duracion_meses", nullable = false)
    private Integer duracionMeses;

    @Column(name = "importe_mensual", nullable = false, precision = 12, scale = 2)
    private BigDecimal importeMensual;

    @Column(name = "dia_vencimiento_mensual", nullable = false)
    private Integer diaVencimientoMensual;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "eliminado_logicamente", nullable = false)
    private Boolean eliminadoLogicamente = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoContrato estado = EstadoContrato.ACTIVO;

    // Constructores
    public Contrato() {
        super();
    }

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

    // Getters y Setters (combinados para ambos módulos)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Persona getInquilino() {
        return inquilino;
    }

    public void setInquilino(Persona inquilino) {
        this.inquilino = inquilino;
    }

    public Persona getPropietario() {
        return propietario;
    }

    public void setPropietario(Persona propietario) {
        this.propietario = propietario;
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

    public Integer getDuracionMeses() {
        return duracionMeses;
    }

    public void setDuracionMeses(Integer duracionMeses) {
        this.duracionMeses = duracionMeses;
    }

    public BigDecimal getImporteMensual() {
        return importeMensual;
    }

    public void setImporteMensual(BigDecimal importeMensual) {
        this.importeMensual = importeMensual;
    }

    public Integer getDiaVencimientoMensual() {
        return diaVencimientoMensual;
    }

    public void setDiaVencimientoMensual(Integer diaVencimientoMensual) {
        this.diaVencimientoMensual = diaVencimientoMensual;
    }

    public Boolean getActivo() {
        return activo;
    }

    public boolean isActivo() {
        return Boolean.TRUE.equals(activo);
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean getEliminadoLogicamente() {
        return eliminadoLogicamente;
    }

    public void setEliminadoLogicamente(Boolean eliminadoLogicamente) {
        this.eliminadoLogicamente = eliminadoLogicamente;
    }

    public EstadoContrato getEstado() {
        return estado;
    }

    public void setEstado(EstadoContrato estado) {
        this.estado = estado;
    }
}