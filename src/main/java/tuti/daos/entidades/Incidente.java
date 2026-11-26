package tuti.daos.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;
import java.time.LocalDateTime;

@Entity
@Table(name = "incidente")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Incidente extends RepresentationModel<Incidente> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contrato_id")
    private Long contratoId;

    @Column
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column
    private CategoriaIncidente categoria;

    @Column(name = "fecha_hora_alta")
    private LocalDateTime fechaHoraAlta;

    @Enumerated(EnumType.STRING)
    @Column
    private PrioridadIncidente prioridad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoIncidente estado = EstadoIncidente.EN_PROCESO;

    public enum CategoriaIncidente { PLOMERIA, ELECTRICIDAD, GAS, GENERAL }
    public enum PrioridadIncidente { BAJA, MEDIA, ALTA }
    public enum EstadoIncidente { ABIERTO, EN_PROCESO, RESUELTO, CANCELADO, REABIERTO }

    public Incidente() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getContratoId() { return contratoId; }
    public void setContratoId(Long contratoId) { this.contratoId = contratoId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public CategoriaIncidente getCategoria() { return categoria; }
    public void setCategoria(CategoriaIncidente categoria) { this.categoria = categoria; }

    public LocalDateTime getFechaHoraAlta() { return fechaHoraAlta; }
    public void setFechaHoraAlta(LocalDateTime fechaHoraAlta) { this.fechaHoraAlta = fechaHoraAlta; }

    public PrioridadIncidente getPrioridad() { return prioridad; }
    public void setPrioridad(PrioridadIncidente prioridad) { this.prioridad = prioridad; }

    public EstadoIncidente getEstado() { return estado; }
    public void setEstado(EstadoIncidente estado) { this.estado = estado; }

    // Mantiene compatibilidad si tu servicio asigna el estado como String
    public void setEstado(String estado) {
        if (estado == null) {
            this.estado = null;
            return;
        }
        String normalizado = estado.trim().toUpperCase().replace(" ", "_");
        this.estado = EstadoIncidente.valueOf(normalizado);
    }
}