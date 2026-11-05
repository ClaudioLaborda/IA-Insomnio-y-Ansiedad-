package tuti.daos.entidades;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contrato")
public class Contrato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquilino_id", nullable = false)
    private Persona inquilino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propietario_id", nullable = false)
    private Persona propietario;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "duracion_meses", nullable = false)
    private Integer duracionMeses;

    @Column(name = "importe_mensual", nullable = false, precision = 10, scale = 2)
    private BigDecimal importeMensual;

    @Column(name = "dia_vencimiento_mensual", nullable = false)
    private Integer diaVencimientoMensual;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "eliminado_logicamente", nullable = false)
    private Boolean eliminadoLogicamente = false;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Persona getInquilino() { return inquilino; }
    public void setInquilino(Persona inquilino) { this.inquilino = inquilino; }
    public Persona getPropietario() { return propietario; }
    public void setPropietario(Persona propietario) { this.propietario = propietario; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public Integer getDuracionMeses() { return duracionMeses; }
    public void setDuracionMeses(Integer duracionMeses) { this.duracionMeses = duracionMeses; }
    public BigDecimal getImporteMensual() { return importeMensual; }
    public void setImporteMensual(BigDecimal importeMensual) { this.importeMensual = importeMensual; }
    public Integer getDiaVencimientoMensual() { return diaVencimientoMensual; }
    public void setDiaVencimientoMensual(Integer diaVencimientoMensual) { this.diaVencimientoMensual = diaVencimientoMensual; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public Boolean getEliminadoLogicamente() { return eliminadoLogicamente; }
    public void setEliminadoLogicamente(Boolean eliminadoLogicamente) { this.eliminadoLogicamente = eliminadoLogicamente; }
}