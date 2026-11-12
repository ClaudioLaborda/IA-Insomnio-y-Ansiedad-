package tuti.daos.entidades;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // <-- ¡Faltaba este import!

@Entity
@Table(name = "incidente")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Incidente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String estado = "En proceso";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}